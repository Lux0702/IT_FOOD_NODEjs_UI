package com.example.it_food.InterFace;

import com.example.it_food.model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;

public interface ApiService {


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://it4food.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    ApiService apiService = retrofit.create(ApiService.class);

    @GET("cart-items/")
    Call<ResponseBody> getProInCart(@Body User user);
}
