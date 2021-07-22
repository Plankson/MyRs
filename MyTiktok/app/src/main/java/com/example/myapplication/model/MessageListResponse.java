package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MessageListResponse {
    @SerializedName("feeds")
    public List<data> feeds;
    @SerializedName("success")
    public boolean success;
}
