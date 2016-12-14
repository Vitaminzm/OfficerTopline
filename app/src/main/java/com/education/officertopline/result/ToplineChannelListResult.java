package com.education.officertopline.result;

import com.education.officertopline.entity.ToplineChannelInfo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ToplineChannelListResult extends BaseResult{

    @SerializedName("data")
    private List<ToplineChannelInfo> list;

    public List<ToplineChannelInfo> getList() {
        return list;
    }

    public void setList(List<ToplineChannelInfo> list) {
        this.list = list;
    }
}
