package com.sp.video.yi.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Administrator on 2016/5/1.
 */
public class GetTokenMsg extends BaseMsg {
    @JsonProperty("token")
    public Integer token;
    @JsonProperty("heartbeat")
    public Integer heartbeat;

    public GetTokenMsg() {
        this.msgId = MsgIdEnum.GET_TOKEN.getId();
        this.token = 0;
        this.heartbeat = 1;
        this.param = 0;
    }
}
