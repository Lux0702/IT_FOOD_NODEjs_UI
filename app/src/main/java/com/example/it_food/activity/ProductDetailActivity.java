package com.example.it_food.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.it_food.InterFace.APIService;
import com.example.it_food.R;
import com.example.it_food.helper.SharedPreferences;
import com.example.it_food.model.ProductItem;
import com.example.it_food.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.reactivex.rxjava3.annotations.NonNull;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView imgFood;
    private TextView txtFoodName, txtDescription, txtFoodPrice;
    private EditText edtQuantity;
    private Button btnAddToCart;
    private ImageButton btnFavorite, btnDecrease, btnIncrease;
    private Context mContext;
    private String productId;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_product_detail);
        imgFood = findViewById(R.id.imgFood);
        txtFoodName = findViewById(R.id.txtFoodName);
        txtDescription = findViewById(R.id.txtDescription);
        txtFoodPrice = findViewById(R.id.txtFoodPrice);
        edtQuantity = findViewById(R.id.edtQuantity);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnDecrease = findViewById(R.id.btnIncrease);
        btnIncrease = findViewById(R.id.btnDecrease);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        mContext = this;
        user = SharedPreferences.getInstance(this).getUser();

        Intent intent = getIntent();
        String productName = intent.getStringExtra("productName");
        productId = intent.getStringExtra("productId");
        double productPrice = intent.getDoubleExtra("productPrice", 0.0);
        String productDesc = intent.getStringExtra("productDesc");
        String productImg = intent.getStringExtra("productImg");

        Glide.with(mContext)
            .load(productImg)
            .into(new CustomTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    imgFood.setImageDrawable(resource);
                }
                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {
                    // Xử lý khi ảnh bị xóa đi (có thể không cần xử lý)
                }
            });
        txtFoodName.setText(productName);
        txtDescription.setText(productDesc);
        txtFoodPrice.setText(String.valueOf(productPrice));
        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentValue = Integer.parseInt(edtQuantity.getText().toString());
                if (currentValue > 0) {
                    currentValue--;
                }
                edtQuantity.setText(String.valueOf(currentValue));
            }
        });
        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentValue = Integer.parseInt(edtQuantity.getText().toString());
                currentValue++;
                edtQuantity.setText(String.valueOf(currentValue));
            }
        });
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> body = new HashMap<>();
                body.put("userId", user.getId());
                body.put("productId", productId);
                body.put("quantity", Integer.parseInt(edtQuantity.getText().toString()));
                APIService.apiService.addToCart(body).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(ProductDetailActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
                        } else {
                            // Xử lý khi yêu cầu không thành công
                            Toast.makeText(ProductDetailActivity.this, "Failed to add to cart", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // Xử lý khi gặp lỗi trong quá trình gửi yêu cầu
                        Toast.makeText(ProductDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}