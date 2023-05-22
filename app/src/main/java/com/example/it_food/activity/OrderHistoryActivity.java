package com.example.it_food.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView rycOrderItem;
    private List<OrderHistory> mListOrder;
    private OrderHistoryAdapter orderHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        rycOrderItem = findViewById(R.id.rycOrderItem);
        mListOrder = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rycOrderItem.setLayoutManager(linearLayoutManager);
        callApiOrderHistory();
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
                        orderHistoryAdapter = new OrderHistoryAdapter(mListOrder, OrderHistoryActivity.this);
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