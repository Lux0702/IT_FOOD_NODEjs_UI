package com.example.it_food.activity.manager;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.it_food.Adapter.EditCategoryAdapter;
import com.example.it_food.InterFace.APIService;
import com.example.it_food.R;
import com.example.it_food.activity.EditProfileActivity;
import com.example.it_food.model.Category;
import com.example.it_food.model.ProductItem;

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

public class ManageCategoryActivity extends AppCompatActivity {
    Button btnAdd;
    TextView textViewCategory;
    private List<Category> mListCategory;
    private List<ProductItem> mListProduct;
    RecyclerView recyclerViewCategories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_category);
        btnAdd = findViewById(R.id.buttonAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageCategoryActivity.this, AddCategoryActivity.class);
                startActivity(intent);
            }
        });

        recyclerViewCategories = findViewById(R.id.recyclerViewCategories);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewCategories.setLayoutManager(linearLayoutManager);
        mListCategory = new ArrayList<>();
        callApiGetCategory();
    }

    private void callApiGetCategory() {
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
                        EditCategoryAdapter editCategoryAdapter= new EditCategoryAdapter(mListCategory, ManageCategoryActivity.this);
                        recyclerViewCategories.setAdapter(editCategoryAdapter);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Xử lý khi phản hồi không thành công
                    Toast.makeText(ManageCategoryActivity.this, "response not success", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ManageCategoryActivity.this, "Call api error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}