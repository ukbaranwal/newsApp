package com.at.newsapp.Activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ProgressBar;

import com.at.newsapp.R;

public class SplashscreenActivity extends AppCompatActivity {

    Handler handler;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashscreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }
    private final static int FADE_DURATION = 1500;
}