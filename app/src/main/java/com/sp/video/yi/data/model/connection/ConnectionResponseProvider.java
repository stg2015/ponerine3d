package com.sp.video.yi.data.model.connection;

import android.util.Log;

import com.sp.video.yi.data.model.connection.XiaoYiCameraConnection;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/5/1.
 */
public enum ConnectionResponseProvider {
    INSTANCE;

    public static HashMap<String,XiaoYiCameraConnection> cacheConnections = new HashMap<>();

    public void init(){
        cacheConnections.clear();
    }

    public void deleteConnection(XiaoYiCameraConnection xiaoYiCameraConnection){
        if(cacheConnections.containsKey(xiaoYiCameraConnection.getId())){
            cacheConnections.remove(xiaoYiCameraConnection.getId());
        }
    }

    public void cacheConnection(XiaoYiCameraConnection xiaoYiCameraConnection){
        cacheConnections.put(xiaoYiCameraConnection.getId(), xiaoYiCameraConnection);
    }



    public void onMsgResponse(String connectionId, String msg) {
        if(cacheConnections.containsKey(connectionId)){
            cacheConnections.get(connectionId).onMsgResponse(msg);
        }else{
            Log.d("wwc","接收到未缓存连接");
        }
    }

    public boolean alreadyHasConnection(XiaoYiCameraConnection connection) {
        boolean alreadyHasConnection = ConnectionResponseProvider.cacheConnections.containsKey(connection.getId());
        return alreadyHasConnection;
    }
}
