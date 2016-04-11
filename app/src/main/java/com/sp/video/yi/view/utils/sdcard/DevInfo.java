package com.sp.video.yi.view.utils.sdcard;

/**
 * Created by huangb on 2015/5/18,0018.
 */
public class DevInfo {
    private String label;
    private String mount_point;
    private String path;
    private String sysfs_path;

    public DevInfo() {
    }

    public String getLabel() {
        return this.label;
    }

    protected void setLabel(String label) {
        this.label = label;
    }

    public String getMount_point() {
        return this.mount_point;
    }

    protected void setMount_point(String mount_point) {
        this.mount_point = mount_point;
    }

    public String getPath() {
        return this.path;
    }

    protected void setPath(String path) {
        this.path = path;
    }

    public String getSysfs_path() {
        return this.sysfs_path;
    }

    protected void setSysfs_path(String sysfs_path) {
        this.sysfs_path = sysfs_path;
    }
}