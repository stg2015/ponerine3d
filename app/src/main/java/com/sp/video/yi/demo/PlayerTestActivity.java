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
    @Bind(R.id.tv_ip_address)
    EditText   tvIpAddress;
    @Bind(R.id.btn_start)
    Button     btnStart;
    @Bind(R.id.pv_video)
    PlayerView mPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_test);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

    }
    private static final String RTSP_URL_FORMATER = "rtsp://%1$s:554/live";
    private String mRTSPUrl = "";

    @Override
    protected void onResume() {
        super.onResume();
        mRTSPUrl = getRTSPUrl();
        //使用步骤
        //第一步 ：通过findViewById或者new PlayerView()得到mPlayerView对象
        //mPlayerView= new PlayerView(PlayerActivity.this);

        //第二步：设置参数，毫秒为单位
        mPlayerView.setNetWorkCache(1000);

        //第三步:初始化播放器
        mPlayerView.initPlayer(mRTSPUrl);

        //第四步:设置事件监听，监听缓冲进度等
        mPlayerView.setOnChangeListener(this);

        //第五步：开始播放
        mPlayerView.start();
    }

    private String getRTSPUrl() {
        String formatUrl = "";
        formatUrl =  String.format(RTSP_URL_FORMATER,tvIpAddress.getText().toString().trim());
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

    @OnClick({R.id.toolbar, R.id.tv_ip_address, R.id.btn_start, R.id.pv_video})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                break;
            case R.id.tv_ip_address:
                break;
            case R.id.btn_start:
                mPlayerView.pause();
                mRTSPUrl = getRTSPUrl();
                mPlayerView.initPlayer(mRTSPUrl);
                mPlayerView.start();
                break;
            case R.id.pv_video:
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
        mPlayerView.stop();
    }
}
