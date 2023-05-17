package com.example.it_food.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.it_food.InterFace.APIService;
import com.example.it_food.R;
import com.example.it_food.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

EditText etName,etPhone,etPassword,etAddress, etEmail,etGender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);
        etName=findViewById(R.id.etYourName);
        etPassword=findViewById(R.id.etPasswordSignUp);
        etPhone=findViewById(R.id.etPhoneSignUp);
        etEmail=findViewById(R.id.etEmailSignUP);
        etAddress=findViewById(R.id.etYourAddress);
        etGender=findViewById(R.id.etGender);
        findViewById(R.id.imageArrowleft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_left, android.R.anim.fade_out);
            }
        });
        findViewById(R.id.btnSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userRegister();
            }
        });
    }
    private void userRegister(){
        final String username = etName.getText().toString();
        final String password = etPassword.getText().toString().trim();
        final String phone = etPhone.getText().toString();
        final String email = etEmail.getText().toString();
        final String address = etAddress.getText().toString();
        final String gender = etGender.getText().toString();

        if (TextUtils.isEmpty(username)) {
            etName.setError("Please enter your username");
            etName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Please enter your password");
            etPassword.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Please enter your username");
            etEmail.requestFocus();
            return;
        }else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email");
            etEmail.requestFocus();
            return;
        }
        String phonePattern = "^\\d{10,}$";
        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Please enter your password");
            etPhone.requestFocus();
            return;
        }else if (!phone.matches(phonePattern)) {
            etPhone.setError("Phone number must contain at least 10 digits");
            etPhone.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(address)) {
            etAddress.setError("Please enter your username");
            etAddress.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(gender)) {
            etGender.setError("Please enter your password");
            etGender.requestFocus();
            return;
        }
        String passwordPattern = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}$";
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Please enter your password");
            etPassword.requestFocus();
            return;
        } else if (!password.matches(passwordPattern)) {
            etPassword.setError("Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one digit");
            etPassword.requestFocus();
            return;
        }

        User user = new User();
        user.setPhoneNumber(phone);
        user.setAddress(address);
        user.setEmail(email);
        user.setGender(gender);
        user.setName(username);
        user.setPassword(password);
        APIService.apiService.register(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {

                    // Đăng nhập thành công
                    Toast.makeText(SignUpActivity.this, "Register user successfully", Toast.LENGTH_SHORT).show();
                    // Chuyển đến activity tiếp theo sau khi đăng nhập thành công
                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Xử lý thất bại, hiển thị thông báo lỗi
                    Toast.makeText(SignUpActivity.this, "Register Failed", Toast.LENGTH_SHORT).show();
                    Log.e("SignUpActivity", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Register Fail", Toast.LENGTH_SHORT).show();

            }
        });
    }
}