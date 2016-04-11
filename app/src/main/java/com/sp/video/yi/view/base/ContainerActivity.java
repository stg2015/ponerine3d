package com.sp.video.yi.view.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.nd.hy.android.commons.data.Restore;
import com.nd.hy.android.commons.util.Ln;
import com.sp.video.yi.constant.BundleKey;
import com.sp.video.yi.demo.R;


/**
 * Created by Bryce on 2015/4/30.
 */
public class ContainerActivity extends BaseActivity {
    private static final String KEY_TAG = "ContainerActivity";

    @Restore(KEY_TAG)
    private MenuFragmentTag fragmentTag;
    private DispatchFragment mDispatchFragment;
    //用来解决在界面切换过程中，退出动画无效问题
    protected int activityCloseEnterAnimation;
    protected int activityCloseExitAnimation;

    public static void start(Context context, MenuFragmentTag tag, Bundle bundle) {
        Intent intent = new Intent(context, ContainerActivity.class);
        Bundle data = new Bundle();
        boolean broughtToFront = false;
        boolean newTask = false;
        if (bundle != null) {
            data.putAll(bundle);
            broughtToFront = bundle.getBoolean(BundleKey.KEY_REORDER_TO_FRONT);
            newTask = bundle.getBoolean(BundleKey.KEY_NEW_TASK);
        }
        data.putSerializable(KEY_TAG, tag);
        intent.putExtras(data);

        //限制多次重复启动处理
        if (broughtToFront) {
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        }
        if (newTask) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.sp_slide_in_from_right_200, R.anim.sp_slide_out_to_bottom);
        }
    }

    @Override
    public void finish() {
        super.finish();
        //当退出的时候重新设置当前动画以保证动画的有效性
        overridePendingTransition(R.anim.sp_slide_in_from_right_400, R.anim.sp_slide_out_from_right_400);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.sp_activity_container;
    }

    @Override
    protected void afterCreate(Bundle bundle) {
        //获取当前activity的切换动画
        TypedArray activityStyle = getTheme().obtainStyledAttributes(new int[]{android.R.attr.windowAnimationStyle});
        int windowAnimationStyleResId = activityStyle.getResourceId(0, 0);
        activityStyle.recycle();
        activityStyle = getTheme().obtainStyledAttributes(windowAnimationStyleResId, new int[]{android.R.attr.activityCloseEnterAnimation, android.R.attr.activityCloseExitAnimation});
        activityCloseEnterAnimation = activityStyle.getResourceId(0, 0);
        activityCloseExitAnimation = activityStyle.getResourceId(1, 0);
        activityStyle.recycle();
        FragmentTransaction tf = getSupportFragmentManager().beginTransaction();
        Fragment fragment = ModuleFragmentGenerator.getTargetFragment(fragmentTag, null);
        if (fragment instanceof DispatchFragment) {
            mDispatchFragment = (DispatchFragment) fragment;
        }
        if (fragment == null) {
            Ln.e("fragment:[%s] is null,forget defined in 'ModuleFragmentGenerator.getTargetFragment()'?", mDispatchFragment);
            return;
        }
        tf.add(R.id.vg_container_content, fragment);
        tf.commit();
    }

    public void setDispatchFragment(DispatchFragment fragment) {
        this.mDispatchFragment = fragment;
    }

    @Override
    public void onBackPressed() {
        if (mDispatchFragment != null) {
            if (mDispatchFragment.handlerBackPressed()) {
                return;
            }
        }
        super.onBackPressed();
    }

    public interface DispatchFragment {
        public boolean handlerBackPressed();
    }




}