package com.sp.video.yi.view.base;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.sp.video.yi.demo.R;


public abstract class BaseToolbarActivity extends BaseActivity {

    protected Toolbar mToolbar;

    protected Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected void onBaseCreate(Bundle state) {
        super.onBaseCreate(state);
        mToolbar = findViewCall(getToolbarId());
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    protected int getToolbarId() {
        return R.id.tb_common;
    }

    protected void setClickNavigationToFinish() {
        getToolbar().setNavigationOnClickListener(view -> finish());
    }
}