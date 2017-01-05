package com.chen.okhttp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by chen on 2017-01-05.
 */

public class NewsBean {
    @Override
    public String toString() {
        return "NewsBean{" +
                "data=" + data +
                '}';
    }

    @SerializedName("T1348647909107")
    public ArrayList<NewsDetail> data;


}
