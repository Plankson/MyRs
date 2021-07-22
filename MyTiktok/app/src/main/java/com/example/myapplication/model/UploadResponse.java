package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

public class UploadResponse {
    @SerializedName("result")
    public data message;
    @SerializedName("success")
    public boolean success;

    @SerializedName("error")
    public String error;
}
