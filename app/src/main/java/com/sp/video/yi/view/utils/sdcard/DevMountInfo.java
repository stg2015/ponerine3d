package com.sp.video.yi.view.utils.sdcard;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by huangb on 2015/5/18,0018.
 */
public class DevMountInfo implements IDev {
    public final String HEAD         = "dev_mount";
    public final String LABEL        = "<label>";
    public final String MOUNT_POINT  = "<mount_point>";
    public final String PATH         = "<part>";
    public final String SYSFS_PATH   = "<sysfs_path1...>";
    private final int               NLABEL       = 1;
    private final int               NPATH        = 2;
    private final int               NMOUNT_POINT = 3;
    private final int               NSYSFS_PATH  = 4;
    private final int               DEV_INTERNAL = 0;
    private final int               DEV_EXTERNAL = 1;
    private ArrayList<String> cache        = new ArrayList();
    private static DevMountInfo dev;
    private        DevInfo      info;
    private final File VOLD_FSTAB;

    public DevMountInfo() {
        this.VOLD_FSTAB = new File(Environment.getRootDirectory().getAbsoluteFile() + File.separator + "etc" + File.separator + "vold.fstab");
    }

    public static DevMountInfo getInstance() {
        if (null == dev) {
            dev = new DevMountInfo();
        }

        return dev;
    }

    private DevInfo getInfo(int device) {
        if(null == this.info) {
            this.info = new DevInfo();
        }

        try {
            this.initVoldFstabToCache();
        } catch (IOException var3) {
            var3.printStackTrace();
        }

        if(device >= this.cache.size()) {
            return null;
        } else {
            String[] sinfo = ((String)this.cache.get(device)).split(" ");
            this.info.setLabel(sinfo[1]);
            this.info.setMount_point(sinfo[3]);
            this.info.setPath(sinfo[2]);
            this.info.setSysfs_path(sinfo[4]);
            return this.info;
        }
    }

    private void initVoldFstabToCache() throws IOException {
        this.cache.clear();
        BufferedReader br = new BufferedReader(new FileReader(this.VOLD_FSTAB));
        String tmp = null;

        while((tmp = br.readLine()) != null) {
            if(tmp.startsWith("dev_mount")) {
                this.cache.add(tmp);
            }
        }

        br.close();
        this.cache.trimToSize();
    }

    public DevInfo getInternalInfo() {
        return this.getInfo(0);
    }

    public DevInfo getExternalInfo() {
        return this.getInfo(1);
    }

    public static String getSDPath() {
        boolean sdCardExist = Environment.getExternalStorageState().equals("mounted");
        if(sdCardExist) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            DevMountInfo dev = getInstance();
            DevInfo info = dev.getExternalInfo();
            String sd2Path = info.getPath();
            return sd2Path != null && sd2Path.length() > 0?sd2Path:null;
        }
    }
}