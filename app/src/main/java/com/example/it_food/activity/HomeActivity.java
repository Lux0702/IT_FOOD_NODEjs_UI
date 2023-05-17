package com.example.it_food.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
<<<<<<< HEAD
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
=======
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
>>>>>>> 8e03d11804a052dbf3fd8f9aa5b1c303a2282ada

import com.example.it_food.Adapter.BestSellerAdapter;
import com.example.it_food.Adapter.CategoryAdapter;
import com.example.it_food.InterFace.APIService;
import com.example.it_food.R;
import com.example.it_food.Adapter.BannerAdapter;
import com.example.it_food.helper.SharedPreferences;
import com.example.it_food.model.Category;
import com.example.it_food.model.ProductItem;
import com.example.it_food.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private BannerAdapter bannerAdapter;
    private List<Category> mListCategory;
    private List<ProductItem> mListProduct;
    RecyclerView recyclerViewCategories, recyclerViewBestSeller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
=======
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
>>>>>>> 8e03d11804a052dbf3fd8f9aa5b1c303a2282ada
        setContentView(R.layout.activity_home);
        ViewPager viewPager = findViewById(R.id.viewPager);

        bannerAdapter = new BannerAdapter(this, viewPager);
        viewPager.setAdapter(bannerAdapter);
        bannerAdapter.startAutoScroll(3000);

        recyclerViewCategories = findViewById(R.id.recyclerViewCategories);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategories.setLayoutManager(linearLayoutManager);
        mListCategory = new ArrayList<>();
        callApiGetCategory();

        recyclerViewBestSeller = findViewById(R.id.recyclerViewBestSelling);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewBestSeller.setLayoutManager(linearLayoutManager1);
        mListProduct = new ArrayList<>();
        callApiGetBestSellerProduct();

        bottomNavigation();

    }
    private void callApiGetCategory() {
        User user = SharedPreferences.getInstance(this).getUser();
        APIService.apiService.getCategories().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject itemObject = jsonArray.getJSONObject(i);
                            String id = itemObject.getString("_id");
                            String name = itemObject.getString("name");
                            String image = itemObject.getString("image");
                            // Trích xuất các thông tin khác của sản phẩm tại đây
                            Category category = new Category(id, name, image);
                            mListCategory.add(category);
                        }
                        CategoryAdapter categoryAdapter = new CategoryAdapter(mListCategory, HomeActivity.this);
                        recyclerViewCategories.setAdapter(categoryAdapter);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Xử lý khi phản hồi không thành công
                    Toast.makeText(HomeActivity.this, "response not success", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Call api error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void callApiGetBestSellerProduct() {
        User user = SharedPreferences.getInstance(this).getUser();
        APIService.apiService.getBestSellerProducts().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject itemObject = jsonArray.getJSONObject(i);
                            String id = itemObject.getString("_id");
                            String name = itemObject.getString("name");
                            String image = itemObject.getString("image");
                            String desc = itemObject.getString("description");
                            double price = itemObject.getDouble("price");
                            ProductItem productItem = new ProductItem(id, name, desc, price, image);
                            mListProduct.add(productItem);
                        }
                        BestSellerAdapter bestSellerAdapter = new BestSellerAdapter(mListProduct, HomeActivity.this);
                        recyclerViewBestSeller.setAdapter(bestSellerAdapter);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Xử lý khi phản hồi không thành công
                    Toast.makeText(HomeActivity.this, "response not success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Call api error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void bottomNavigation() {
        FloatingActionButton btnCart= findViewById(R.id.btnCart);
        ImageView imbHome = findViewById(R.id.imbHome);
        ImageView imbFavorite = findViewById(R.id.imbFavorite);
        ImageView imbChat = findViewById(R.id.imbChat);
        ImageView imbProfile = findViewById(R.id.imbProfile);
        imbHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        imbProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });


    }
}