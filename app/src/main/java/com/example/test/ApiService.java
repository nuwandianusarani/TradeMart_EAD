package com.example.test;

import com.example.test.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("api/Auth/users")
    Call<List<User>> getUsers();
}
