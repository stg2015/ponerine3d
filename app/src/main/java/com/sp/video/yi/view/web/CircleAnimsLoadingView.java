package com.sp.video.yi.view.web;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sp.video.yi.demo.R;


/**
 * 圆形加载动画
 */
public class CircleAnimsLoadingView extends RelativeLayout {

    private Context context;

    private ImageView imageView;

    private TextView textView;

    public CircleAnimsLoadingView(Context context) {
        super(context);
        this.context = context;

        createAnimationsAndStart();
        addView(imageView);
        textView = new TextView(getContext());
        textView.setText("正在加载");
        textView.setTextSize(13);
        textView.setPadding(10, 0, 0, 0);
//        textView.setTextColor(getResources().getColor(R.color.hy_mui__circle_loading_text_color));
        LayoutParams txtLp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        txtLp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        txtLp.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
        textView.setLayoutParams(txtLp);
        addView(textView);
    }

    /**
     * 创建动画并启动动画
     */
    public void createAnimationsAndStart() {
        imageView = new ImageView(getContext());
        imageView.setBackgroundResource(R.drawable.sp_ic_common_progress);
        LayoutParams lp = new LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout
                .LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        imageView.setLayoutParams(lp);
        imageView.setId(R.id.sp_circle_load_view_id);

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.sp_anim_circle_loading_view);
        LinearInterpolator lir = new LinearInterpolator();
        animation.setInterpolator(lir);
        imageView.startAnimation(animation);
    }

}