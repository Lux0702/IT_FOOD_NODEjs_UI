package com.example.it_food.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.it_food.Adapter.ProductItemAdapter;
import com.example.it_food.InterFace.APIService;
import com.example.it_food.R;
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


public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<ProductItem> mListProduct;
    private AppCompatButton confirmOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cart);

        confirmOrder = findViewById(R.id.btnConfirmOrder);

        recyclerView = findViewById(R.id.recyclerListellipsetwenty);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        mListProduct = new ArrayList<>();
        callApiGetProductInCart();

        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this, ConfirmOrderActivity.class);
                startActivity(intent);
            }
        });
    }

    private void callApiGetProductInCart() {
        APIService.apiService.GetProInCart("6454bd3cf82eec776eb7d842").enqueue(new Callback<ResponseBody>() {
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
                            String itemName = itemObject.getString("name");
                            String description = itemObject.getString("description");
                            double itemPrice = itemObject.getDouble("price");
                            int quantity = itemObject.getInt("quantity");
                            String image = itemObject.getString("image");
                            // Trích xuất các thông tin khác của sản phẩm tại đây
                            ProductItem productItem = new ProductItem(id, description, itemName, itemPrice, image, quantity);
                            mListProduct.add(productItem);
                        }
                        ProductItemAdapter productItemAdapter = new ProductItemAdapter(mListProduct);
                        recyclerView.setAdapter(productItemAdapter);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Xử lý khi phản hồi không thành công
                    Toast.makeText(CartActivity.this, "response not success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Call api error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}