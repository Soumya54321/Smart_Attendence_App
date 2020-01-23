package com.example.attendance;

import com.example.attendance.model.User;


import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {

    @POST("login")
    Call<User> login(@Body User user);

    @Multipart
    @POST("/upload")
    Call<ResponseBody> upload(@Part MultipartBody.Part file);
}
