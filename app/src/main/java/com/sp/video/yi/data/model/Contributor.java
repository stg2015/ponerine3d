package com.sp.video.yi.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Weichao Wang on 2016/4/11.
 */
public class Contributor {
    @JsonProperty("login")
    public String login;

    @JsonProperty("contributions")
    public long contributions;

    public String getLogin() {
        return login;
    }

    public long getContributions() {
        return contributions;
    }
}
