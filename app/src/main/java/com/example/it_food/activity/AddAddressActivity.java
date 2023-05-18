package com.example.it_food.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

public class AddAddressActivity extends AppCompatActivity {

    private AppCompatButton btnAddNewAddress;
    private TextView txtNewAddress;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        btnAddNewAddress = findViewById(R.id.btnAddNewAddress);
        txtNewAddress = findViewById(R.id.txtNewAddress);
        user = SharedPreferences.getInstance(this).getUser();
        btnAddNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtNewAddress.getText() == null){
                    txtNewAddress.setError("Chưa điền địa chỉ");
                    txtNewAddress.requestFocus();
                    return;
                }
                Map<String, Object> body = new HashMap<>();
                body.put("userId", user.getId());
                body.put("address", txtNewAddress.getText().toString());
                APIService.apiService.addAddress(body).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Intent intent = new Intent(AddAddressActivity.this, AddressActivity.class);
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
        });
    }
}