package com.sp.video.yi.view.base;



/**
 * FragmentTag
 * Author:lanjl
 * Date :15-11-13
 */
public enum MenuFragmentTag {
    DemoFragment(1);

    private int strId;

    MenuFragmentTag(int stringId) {
        strId = stringId;
    }

    public int getStrId() {
        return strId;
    }

}