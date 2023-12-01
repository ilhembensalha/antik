package com.example.antik;

import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface ApiService {
    @POST("register") // Adjust the endpoint as needed
    Call<JsonObject> registerUser(@Body Map<String, String> parameters);

    @POST("login")
    Call<JsonObject> loginUser(@Body Map<String, String> parameters);// Adjust the endpoint as needed


    @PUT("editProfile/{id}") // Adjust the endpoint as need
    Call<JsonObject> editProfile(@Path("id") int userId, @Body User user);


    @Multipart
    @PUT("updateProfileImage/{id}")
    Call<ResponseBody> updateProfileImage(@Path("id") int userId, @Part MultipartBody.Part avatar);

    @GET
    Call<ResponseBody> getUserImage(@Url String url);

}