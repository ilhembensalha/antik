package com.example.antik;

import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    @POST("register") // Adjust the endpoint as needed
    Call<JsonObject> registerUser(@Body Map<String, String> parameters);

    @POST("login")
    Call<JsonObject> loginUser(@Body Map<String, String> parameters);// Adjust the endpoint as needed

}