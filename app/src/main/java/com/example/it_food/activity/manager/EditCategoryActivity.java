package com.example.it_food.activity.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.it_food.InterFace.APIService;
import com.example.it_food.R;
import com.example.it_food.activity.AddAddressActivity;
import com.example.it_food.activity.AddressActivity;
import com.example.it_food.helper.SharedPreferences;
import com.example.it_food.model.User;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCategoryActivity extends AppCompatActivity {
    EditText edtIdCategory, edtNameCategory;
    ImageView imgCategoryEdit;
    AppCompatButton btnEdit;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);
        Intent intent = getIntent();
        String categoryId = intent.getStringExtra("categoryId");
        String name = intent.getStringExtra("name");
        String image = intent.getStringExtra("image");

        edtIdCategory = findViewById(R.id.edtIdCategory);
        edtNameCategory = findViewById(R.id.edtNameCategory);
        imgCategoryEdit = findViewById(R.id.imgCategoryEdit);
        btnEdit = findViewById(R.id.btnEdit);
        edtIdCategory.setText(categoryId);
        edtNameCategory.setText(name);
        Glide.with(getApplicationContext()).load(image).into(imgCategoryEdit);

        user = SharedPreferences.getInstance(this).getUser();
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCategory(categoryId, image);
            }
        });
    }
    private void updateCategory(String categoryId, String image)
    {
        Map<String, Object> body = new HashMap<>();
        body.put("userId", user.getId());
        body.put("categoryId", categoryId);
        body.put("name", edtNameCategory.getText().toString());
        body.put("image", image);
        APIService.apiService.updateCategory(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Intent intent = new Intent(EditCategoryActivity.this, ManageCategoryActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Log.d("TAG", "Response Error");
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("TAG", "On Failure");
            }
        });
    }
}