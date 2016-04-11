package com.sp.video.yi.view.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.widget.Toast;

import com.nd.hy.android.commons.util.Ln;
import com.sp.video.yi.demo.R;
import com.sp.video.yi.view.utils.sdcard.SdCardStatus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Bryce on 14-6-11.
 */
public class PhotoUtil {

    public static final int TAKE_PICTURE = 0xFF1;
    public static final int PICK_ALBUM   = 0xFF2;
    public static final int CROP_ALBUM   = 0xFF3;

    public static final int SD_NOT_EXISTS      = 0x0;
    public static final int CAPTURE_SUCCESS    = 0x1;
    public static final int CANNOT_FIND_CAMERA = 0x2;

    private static final String DEFAULT_PIC_PATH = SdCardStatus.getSDPath() + File.separator;
    private static final String DEFAULT_PATH     = "NdIndustry";

    private static final PhotoUtil INSTANCE = new PhotoUtil();

    private PhotoUtil() {

    }

    public static final PhotoUtil getInstance() {
        return INSTANCE;
    }


    public void takePhoto(Context context) {
        takePhoto(context, getFilePath());
    }

    public void takePhoto(Context context, String filePath) {
        if (!SdCardStatus.hasSdCard()) {
            Toast.makeText(context, "no sdcard", Toast.LENGTH_SHORT).show();
            return;
        }
        File mPhotoFile = new File(filePath);
        if (!mPhotoFile.exists()) {
            try {
                mPhotoFile.createNewFile();
            } catch (IOException e) {
                Ln.e(e);
            }
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
        ((Activity) context).startActivityForResult(intent, TAKE_PICTURE);
    }

    public void pickAlbum(Context context) {
        final String IMAGE_TYPE = "image/*";
        Intent pickAlbum = new Intent(Intent.ACTION_PICK, null);
        pickAlbum.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_TYPE);
        ((Activity) context).startActivityForResult(pickAlbum, PICK_ALBUM);
    }

    public void cropImage(Context context, Uri uri, int outputX, int outputY){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        if (context instanceof Activity){
            ((Activity)context).startActivityForResult(intent, CROP_ALBUM);
        } else {
            //TODO throw exception
        }
    }

    public String getFilePath(){
        long curTime = System.currentTimeMillis();
        return DEFAULT_PIC_PATH + curTime + "_" + "PIC.jpg";
    }

