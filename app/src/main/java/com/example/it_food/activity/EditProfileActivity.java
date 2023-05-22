package com.example.it_food.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.it_food.InterFace.APIService;
import com.example.it_food.R;
import com.example.it_food.helper.SharedPreferences;
import com.example.it_food.model.User;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    Button btnEditPasswd;
    ProgressDialog progressDialog;
    String phoneNumber, currentPassword;
    EditText edtPhone, edtUserName, edtEmail, edtGender, edtPassword;
    ImageView imgProfileEdit;
    private Uri selectedImageUri;
    User user;
    private APIService apiService;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_profile);
        btnEditPasswd = findViewById(R.id.btnEditPasswd);
        imgProfileEdit = findViewById(R.id.imgProfileEdit);
        findViewById(R.id.imageArrowleft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_left, android.R.anim.fade_out);
            }
        });
        if (SharedPreferences.getInstance(this).isLoggedIn()) {
            edtUserName = findViewById(R.id.edtUserName);
            edtEmail = findViewById(R.id.edtEmail);
            edtPhone = findViewById(R.id.edtPhone);
            edtGender = findViewById(R.id.edtGender);
            edtPassword = findViewById(R.id.edtPassword);
            imgProfileEdit = findViewById(R.id.imgProfileEdit);

            user = SharedPreferences.getInstance(this).getUser();
            edtEmail.setText(user.getEmail());
            edtPhone.setText(user.getPhoneNumber());
            edtUserName.setText(user.getName());
            edtGender.setText(user.getGender());
            edtPassword.setText(user.getPassword());
            currentPassword = edtPassword.getText().toString();
            phoneNumber = edtPhone.getText().toString();
            Glide.with(getApplicationContext()).load(user.getAvatar()).into(imgProfileEdit);

        } else {
            Intent intent = new Intent(EditProfileActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        }

        findViewById(R.id.avatarFrame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(EditProfileActivity.this);
                chooseImage();
            }
        });
        btnEditPasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoChangePasswordActivity(phoneNumber, currentPassword);
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    Uri imageUri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            imgProfileEdit

            // create a file from the image URI
            File file = new File(getRealPathFromURI(imageUri));

            // create a request body with the file
            //RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);

            // create a multipart request with the request body
            //MultipartBody.Part imagePart = MultipartBody.Part.createFormData("avatar", file.getName(), requestBody);

            //RequestBody idRequestBody = RequestBody.create(MediaType.parse("text/plain"), user.getId());
            // upload the image using the API service
            *//*apiService.uploadImage(idRequestBody,imagePart).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(EditProfileActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(EditProfileActivity.this, "Failed " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Failed " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });*//*

        }*/
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            Log.d("TAG", "Selected Image Uri: " + selectedImageUri); // Thêm dòng này

            imgProfileEdit.setImageURI(selectedImageUri);
            uploadImageToApi();
        }
    }


    private String getRealPathFromURI(Uri uri) {
        String filePath = null;
        Cursor cursor = null;
        try {
            String[] projection = {MediaStore.Images.ImageColumns.DATA};
            cursor = getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA);
                filePath = cursor.getString(columnIndex);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return filePath;
    }


    private void gotoChangePasswordActivity(String phoneNumber, String currentPassword) {
        Intent intent = new Intent(EditProfileActivity.this, ChangePasswordActivity.class);
        intent.putExtra("phone_Number", phoneNumber);
        intent.putExtra("current_Password", currentPassword);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_left, android.R.anim.fade_out);
        finish();
    }

    private void uploadImageToApi() {
        if (selectedImageUri != null) {
            String idValue = user.getId();
            File imageFile = new File(getRealPathFromURI(selectedImageUri)); // Lấy đường dẫn thực tế của ảnh từ Uri
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageFile);
            MultipartBody.Part avartaPart = MultipartBody.Part.createFormData("avatar", imageFile.getName(), requestFile);

            // Tạo RequestBody cho trường "id"
            RequestBody id = RequestBody.create(MediaType.parse("id"), idValue);

            // Tạo API service
            APIService.apiService.uploadImage(id, avartaPart).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Toast.makeText(getApplicationContext(), "Upload successful", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
}