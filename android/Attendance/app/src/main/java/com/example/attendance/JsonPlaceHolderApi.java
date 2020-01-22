package com.example.attendance;

import com.example.attendance.model.Post;
import com.example.attendance.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {

    @GET("users")
    Call<List<User>> getUsers();

}
