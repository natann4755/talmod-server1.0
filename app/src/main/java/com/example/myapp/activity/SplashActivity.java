package com.example.myapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import com.example.model.Profile;
import com.example.myapp.R;
import com.example.myapp.dataBase.AppDataBase;
import com.example.myapp.utils.Language;
import com.example.myapp.utils.ManageSharedPreferences;
import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setLanguage();
//        startNextActivity();
        moveToSignUp();
    }

    private void moveToSignUp() {
        startActivity(new Intent(SplashActivity.this, SignUpLoginActivity.class));
    }


    private void setLanguage() {
        Profile mProfile = ManageSharedPreferences.getProfile(getBaseContext());
        if (mProfile == null) {
            return;
        }
        Language.setAppLanguage(mProfile.getLanguage(), getBaseContext());
    }

    private void startNextActivity() {
        if (isFirstTime()) {
            startActivity(new Intent(SplashActivity.this, ProfileActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
        finish();
    }

    private boolean isFirstTime() {
        ArrayList<Integer> mTypeOfLeaning = (ArrayList<Integer>) AppDataBase.getInstance(this).daoLearning().getAllIndexTypeOfLeaning();
        return mTypeOfLeaning.size() < 1;
    }
}