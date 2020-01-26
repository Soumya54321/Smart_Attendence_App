package com.example.attendance.model;

import com.google.gson.annotations.SerializedName;

public class Result {
    @SerializedName("success")
    private String success;

    public String getSuccess() {
        return success;
    }
}
