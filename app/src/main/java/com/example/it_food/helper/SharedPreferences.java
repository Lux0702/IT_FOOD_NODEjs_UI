package com.example.it_food.helper;

import android.content.Context;
import android.content.Intent;

import com.example.it_food.activity.SignInActivity;
import com.example.it_food.model.User;

public class SharedPreferences {
    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_AVATAR = "avatar";
    private  static   SharedPreferences mInstance;
    private static Context ctx;

    //Constructor
    public SharedPreferences(Context context) {
        ctx = context;
    }
    public static synchronized SharedPreferences getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPreferences(context);
        }
        return mInstance;
    }

    public void userLogin(User user) {
        android.content.SharedPreferences sharedPreferences = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ID, user.getId());
        editor.putString(KEY_NAME, user.getName());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_PHONE, user.getPhoneNumber());
        editor.putString(KEY_ADDRESS, user.getAddress());
        editor.putString(KEY_GENDER, user.getGender());
        editor.putString(KEY_AVATAR, user.getAvatar());
        editor.apply();
    }
    public boolean isLoggedIn() {
        android.content.SharedPreferences sharedPreferences = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NAME, null) != null;
    }
    public User getUser() {
        android.content.SharedPreferences sharedPreferences = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString(KEY_ID, null),
                sharedPreferences.getString(KEY_PHONE, null),
                sharedPreferences.getString(KEY_NAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_GENDER, null),
                sharedPreferences.getString(KEY_AVATAR, null),
                sharedPreferences.getString(KEY_ADDRESS, null)
        );
    }
    public void logout() {
        android.content.SharedPreferences sharedPreferences = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        ctx.startActivity(new Intent(ctx, SignInActivity.class));
    }

    public void updateImage(String profileImage) {
        android.content.SharedPreferences sharedPreferences = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_AVATAR, profileImage);
        editor.apply();
    }
}
