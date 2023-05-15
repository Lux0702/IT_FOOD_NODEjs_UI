package com.example.it_food.activity.api;

import com.example.it_food.activity.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://it4food.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    ApiService apiService = retrofit.create(ApiService.class);

    @GET("cart-items/")
    Call<ResponseBody> getProInCart(@Body User user);
}
