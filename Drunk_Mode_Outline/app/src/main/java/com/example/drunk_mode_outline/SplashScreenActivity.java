package com.example.drunk_mode_outline;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.drunk_mode_outline.R;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EasySplashScreen config = new EasySplashScreen(SplashScreenActivity.this)
                .withFullScreen()
                .withTargetActivity(SignInActivity.class)
                .withSplashTimeOut(5000)        // 5sec splash screen
                .withBackgroundColor(Color.parseColor("#6ce0cd"))
                .withBeforeLogoText("Welcome to Drunk Mode")
                .withLogo(R.mipmap.ic_launcher);

        config.getBeforeLogoTextView().setTextColor(Color.WHITE);

        View easySplashScreen = config.create();
        setContentView(easySplashScreen);
    }
}
