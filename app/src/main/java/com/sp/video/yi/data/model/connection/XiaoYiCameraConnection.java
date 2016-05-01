package com.sp.video.yi.data.model.connection;

import android.util.Log;

import com.nd.hy.android.commons.data.ObjectMapperUtils;
import com.sp.video.yi.data.model.BaseMsg;
import com.sp.video.yi.data.model.GetTokenMsg;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/29.
 */
public class XiaoYiCameraConnection implements Serializable, IActionMsg, IMsgResponseHandler {
    private final String ipAddress;
    private final int port;
    private final String id;

    private int token;

    public XiaoYiCameraConnection(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.id = ipAddress + ":" + port;

    }

    public String getId() {
        return id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    @Override
    public BaseMsg getAccessTokenMsg() {
        return new GetTokenMsg();
    }


    //IMsgResponseHandler
    @Override
    public void onMsgResponse(String response) throws RuntimeException {
        BaseMsg responsseEntry = null;
        try {
            responsseEntry = ObjectMapperUtils.getMapperInstance().readValue(response, BaseMsg.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (responsseEntry == null || responsseEntry.isError()) {
            String errorMsg =responsseEntry == null?"响应为空":responsseEntry.getRval()+"";
            throw new RuntimeException("发生错误:"+errorMsg);
        }else{
            parseMsg(responsseEntry);
        }
    }

    private void parseMsg(BaseMsg responsseEntry) {
        switch (responsseEntry.getMsg()){
            case GET_TOKEN:
                this.token = responsseEntry.getParam();
                Log.d("wwc","【Get token】 = "+token);
                break;
        }
    }
}
