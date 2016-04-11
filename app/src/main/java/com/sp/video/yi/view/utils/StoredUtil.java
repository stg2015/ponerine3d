package com.sp.video.yi.view.utils;

import android.content.Context;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Environment;
import android.os.RemoteException;
import android.os.StatFs;

import com.nd.hy.android.commons.bus.EventBus;
import com.nd.hy.android.commons.util.Ln;
import com.nd.hy.android.hermes.frame.base.AppContextUtil;

import java.io.File;
import java.lang.reflect.Method;

/**
 * 存储空间工具类
 * Created by linqq on 14-6-30.
 * @version 2.1
 */
public class StoredUtil extends IPackageStatsObserver.Stub{

    private String eventName;

    public StoredUtil() {
    }

    public StoredUtil(String eventName) {
        this.eventName = eventName;
    }

    /**
     * 将字节大小格式化为"*KB,*MB,*GB"显示
     *
     * @param sizeStr 需要格式化的字节数据
     * @return 格式化后的空格大小显示
     */
    public static String formatSize2Str(String sizeStr) {
        double valueOfSize = 0;
        try {
            valueOfSize = Double.valueOf(sizeStr);
        } catch (Exception e) {
            valueOfSize = 0;
        }
        return formatSize2Str(valueOfSize);
    }

    /**
     * 将字节大小格式化为"*KB,*MB,*GB"显示
     *
     * @param size 需要格式化的字节数据
     * @return 格式化后的空格大小显示
     */
    public static String formatSize2Str(double size) {
        int iKB = 1024;
        long iMB = 1048576;
        long iGB = 1073741824;

        if (size < iKB)
            return String.format("%.2f B", size);

        if (size >= iKB && size < iMB)
            return String.format("%.2f KB", size / iKB);

        if (size >= iMB && size < iGB)
            return String.format("%.2f MB", size / iMB);

        if (size >= iGB)
            return String.format("%.2f GB", size / iGB);
        return String.format("%.2f bytes", size);
    }

    /**
     * 获取可用空间大小
     * @return 可用空间大小(字节)
     */
    public static long getAvailaleSize(){
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * 获取可用空间大小
     * @param reserve 预留空间字节数(计算可用空间时将被扣除的预留空间大小)
     * @return 可用空间大小(字节)
     */
    public static long getAvailaleSize(long reserve){
        long availaleSize = getAvailaleSize();
        return availaleSize - reserve < 0 ? 0 : availaleSize - reserve;
    }

    /**
     * 获取可用空间大小
     * @return 可用空间大小(格式化表示)
     */
    public static String getAvailaleSizeFormat(){
        return formatSize2Str(getAvailaleSize());
    }

    /**
     * 获取可用空间大小
     * @param reserve 预留空间字节数(计算可用空间时将被扣除的预留空间大小)
     * @return 可用空间大小(格式化表示)
     */
    public static String getAvailaleSizeFormat(long reserve){
        long availaleSize = getAvailaleSize();
        return formatSize2Str(availaleSize-reserve < 0 ? 0 : availaleSize-reserve);
    }

    /**
     * 获取空间大小
     * @return 可用空间大小(字节)
     */
    public static long getTotalSize(){
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long totalBlocks = stat.getBlockCount();
        long blockSize = stat.getBlockSize();
        return totalBlocks*blockSize;
    }

    /**
     * 获取空间大小
     * @return 可用空间大小(格式化表示)
     */
    public static String getTotalSizeFormat(){
        return formatSize2Str(getTotalSize());
    }

    /**
     * 获取指定目录已使用空间(目录大小)
     * @param dir 目录
     * @return 已使用空间
     */
    public static long getUsedSize(File dir){
        if(!dir.isDirectory()) return 0;
        long totalSize = 0;
        for (File _file:dir.listFiles()){
            if(_file.isDirectory()){
                totalSize += getUsedSize(_file);
            }else{
                totalSize += _file.length();
            }
        }
        return totalSize;
    }

    /**
     * 获取指定目录已使用空间(目录大小)
     * @param dir 目录
     * @return 已使用空间
     */
    public static String getUsedSizeStr(File dir){
        return formatSize2Str(getUsedSize(dir));
    }

    /**
     * 清除指定目录下的文件
     * @param directory 需要清理的目录(目录本身不会被清除)
     */
    public static void cleanFilesByDirectory(File directory) {
        if (directory != null && directory.exists()) {
            if(directory.isFile()){
                directory.delete();
                return;
            }
            for (File file: directory.listFiles()){
                if(file.isFile()){
                    file.delete();
                }else{
                    cleanFilesByDirectory(file);
                }
            }
        }
    }

    /**
     * 清除缓存目录下的所有目录及文件.<br>
     *     包含:
     *     <li>"data/data/包名/cache/"</li>
     *     <li>"sdcard/Android/data/包名/cache/"</li>
     */
    public static void cleanCacheDirectory(){
        cleanFilesByDirectory(AppContextUtil.getContext().getCacheDir());
        cleanFilesByDirectory(AppContextUtil.getContext().getExternalCacheDir());
    }

    /**
     * 获取缓存目录大小.<br>
     *     包含:
     *     <li>"data/data/包名/cache/"</li>
     *     <li>"sdcard/Android/data/包名/cache/"</li>
     * @return 缓存目录大小
     */
    public static long getCacheDirSize(){
        long cacheSize = 0;
        if(AppContextUtil.getContext().getCacheDir() != null){
            cacheSize += getUsedSize(AppContextUtil.getContext().getCacheDir());
        }
        if(AppContextUtil.getContext().getExternalCacheDir() != null){
            cacheSize += getUsedSize(AppContextUtil.getContext().getExternalCacheDir());
        }
        return cacheSize;
    }

    /**
     * SD卡是否挂载
     * @return SD卡是否挂载
     * @since 2.1
     */
    public static boolean isSDCardReady(){
        String STATE = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(STATE);
    }

    /**
     * 获取包信息,通过指定的eventName回调.
     * @param context 上下文对象
     * @param eventName eventbus名称
     */
    public static void getPackageStats(Context context,String eventName) {
        String packName = context.getPackageName();
        PackageManager pm = context.getPackageManager();  //得到pm对象
        try {
            //通过反射机制获得该隐藏函数
            Method getPackageSizeInfo = pm.getClass().getMethod("getPackageSizeInfo", String.class,IPackageStatsObserver.class);
            getPackageSizeInfo.invoke(pm, packName,new StoredUtil(eventName));
        }
        catch(Exception ex){
            Ln.e(ex);
        }

    }

    @Override
    public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
        EventBus.postEvent(eventName, pStats);
    }
}