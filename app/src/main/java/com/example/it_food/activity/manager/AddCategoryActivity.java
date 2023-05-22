package com.example.it_food.activity.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.it_food.InterFace.APIService;
import com.example.it_food.R;
import com.example.it_food.helper.SharedPreferences;
import com.example.it_food.model.User;

import org.checkerframework.checker.units.qual.C;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCategoryActivity extends AppCompatActivity {
    EditText edtNameCategory;
    AppCompatButton btnAdd;
    User user;
    ImageView imgCategoryEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        edtNameCategory = findViewById(R.id.edtNameCategory);
        imgCategoryEdit = findViewById(R.id.imgCategoryEdit);
        user = SharedPreferences.getInstance(this).getUser();
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCategory();
            }
        });
    }
    private void addCategory()
    {
        Map<String, Object> body = new HashMap<>();
        body.put("name", edtNameCategory.getText().toString());
        body.put("image", "image");
        body.put("userId", user.getId());
        APIService.apiService.addCategory(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Intent intent = new Intent(AddCategoryActivity.this, ManageCategoryActivity.class);
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