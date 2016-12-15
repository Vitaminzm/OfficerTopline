package com.education.officertopline.utils;

import com.education.officertopline.entity.ToplineNewsListInfo;

import java.util.Comparator;

/**
 * Created by symbol on 2016/12/15.
 */
public class NewsInfoComparator implements Comparator<ToplineNewsListInfo> {
    @Override
    public int compare(ToplineNewsListInfo lhs, ToplineNewsListInfo rhs) {
        return rhs.getId().compareTo(lhs.getId());
    }
}
