package com.example.it_food.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.it_food.Adapter.OrderDetailAdapter;
import com.example.it_food.Adapter.OrderHistoryAdapter;
import com.example.it_food.InterFace.APIService;
import com.example.it_food.R;
import com.example.it_food.helper.SharedPreferences;
import com.example.it_food.model.OrderHistory;
import com.example.it_food.model.ProductItem;
import com.example.it_food.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity implements OrderDetailAdapter.OnButtonClickListener{
    private RecyclerView rycOrderDetailItem;
    private List<ProductItem> mListProduct;
    private OrderDetailAdapter orderDetailAdapter;
    private String orderId;
    private ImageView imageArrowleft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order_detail);

        imageArrowleft = findViewById(R.id.imageArrowleft);
        rycOrderDetailItem = findViewById(R.id.rycOrderDetailItem);
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rycOrderDetailItem.setLayoutManager(linearLayoutManager);

        imageArrowleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(OrderDetailActivity.this, OrderHistoryActivity.class);
                startActivity(intent1);
                finish();
            }
        });
        mListProduct = new ArrayList<>();
        callApiOrderDetail();
    }

    @Override
    public void onButtonClicked(int position) {
        Intent intent = new Intent(this, FeedbackActivity.class);
        intent.putExtra("product", mListProduct.get(position).get_id());
        intent.putExtra("image", mListProduct.get(position).getImage());
        startActivity(intent);
        finish();
    }

    private void callApiOrderDetail() {
        User user = SharedPreferences.getInstance(this).getUser();
        Map body = new HashMap<>();
        body.put("userId", user.getId());
        body.put("orderId", orderId);
        APIService.apiService.getProductByOrderId(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray jsonArray = jsonObject.getJSONArray("results");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject itemObject = jsonArray.getJSONObject(i);
                            String name = itemObject.getString("name");
                            String description = itemObject.getString("description");
                            String image = itemObject.getString("image");
                            Double price = itemObject.getDouble("price");
                            int quantity = itemObject.getInt("quantity");
                            // Trích xuất các thông tin khác của sản phẩm tại đây
                            ProductItem productItem = new ProductItem(name, description, price, image, quantity);
                            mListProduct.add(productItem);
                        }
                        orderDetailAdapter = new OrderDetailAdapter(mListProduct, OrderDetailActivity.this, OrderDetailActivity.this);
                        rycOrderDetailItem.setAdapter(orderDetailAdapter);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Xử lý khi phản hồi không thành công
                    Toast.makeText(OrderDetailActivity.this, "response not success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(OrderDetailActivity.this, "Call api error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}