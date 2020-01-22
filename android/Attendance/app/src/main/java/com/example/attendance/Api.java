package com.example.attendance;

import com.example.attendance.model.User;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Api {

    @POST("login")
    Call<User> login(@Body User user);
}
