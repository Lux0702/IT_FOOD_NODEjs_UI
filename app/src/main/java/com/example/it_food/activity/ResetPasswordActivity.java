package com.example.it_food.activity;

import static com.example.it_food.InterFace.APIService.apiService;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.it_food.R;
import com.example.it_food.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {

    private  String  mphoneNumber,newPass,confirmPass;
    EditText etNewPassword,etConfirmPassword;
    private static final  String TAG=ResetPasswordActivity.class.getName();

    Button btnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        mphoneNumber=getIntent().getStringExtra("phone_Number");
        init();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(etNewPassword.getText().toString())) {
                    etNewPassword.setError("Please enter code OTP");
                    etNewPassword.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(etConfirmPassword.getText().toString())) {
                    etConfirmPassword.setError("Please enter code OTP");
                    etConfirmPassword.requestFocus();
                    return;
                }
                String password = etNewPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();

                if (password.equals(confirmPassword)) {
                    // Hai trường password và confirm password giống nhau
                    // Thực hiện các hành động tiếp theo
                    ResetPassword(mphoneNumber);
                      gotoFinishResetActivity();

                } else {
                    // Hai trường password và confirm password không giống nhau
                    // Hiển thị thông báo lỗi cho người dùng
                    Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_SHORT).show();
                    etConfirmPassword.setError("Please enter correct password");
                    etConfirmPassword.requestFocus();
                }
            }
        });


    }
    private void getDataUser(String phoneNumber){
        Call<User> getUserCall = apiService.getUserByPhoneNumber(phoneNumber);
        getUserCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    String name = user.getName();
                    String email = user.getEmail();

                    Log.e(TAG, "name:"+name);
                    // Tiếp tục xử lý sau khi nhận được thông tin người dùng
                    Toast.makeText(ResetPasswordActivity.this, "name:"+name, Toast.LENGTH_SHORT).show();

                } else {
                    // Xử lý lỗi
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Xử lý lỗi kết nối
                Log.e(TAG, "Error: " + t.getMessage());
            }
        });
    }
    private void ResetPassword(String phone){
            getDataUser(phone);
    }
    private void init(){
        etConfirmPassword=findViewById(R.id.etConfirmPasswor);
        etNewPassword=findViewById(R.id.etNewPasswordFo);
        btnNext=findViewById(R.id.btnNext);

    }
    private void gotoFinishResetActivity() {
        Intent intent = new Intent(ResetPasswordActivity.this, FinishResetActivity.class);
        //intent.putExtra("phone_Number", phoneNumber);
        startActivity(intent);
        finish();
    }
}