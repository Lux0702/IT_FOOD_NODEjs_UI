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

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity {
    EditText edtCategory, edtNameProduct, edtPrice, edtDesc;
    AppCompatButton btnAdd;
    User user;
    ImageView imgProductEdit;
    String categoryId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Intent intent = getIntent();
        categoryId = intent.getStringExtra("categoryId");
        edtCategory = findViewById(R.id.edtCategory);
        edtNameProduct = findViewById(R.id.edtNameProduct);
        edtPrice = findViewById(R.id.edtPrice);
        edtDesc = findViewById(R.id.edtDescript);
        btnAdd = findViewById(R.id.btnAdd);
        user = SharedPreferences.getInstance(this).getUser();
        imgProductEdit = findViewById(R.id.imgProductEdit);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProduct();
            }
        });
    }
    private void addProduct()
    {
        Map<String, Object> body = new HashMap<>();
        body.put("userId", user.getId()) ;
        body.put("name", edtNameProduct.getText().toString());
        body.put("description", edtDesc.getText().toString());
        body.put("price", Float.parseFloat(edtPrice.getText().toString()));
        body.put("quantity", 10);
        body.put("categoryId", categoryId);
        body.put("image", "image");
        APIService.apiService.addProduct(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Intent intent = new Intent(AddProductActivity.this, ManageCategoryActivity.class);
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