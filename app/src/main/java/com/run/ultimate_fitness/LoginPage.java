package com.run.ultimate_fitness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.run.ultimate_fitness.ui.home.HomeFragment;
import com.run.ultimate_fitness.ui.home.HomeViewModel;

public class LoginPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page2);
    }

    public void login(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void signUp(View view){
        Intent intent = new Intent(this, EmailSignUp.class);
        startActivity(intent);
    }
}