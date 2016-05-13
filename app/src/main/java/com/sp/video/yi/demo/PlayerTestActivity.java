package com.sp.video.yi.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sp.vlc_player.PlayerView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayerTestActivity extends AppCompatActivity implements View.OnClickListener, PlayerView.OnChangeListener {

    @Bind(R.id.toolbar)
    Toolbar    toolbar;
    @Bind(R.id.tv_ip_address1)
    EditText   tvIpAddress1;
    @Bind(R.id.tv_ip_address2)
    EditText   tvIpAddress2;
    @Bind(R.id.btn_start1)
    Button     btnStart1;
    @Bind(R.id.btn_start2)
    Button     btnStart2;
    @Bind(R.id.pv_video1)
    PlayerView mPlayerView1;
    @Bind(R.id.pv_video2)
    PlayerView mPlayerView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_test);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

    }
    private static final String RTSP_URL_FORMATER = "rtsp://%1$s/live";
    private String mRTSPUrl1 = "";
    private String mRTSPUrl2 = "";

    @Override
    protected void onResume() {
        super.onResume();
        mRTSPUrl1 = getRTSPUrl(1);
        mRTSPUrl2 = getRTSPUrl(2);
        //使用步骤
        //第一步 ：通过findViewById或者new PlayerView()得到mPlayerView1对象
        //mPlayerView1= new PlayerView(PlayerActivity.this);

        //第二步：设置参数，毫秒为单位
        mPlayerView1.setNetWorkCache(1000);
        mPlayerView2.setNetWorkCache(1000);

        //第三步:初始化播放器
        mPlayerView1.initPlayer(mRTSPUrl1);
        mPlayerView2.initPlayer(mRTSPUrl2);

        //第四步:设置事件监听，监听缓冲进度等
        mPlayerView1.setOnChangeListener(this);
        mPlayerView2.setOnChangeListener(this);

        //第五步：开始播放
        mPlayerView1.start();
        mPlayerView2.start();
    }

    private String getRTSPUrl(int index) {
        String formatUrl;
        if (index == 1)
            formatUrl =  String.format(RTSP_URL_FORMATER,tvIpAddress1.getText().toString().trim());
        else
            formatUrl =  String.format(RTSP_URL_FORMATER,tvIpAddress2.getText().toString().trim());
        return  formatUrl;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.toolbar, R.id.tv_ip_address1, R.id.btn_start1, R.id.pv_video1, R.id.tv_ip_address2, R.id.btn_start2, R.id.pv_video2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                break;
            case R.id.tv_ip_address1:
                break;
            case R.id.tv_ip_address2:
                break;
            case R.id.btn_start1:
                mPlayerView1.pause();
                mRTSPUrl1 = getRTSPUrl(1);
                mPlayerView1.initPlayer(mRTSPUrl1);
                mPlayerView1.start();
                break;
            case R.id.btn_start2:
                mPlayerView2.pause();
                mRTSPUrl2 = getRTSPUrl(2);
                mPlayerView2.initPlayer(mRTSPUrl2);
                mPlayerView2.start();
                break;
            case R.id.pv_video1:
                break;
            case R.id.pv_video2:
                break;
        }
    }

    @Override
    public void onBufferChanged(float buffer) {
        Log.d("wwc","onBufferChanged:"+buffer);
    }

    @Override
    public void onLoadComplet() {
        Log.d("wwc","onLoadComplet");
    }

    @Override
    public void onError() {
        Log.d("wwc","onError");
    }

    @Override
    public void onEnd() {
        Log.d("wwc","onEnd");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayerView1.stop();
        mPlayerView2.stop();
    }
}
