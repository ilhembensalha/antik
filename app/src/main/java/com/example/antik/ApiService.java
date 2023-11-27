package com.example.antik;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    @FormUrlEncoded
    @POST("register") // Adjust the endpoint as needed
    Call<ApiResponse> registerUser(@FieldMap Map<String, String> parameters);
}