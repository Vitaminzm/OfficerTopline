package com.education.officertopline.result;

import com.education.officertopline.entity.ToplineNewsListInfo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ToplineNewsListResult extends BaseResult{

    @SerializedName("data")
    private List<ToplineNewsListInfo> list;

    public List<ToplineNewsListInfo> getList() {
        return list;
    }

    public void setList(List<ToplineNewsListInfo> list) {
        this.list = list;
    }
}
