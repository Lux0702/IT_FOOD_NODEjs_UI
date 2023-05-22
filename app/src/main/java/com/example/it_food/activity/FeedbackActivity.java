package com.example.it_food.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.it_food.Adapter.OrderHistoryAdapter;
import com.example.it_food.InterFace.APIService;
import com.example.it_food.R;
import com.example.it_food.helper.SharedPreferences;
import com.example.it_food.model.OrderHistory;
import com.example.it_food.model.User;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackActivity extends AppCompatActivity {
    private String productId, imageUrl;
    AppCompatButton btnSubmit;
    EditText etLeaveFeedback;
    ImageView imgProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_feedback);

        btnSubmit = findViewById(R.id.btnSubmit);
        imgProduct = findViewById(R.id.imgProductComment);
        etLeaveFeedback = findViewById(R.id.etLeaveFeedback);

        Intent intent = getIntent();
        productId = intent.getStringExtra("product");
        imageUrl = intent.getStringExtra("image");
        bindData(imageUrl);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(etLeaveFeedback.getText().toString())){
                    etLeaveFeedback.setError("Please enter feedback");
                    etLeaveFeedback.requestFocus();
                    return;
                }else {
                    sendFeedback();
                }
            }
        });
    }

    public void bindData(String imageUrl) {
        Picasso.get().load(imageUrl).into(imgProduct);
    }

    private void sendFeedback() {

        User user = SharedPreferences.getInstance(this).getUser();

        Map body = new HashMap<>();
        body.put("userId", user.getId());
        body.put("product", productId);
        body.put("comment", etLeaveFeedback.getText().toString());
        APIService.apiService.getOrderHistory(user.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(FeedbackActivity.this, "Comment success", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(FeedbackActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Xử lý khi phản hồi không thành công
                    Toast.makeText(FeedbackActivity.this, "response not success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(FeedbackActivity.this, "Call api error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}