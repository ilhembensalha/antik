package com.example.antik;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    @POST("updateProfileImage/{id}")
    Call<ResponseBody> updateProfileImage(@Path("id") int userId, @Part MultipartBody.Part avatar);

    @GET
    Call<ResponseBody> getUserImage(@Url String url);

    @Multipart
    @POST("annonces")
    Call<ResponseBody> addAnnouncement(
            @Part("titre") RequestBody titre,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part image,
            @Part("prix") RequestBody prix,
            @Part("livraison") RequestBody livraison,
            @Part("location") RequestBody location,

            @Part("cat_id") RequestBody cat_id,
            @Part("user_id") RequestBody user_id
    );
    // Define the endpoint for getting the list of categories
    @GET("categories")
    Call<List<Categorie>> getCategories();

    @GET("annonces")
    Call<ApiService.ApiResponse> getAnnonces();
    class ApiResponse {
        int status;
        List<Annonce.Annoncee> annonces;

        public int getStatus() {
            return status;
        }

        public List<Annonce.Annoncee> getAnnonces() {
            return annonces;
        }
    }
}