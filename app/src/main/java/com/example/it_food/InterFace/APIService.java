package com.example.it_food.InterFace;

import com.example.it_food.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    static final String BASE_URL = "https://it4food.onrender.com/";

    Gson gson= new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();
    APIService apiService =new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(APIService.class);

    @POST("users/login")
    Call<User> login(@Body User user);

    @POST("users/register")
    Call<User> register(@Body User user);

    @GET("cart-items")
    Call<ResponseBody> GetProInCart(@Query("userId") String id);

    @PATCH("cart-items/update")
    Call<ResponseBody> updateProductInCart(@Body Map<String, Object> body);

    @POST("users/upload-file")
    Call<ResponseBody> uploadImage(
            @Part("id") RequestBody id,
            @Part MultipartBody.Part image
    );

}
