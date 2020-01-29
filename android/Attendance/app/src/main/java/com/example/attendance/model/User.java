package com.example.attendance.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("sucess")
    private String success;
    @SerializedName("_id")
    private String id;
    @SerializedName("email")
    private String email;
    @SerializedName("name")
    private String name;
    @SerializedName("department")
    private String dept;
    @SerializedName("password")
    private String password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDept() {
        return dept;
    }

    public String getSuccess() {
        return success;
    }
}
