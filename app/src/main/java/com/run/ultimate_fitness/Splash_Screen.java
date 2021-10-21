package com.run.ultimate_fitness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Objects;

public class Splash_Screen extends AppCompatActivity {

    private ImageView img_U,img_F,img_Ultimate,img_Fitness;
    private Animation leftAnimation,rightAnimation,bottomAnimation, bottomAnimation2;

    private static int SPLASH_SCREEN = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();




        initViews();
        loadAnimations();
        doAnimations();
        goToDashboad();
    }
    public void onBackPressed(){

    }

    private void loadAnimations() {
        leftAnimation = AnimationUtils.loadAnimation(this, R.anim.left_animation);
        rightAnimation = AnimationUtils.loadAnimation(this, R.anim.right_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        bottomAnimation2 = AnimationUtils.loadAnimation(this, R.anim.bottom_animation2);

    }

    private void initViews() {
        img_U = (ImageView) findViewById(R.id.img_U);
        img_F = (ImageView) findViewById(R.id.img_F);
        img_Ultimate = (ImageView) findViewById(R.id.img_Ultimate);
        img_Fitness = (ImageView) findViewById(R.id.img_Fitness);

    }

    private void doAnimations() {
        img_U.setAnimation(leftAnimation);
        img_F.setAnimation(rightAnimation);
        img_Ultimate.setAnimation(bottomAnimation);
        img_Fitness.setAnimation(bottomAnimation2);


    }

    private void goToDashboad() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash_Screen.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        }, SPLASH_SCREEN);
    }
}