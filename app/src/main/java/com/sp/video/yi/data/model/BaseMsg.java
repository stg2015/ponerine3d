package com.sp.video.yi.data.model;

import android.opengl.Visibility;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.nd.hy.android.commons.data.ObjectMapperUtils;

/**
 * Created by Administrator on 2016/5/1.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseMsg {
    @JsonProperty("msg_id")
    public Integer msgId;
    @JsonProperty("rval")
    public Integer rval;
    @JsonProperty("param")
    public Integer param;



    public MsgIdEnum getMsg(){
        return MsgIdEnum.valueOf(msgId);
    }
    public boolean isError(){
        return rval != null && rval != 0;
    }
    public Integer getMsgId() {
        return msgId;
    }

    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
    }

    public Integer getRval() {
        return rval;
    }

    public void setRval(Integer rval) {
        this.rval = rval;
    }

    public Integer getParam() {
        return param;
    }

    public void setParam(Integer param) {
        this.param = param;
    }

    @Override
    public String toString() {
            String msg ="";
        try {
            msg = ObjectMapperUtils.getMapperInstance().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Log.d("wwc","write and flush msg = "+msg);
        return msg;
    }
}
