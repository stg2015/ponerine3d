package com.sp.video.yi.view.base;

import android.os.Bundle;

import com.nd.hy.android.commons.util.Ln;


/**
 * ModuleFragmentGenerator
 * Description: 模块控制器
 * Author:lix
 * Date :13-12-6
 */
public class ModuleFragmentGenerator {
    public static BaseFragment getTargetFragment(MenuFragmentTag tag, Bundle bundle) {
        BaseFragment targetFragment = null;
        switch (tag) {

        }
        if (targetFragment != null) {
            if (bundle != null) {
                targetFragment.setArguments(bundle);
            }
            return targetFragment;
        }
        Ln.e("you should define a target fragment for you module");
        return null;
    }


}