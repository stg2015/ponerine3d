package com.sp.video.yi.data.model;

/**
 * Created by Administrator on 2016/5/1.
 */
public enum MsgIdEnum {
    GET_TOKEN(257),

    DEFAULT(0);

    private final int id;

    MsgIdEnum(int i) {
        this.id = i;
    }

    public int getId() {
        return id;
    }

    public static MsgIdEnum valueOf(int i){
       for(MsgIdEnum msgIdEnum:MsgIdEnum.values()){
           if(msgIdEnum.getId() == i){
               return msgIdEnum;
           }
       }
        return DEFAULT;
    }
}
