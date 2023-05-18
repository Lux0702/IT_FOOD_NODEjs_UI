package com.example.it_food.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.it_food.InterFace.APIService;
import com.example.it_food.R;
import com.example.it_food.helper.SharedPreferences;
import com.example.it_food.model.Address;
import com.example.it_food.model.Delivery;
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

public class ConfirmOrderActivity extends AppCompatActivity {
    private TextView txtPrice, txtDeliveryPrice, txtTotalPrice, txtAddress;
    private User user;
    private List<Delivery> deliveries;
    private List<Address> mListAddress1;
    private LinearLayout linearLocationset;
    private AppCompatButton btnPlaceMyOrder;
    private int d = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_confirm_order);

        Intent intent = getIntent();
        txtPrice = findViewById(R.id.txtPrice);
        txtDeliveryPrice = findViewById(R.id.txtDeliveryPrice);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        linearLocationset = findViewById(R.id.linearLocationset);
        txtAddress = findViewById(R.id.txtAddress);
        btnPlaceMyOrder = findViewById(R.id.btnPlaceMyOrder);
        String address = intent.getStringExtra("nameAddress");

        mListAddress1 = new ArrayList<>();
        user = SharedPreferences.getInstance(this).getUser();
        RadioGroup radioGroup = (RadioGroup)
                findViewById(R.id.radioGroup1);

        deliveries = new ArrayList<>();

        if(address != null){
            txtAddress.setText(address);
        }
        txtDeliveryPrice.setText("3");

        if (SharedPreferences.getInstance(this).isLoggedIn()){
            linearLocationset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ConfirmOrderActivity.this, AddressActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            callApiGetAddress();

            callApiSubTotal();

            callApiDeliveryPrice();

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group,
                                             int checkedId) {
                    //checkID trả về ID radio
                    switch (checkedId){
                        case R.id.radioExpress:
                            txtDeliveryPrice.setText("3");
                            d = 0;
                            callApiTotal(deliveries.get(0).get_id());
                            break;
                        case R.id.radioFast:
                            txtDeliveryPrice.setText("2");
                            d = 1;
                            callApiTotal(deliveries.get(1).get_id());
                            break;
                        case R.id.radioSave:
                            txtDeliveryPrice.setText("1");
                            d = 2;
                            callApiTotal(deliveries.get(2).get_id());
                            break;
                    }
                }
            });

            btnPlaceMyOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (txtAddress.getText().toString() == ""){
                        Toast.makeText(ConfirmOrderActivity.this, "Please chose address", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Map body = new HashMap<>();
                    body.put("userId", user.getId());
                    body.put("address", txtAddress.getText().toString());
                    body.put("phoneNumber", user.getPhoneNumber());
                    body.put("delivery", deliveries.get(d).get_id());
                    APIService.apiService.placeOrder(body).enqueue(new Callback<ResponseBody>(){

                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()){
                                Intent intent = new Intent(ConfirmOrderActivity.this, OrderCompletedActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                Log.d("TAG", "response not success");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.d("TAG", "On Failure");
                        }
                    });
                }
            });


        }else {
            Intent intent1 = new Intent(ConfirmOrderActivity.this, SignInActivity.class);
            startActivity(intent1);
            finish();
        }
    }

    private void callApiGetAddress() {
        APIService.apiService.getAddress(user.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray jsonArray = jsonObject.getJSONArray("addresses");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject itemObject = jsonArray.getJSONObject(i);
                            String id = itemObject.getString("_id");
                            String itemAddress = itemObject.getString("address");
                            // Trích xuất các thông tin khác của sản phẩm tại đây
                            Address address1 = new Address(id, itemAddress);
                            mListAddress1.add(address1);
                        }
                        if(txtAddress.getText() != null){
                            return;
                        }else txtAddress.setText(mListAddress1.get(0).getAddress());
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Xử lý khi phản hồi không thành công
                    Toast.makeText(ConfirmOrderActivity.this, "response not success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ConfirmOrderActivity.this, "Call api error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callApiTotal(String idDelivery) {
        Map<String, Object> body = new HashMap<>();
        body.put("userId", user.getId());
        body.put("delivery", idDelivery);
        APIService.apiService.totalPriceWithDelivery(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        JSONObject jsonObject = new JSONObject(json);
                        Double price = jsonObject.getDouble("result");
                        txtTotalPrice.setText(String.valueOf(price));
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Xử lý khi phản hồi không thành công
                    Log.d("TAG", "response not success");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("TAG", "On Failure");
            }
        });
    }

    private void callApiDeliveryPrice() {
        APIService.apiService.getDeliveries(user.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject itemObject = jsonArray.getJSONObject(i);
                            String id = itemObject.getString("_id");
                            String itemName = itemObject.getString("name");
                            int itemPrice = itemObject.getInt("price");
                            // Trích xuất các thông tin khác của sản phẩm tại đây
                            Delivery delivery = new Delivery(id, itemName, itemPrice);
                            deliveries.add(delivery);
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Xử lý khi phản hồi không thành công
                    Log.d("TAG", "response not success");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("TAG", "On Failure");
            }
        });
    }

    private void callApiSubTotal() {

        APIService.apiService.totalPrice(user.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        JSONObject jsonObject = new JSONObject(json);
                        Double price = jsonObject.getDouble("result");
                        txtPrice.setText(String.valueOf(price));
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Xử lý khi phản hồi không thành công
                    Log.d("TAG", "response not success");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("TAG", "On Failure");
            }
        });
    }
}