package com.education.officertopline.entity;

import java.io.Serializable;

/**
 * Created by symbol on 2016/12/13.
 */
public class ToplineChannelInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String channelCode;
    private String channelName;

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public boolean equals(Object obj) {
        ToplineChannelInfo tmp = (ToplineChannelInfo)obj;
        return this.channelName.equals(tmp.channelName);
    }

    @Override
    public int hashCode() {
        return this.channelCode.hashCode();
    }
}
