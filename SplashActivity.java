package com.example.assignmentno04;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import pl.droidsonroids.gif.GifImageView;

public class SplashActivity extends AppCompatActivity {

    GifImageView logoSplash;
    Animation rotate, top,bottom,reversetop,righttoleft,lefttoright;
    final int finaldelay=6000;
    final int rotatedelay=2000;
    final int reversedelay=4000;
    TextView Tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_activity);

        logoSplash = findViewById(R.id.splashLogo);
        Tv = findViewById(R.id.textView);
        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        top = AnimationUtils.loadAnimation(this, R.anim.top);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom);
        reversetop = AnimationUtils.loadAnimation(this, R.anim.reversetop);
        righttoleft = AnimationUtils.loadAnimation(this, R.anim.righttoleft);
        lefttoright = AnimationUtils.loadAnimation(this, R.anim.lefttoright);
        logoSplash.setAnimation(top);
        Tv.setAnimation(lefttoright);
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                logoSplash.setAnimation(rotate);
                Tv.setAnimation(rotate);
            }
        }, rotatedelay);

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                logoSplash.setAnimation(bottom);
                Tv.setAnimation(reversetop);
            }
        },reversedelay);
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(SplashActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        },finaldelay);
    }
}