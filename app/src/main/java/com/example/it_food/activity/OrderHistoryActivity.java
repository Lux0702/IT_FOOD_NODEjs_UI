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

import com.example.it_food.Adapter.AddressItemAdapter;
import com.example.it_food.Adapter.OrderHistoryAdapter;
import com.example.it_food.InterFace.APIService;
import com.example.it_food.R;
import com.example.it_food.helper.SharedPreferences;
import com.example.it_food.model.Address;
import com.example.it_food.model.OrderHistory;
import com.example.it_food.model.User;

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

public class OrderHistoryActivity extends AppCompatActivity implements OrderHistoryAdapter.OnButtonClickListener{

    private RecyclerView rycOrderItem;
    private List<OrderHistory> mListOrder;
    private OrderHistoryAdapter orderHistoryAdapter;

    private ImageView imageArrowleft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order_history);

        imageArrowleft = findViewById(R.id.imageArrowleft);
        rycOrderItem = findViewById(R.id.rycOrderItem);
        mListOrder = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rycOrderItem.setLayoutManager(linearLayoutManager);

        imageArrowleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderHistoryActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
        callApiOrderHistory();
    }

    @Override
    public void onButtonClicked(int position) {
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra("orderId", mListOrder.get(position).get_id());
        startActivity(intent);
        finish();
    }

    private void callApiOrderHistory() {
        User user = SharedPreferences.getInstance(this).getUser();
        APIService.apiService.getOrderHistory(user.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray jsonArray = jsonObject.getJSONArray("results");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject itemObject = jsonArray.getJSONObject(i);
                            String id = itemObject.getString("_id");
                            String itemAddress = itemObject.getString("address");
                            String phoneNumber = itemObject.getString("phoneNumber");
                            String delivery = itemObject.getString("delivery");
                            String status = itemObject.getString("status");
                            String totalPrice = itemObject.getString("totalPrice");
                            // Trích xuất các thông tin khác của sản phẩm tại đây
                            OrderHistory orderHistory = new OrderHistory(id, itemAddress, phoneNumber, delivery, status, totalPrice);
                            mListOrder.add(orderHistory);
                        }
                        orderHistoryAdapter = new OrderHistoryAdapter(mListOrder, OrderHistoryActivity.this, OrderHistoryActivity.this);
                        rycOrderItem.setAdapter(orderHistoryAdapter);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Xử lý khi phản hồi không thành công
                    Toast.makeText(OrderHistoryActivity.this, "response not success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(OrderHistoryActivity.this, "Call api error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}