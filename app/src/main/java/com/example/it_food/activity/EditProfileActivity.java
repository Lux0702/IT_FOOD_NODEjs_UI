package com.example.it_food.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.it_food.Adapter.ProductAdapter;
import com.example.it_food.InterFace.APIService;
import com.example.it_food.R;
import com.example.it_food.helper.RealPathUtil;
import com.example.it_food.helper.SharedPreferences;
import com.example.it_food.model.ProductItem;
import com.example.it_food.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    private static final int MY_REQUEST_CODE = 10;
    public static final String TAG = EditProfileActivity.class.getName();
    Button btnEditPasswd;
    ProgressDialog progressDialog;
    String phoneNumber, currentPassword;
    EditText edtPhone, edtUserName, edtEmail, edtGender, edtPassword;
    ImageView imgProfileEdit, imgFromApi;
    private Uri mUri;
    String avatar;
    User user;
    private APIService apiService;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.e(TAG, "onActivityResult");
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data == null) {
                            return;
                        }
                        Uri uri = data.getData();
                        mUri=uri;
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            imgProfileEdit.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();

                        }
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_profile);
        btnEditPasswd = findViewById(R.id.btnEditPasswd);
        imgProfileEdit = findViewById(R.id.imgProfileEdit);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
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
                //progressDialog = new ProgressDialog(EditProfileActivity.this);
                //chooseImage();
                onCLickRequestPermission();

            }
        });
        btnEditPasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoChangePasswordActivity(phoneNumber, currentPassword);
            }
        });
        findViewById(R.id.btnEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mUri!=null)
                {
                    callApiUploadImage();
                    gotoActivity();
                }
            }
        });
    }


    private void onCLickRequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission, MY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            openGallery();
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



    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Image"));
    }
    private void callApiUploadImage() {
        progressDialog.show();
        String id=user.getId();
        RequestBody requestBodyId = RequestBody.create(MediaType.parse("multipart/form-data"),id);
        String strRealpath= RealPathUtil.getRealPath(this,mUri);
        Log.e("IT.FOOD",strRealpath);
        File file=new File(strRealpath);
        RequestBody requestBodyImage = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part multipartBodyImage = MultipartBody.Part.createFormData("avatar", file.getName(), requestBodyImage);
        User user = SharedPreferences.getInstance(this).getUser();
        APIService.apiService.uploadImage(requestBodyId,multipartBodyImage).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "Upload success", Toast.LENGTH_SHORT).show();

                    try {
                        String json = response.body().string();
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject itemObject = jsonArray.getJSONObject(i);
                            String id = itemObject.getString("id");
                            String phone = itemObject.getString("phoneNumber");
                            String name = itemObject.getString("name");
                            String email = itemObject.getString("email");
                            String gender = itemObject.getString("gender");
                            avatar = itemObject.getString("avatar");

                            String address = itemObject.getString("address");
                            SharedPreferences.getInstance(getApplicationContext()).userLogin(new User(id, phone, name, email, gender, avatar, address));
                        }
                        Glide.with(EditProfileActivity.this).load(avatar).into(imgProfileEdit);
                        SharedPreferences.getInstance(getApplicationContext()).updateImage(avatar);
                        Log.e(TAG,  SharedPreferences.getInstance(getApplicationContext()).getUser().getName());



                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Xử lý khi phản hồi không thành công
                    Toast.makeText(EditProfileActivity.this, "response not success" +response.errorBody(), Toast.LENGTH_SHORT).show();
                    Log.e("TAG","response not success" +response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditProfileActivity.this, "Call api error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void gotoActivity() {
        Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
        //intent.putExtra("m_profile",profile);
        startActivity(intent);
        finish();
    }
}