package com.sp.video.yi.view.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.sp.video.yi.demo.R;


/**
 * Created by Devilin on 15-7-30.
 */

public class DialogUtils {

    private Activity mActivity = null;

    public DialogUtils(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public interface OnConfirmListener {
        public void onConfirm();
    }

    public interface DialogHandler {
        public void handleLeft();

        public void handleRight();
    }

    @SuppressWarnings("deprecation")
    public void showDialog(String[] args, final DialogHandler dialogHandler) {
        final Dialog dialog = new Dialog(mActivity, R.style.SpDialogCommons);
        LayoutInflater inflater = mActivity.getLayoutInflater();
        View view = inflater.inflate(R.layout.sp_dialog_view, null);
        ((TextView) view.findViewById(R.id.tv_dialog_title)).setText(args[0]);
        ((TextView) view.findViewById(R.id.tv_dialog_content)).setText(args[1]);

        TextView ok = (TextView) view.findViewById(R.id.leftBtn);
        ok.setText(args[2]);
        ok.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                dialogHandler.handleLeft();
            }
        });
        TextView cancel = (TextView) view.findViewById(R.id.rightBtn);
        cancel.setText(args[3]);
        cancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                dialogHandler.handleRight();
            }
        });

        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager m = mActivity.getWindowManager();
        Display d = m.getDefaultDisplay();
        dialog.getWindow().setLayout(d.getWidth() * 6 / 7,
                LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setWindowAnimations(R.style.SpDialogWindowAnimFade);
        dialog.show();
    }

    public void showDialog(String title, String msg, final DialogHandler dialogHandler) {
        showDialog(new String[]{title, msg, mActivity.getString(R.string.sp_confirm), mActivity.getString(R.string.sp_cancel)}, dialogHandler);
    }




}