    /**
     * bitmap转为base64
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            Ln.e(e);
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                Ln.e(e);
            }
        }
        return result;
    }

    public int takePhoto(Fragment fragment, String filePath){
        if (!SdCardStatus.hasSdCard()) {
            return SD_NOT_EXISTS;
        }
//        File mPhotoFile = new File(filePath);
//        if (!mPhotoFile.exists()) {
//            try {
//                mPhotoFile.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        ContentValues values = new ContentValues();
//        values.put(MediaStore.Images.Media.TITLE, filePath);
//        Uri mCapturedImageURI = fragment.getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(filePath)));
        if(intent.resolveActivity(fragment.getActivity().getPackageManager()) != null){
            fragment.startActivityForResult(intent, TAKE_PICTURE);
            return CAPTURE_SUCCESS;
        }
        return CANNOT_FIND_CAMERA;
    }

    public boolean pickAlbum(Fragment fragment){
        final String IMAGE_TYPE = "image/*";
        Intent pickAlbum = new Intent(Intent.ACTION_PICK, null);
        pickAlbum.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_TYPE);
        Activity act = fragment.getActivity();
        if(act != null && pickAlbum.resolveActivity(act.getPackageManager()) != null){
            fragment.startActivityForResult(pickAlbum, PICK_ALBUM);
            return true;
        }
        return false;
    }

    public boolean cropImage(Fragment fragment,Uri inputFileUri, int outputX, int outputY, Uri outputFileUri){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(inputFileUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        File photoFile = new File(outputFileUri.getPath());
        if (!photoFile.exists()) {
            try {
                photoFile.createNewFile();
            } catch (IOException e) {
                Ln.e(e);
            }
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        Activity act = fragment.getActivity();
        if(act != null && intent.resolveActivity(act.getPackageManager()) != null){
            fragment.startActivityForResult(intent, CROP_ALBUM);
            return true;
        }

        return false;
    }

    /**
     * bitmap转为base64
     * @param filePath
     * @return
     */
    public static String bitmapToBase64(String filePath) {

        String result = null;
        ByteArrayOutputStream baos = null;
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(filePath);
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            Ln.e(e);
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
                if(bitmap != null){
                    bitmap.recycle();
                }
            } catch (IOException e) {
                Ln.e(e);
            }
        }
        return result;
    }

    /**
     * 获取guid
     *
     * @return
     */
    public static String GetGUID() {
        return UUID.randomUUID().toString();
    }

    public static final String TempDir       = "temp";
    public static       char   separatorChar = System
            .getProperty("file.separator", "/").charAt(0);

    /**
     * 获取临时存放目录
     *
     * @return
     */
    public static int getTempPath(StringBuilder sbPath) {
        return getNotePath(TempDir, sbPath);
    }

    /**
     *
     * @param dir
     *            目录类型
     * @param sbPath
     *            返回路径
     * @return
     */
    private static int getNotePath(String dir, StringBuilder sbPath) {
        int ireturn = R.string.sp_no_sdcard_found;
        try {
            sbPath.delete(0, sbPath.length());
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                String sdcardpath = Environment.getExternalStorageDirectory()
                        .getAbsolutePath();
                sbPath.append(sdcardpath).append(separatorChar)
                        .append(DEFAULT_PATH);

                File noteDir = new File(sbPath.toString());
                if (!noteDir.exists())
                    noteDir.mkdir();
                sbPath.append(separatorChar).append(dir);
                sbPath.append(separatorChar);
                noteDir = new File(sbPath.toString());
                if (!noteDir.exists()) {
                    noteDir.mkdir();
                    createNomedia(sbPath);
                }
                ireturn = 0;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return ireturn;
    }

    public static void createNomedia(StringBuilder sbPath) {
        File file = new File(sbPath.toString() + ".nomedia");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 不含点的扩展名
     *
     * @param file
     * @return
     */
    public static String getFileExt(File file) {
        if (file != null) {
            String fileName = file.getName();
            if (fileName.length() > 0) {
                int i = fileName.lastIndexOf('.');
                if ((i > -1) && (i < (fileName.length() - 1))) {
                    return fileName.substring(i + 1);
                }
            }
        }
        return null;
    }

    /**
     * 复制文件
     *
     * @param srcFile
     * @param destFile
     * @return
     */
    public static boolean CopyFile(File srcFile, File destFile) {
        boolean breturn = false;
        FileInputStream src = null;
        FileOutputStream dest = null;
        try {
            src = new FileInputStream(srcFile);
            dest = new FileOutputStream(destFile);
            int bytesum = 0;
            int byteread = 0;
            byte[] buffer = new byte[1024];
            while ((byteread = src.read(buffer)) != -1) {
                bytesum += byteread; // 字节数 文件大小
                dest.write(buffer, 0, byteread);
            }
            breturn = true;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (src != null)
                    src.close();
                if (dest != null)
                    dest.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return breturn;
    }

    /**
     *
     * @n<b>函数名称</b>     :getImgLocalPathByUri
     * @brief 根据图片URI获取本地存放路径
     * @see
     * @since Ver 1.1
     * @param  @param context
     * @param  @param imageUri
     * @param  @return
     * @return String
     * @<b>作者</b>          :  huangszh
     * @<b>创建时间</b>      :  2013-12-25下午2:58:05
     */
    public static String getImgLocalPathByUri(Context context, Uri imageUri) {
        if (imageUri == null) return null;
        // 获取文件的绝对路径  相册
        String path = "";
        String[] projection = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA, MediaStore.Images.Media.SIZE};
        Cursor cursor = MediaStore.Images.Media.query(context.getContentResolver(), imageUri, projection);
        try {
            if (cursor != null && cursor.moveToNext()) {
                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            } else {                    //小米1
                path = imageUri.getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return path;
    }

    // 放大缩小图片
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        return newBmp;
    }

    public static Uri insertImageToSD(Context context, Bitmap bitmap) {
        Uri uri = null;
        // 判断Sd卡是否存在或可写
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Ln.e("SD卡不存在或不可写");
            return uri;
        }
        // 图片所存的ＳＤ卡文件夹
        String saveDirectory = Environment.getExternalStorageDirectory()
                + "/DCIM/camera/";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        // 图片名保存为例如20130109_122351.jpg形式，2013年1月9号，12点23分51秒存的图片
        String imageName = formatter.format(curDate) + ".jpg";
        // 保存图片到指定的位置
        boolean isSaveSuccee = BitmapToolkit.saveBitmap(
                saveDirectory, imageName, bitmap);
        if (bitmap != null && (!bitmap.isRecycled())) {
            bitmap.recycle();
            bitmap = null;
        }
        if (isSaveSuccee) {
            File imageFile = new File(saveDirectory + imageName);
            if (imageFile != null && imageFile.exists()) {
                uri = Uri.fromFile(imageFile);
            }
        }
        return uri;
    }
}