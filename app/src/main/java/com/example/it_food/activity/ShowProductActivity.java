package com.example.it_food.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.it_food.Adapter.CategoryAdapter;
import com.example.it_food.Adapter.ProductAdapter;
import com.example.it_food.InterFace.APIService;
import com.example.it_food.R;
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

public class ShowProductActivity extends AppCompatActivity {
    private ArrayList<ProductItem> mListProduct;
    private TextView txtTitle;
    private RecyclerView recyclerViewShowProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String idcategory = intent.getStringExtra("idcategory");

        recyclerViewShowProduct = findViewById(R.id.recyclerViewShowProduct);
        recyclerViewShowProduct.setLayoutManager(new GridLayoutManager(this, 3));
       /* LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewShowProduct.setLayoutManager(linearLayoutManager1);*/
        mListProduct = new ArrayList<>();
        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText(title);
        mListProduct = new ArrayList<>();
        callApiGetProductList(idcategory);
        //bottomNavigation();
    }
    private void callApiGetProductList(String idcategogy) {
        User user = SharedPreferences.getInstance(this).getUser();
        APIService.apiService.getProductList(idcategogy).enqueue(new Callback<ResponseBody>() {
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
                        ProductAdapter productAdapter = new ProductAdapter(mListProduct, ShowProductActivity.this);
                        recyclerViewShowProduct.setAdapter(productAdapter);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Xử lý khi phản hồi không thành công
                    Toast.makeText(ShowProductActivity.this, "response not success", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ShowProductActivity.this, "Call api error", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(ShowProductActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        imbProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowProductActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowProductActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
    }
}