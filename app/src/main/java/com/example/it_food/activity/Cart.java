package com.example.it_food.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.it_food.R;
import com.example.it_food.activity.api.ApiService;
import com.example.it_food.activity.model.User;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cart extends AppCompatActivity {

    public TextView textProduct;
    public Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        textProduct = findViewById(R.id.textProduct);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(Cart.this, "yess", Toast.LENGTH_SHORT).show();
                clickGetPro();
            }
        });
    }
    private void clickGetPro() {
        User user = new User("6454bd3cf82eec776eb7d842");

        ApiService.apiService.getProInCart(user).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Cart.this, "success", Toast.LENGTH_SHORT).show();
                    /*try {
                        String json = response.body().string();
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        textProduct.setText(jsonArray.toString());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject itemObject = jsonArray.getJSONObject(i);
                            String itemName = itemObject.getString("name");
                            double itemPrice = itemObject.getDouble("price");
                            // Trích xuất các thông tin khác của sản phẩm tại đây
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                } else {
                    // Xử lý khi phản hồi không thành công
                    Toast.makeText(Cart.this, "response error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Cart.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}