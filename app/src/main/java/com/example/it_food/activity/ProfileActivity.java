package com.example.it_food.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.it_food.R;
import com.example.it_food.helper.SharedPreferences;
import com.example.it_food.model.User;

public class ProfileActivity extends AppCompatActivity {
    TextView txtId, txtUserName, txtEmail, txtPhone, txtAddress;
    String txtPassword,phone;
    TextView txtLogout, txtEdit;
    ImageView imgProfile, imgGender;

    String profileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);

        findViewById(R.id.imageArrowleft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_left, android.R.anim.fade_out);
            }
        });

        if(SharedPreferences.getInstance(this).isLoggedIn()) {
            txtUserName = findViewById(R.id.txtUsername);
            txtEmail = findViewById(R.id.txtEmail);
            txtPhone = findViewById(R.id.txtPhone);
            txtLogout = findViewById(R.id.txtLogout);
            txtEdit = findViewById(R.id.txtEdit);

            txtAddress = findViewById(R.id.txtAddress);
            imgGender = findViewById(R.id.imgGender);
            imgProfile = findViewById(R.id.imgProfile);
            User user = SharedPreferences.getInstance(this).getUser();
            txtEmail.setText(user.getEmail());
            txtPhone.setText(user.getPhoneNumber());
            txtUserName.setText(user.getName());
            txtPassword=getIntent().getStringExtra("m_Password");
            phone=txtPhone.getText().toString();

            if (user.getGender()!="Nam")
                imgGender.setImageResource(R.drawable.ic_female);

            Glide.with(getApplicationContext()).load(user.getAvatar()).into(imgProfile);

            txtLogout.setOnClickListener(this::onClick);

            txtEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToActivity(txtPassword);
                }
            });

        }  else {
            Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void onClick(View view) {
        if (view.equals(txtLogout)) {
            SharedPreferences.getInstance(getApplicationContext()).logout();
        }
    }

    private void goToActivity(String password) {
        Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
        intent.putExtra("m_Password",password);
        startActivity(intent);
        finish();
    }
}