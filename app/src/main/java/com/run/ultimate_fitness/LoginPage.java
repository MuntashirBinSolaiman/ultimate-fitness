package com.run.ultimate_fitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText loginEmailTxt, loginPasswordTxt;
    private ProgressBar TheProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page2);

        mAuth = FirebaseAuth.getInstance();

        loginEmailTxt = findViewById(R.id.loginUsernameEditText);
        loginPasswordTxt = findViewById(R.id.loginPasswordEditText);
        TheProgressBar = findViewById(R.id.emailProgressBar);
    }

    public void login(View view){
        logInUser();
    }

    public void signUp(View view){
        Intent intent = new Intent(this, EmailSignUp.class);
        startActivity(intent);
    }

    public void forgotPassword(View view){
        Intent intent = new Intent(this, EmailSignUp.class);
        startActivity(intent);
    }

    private void logInUser(){

        String email = loginEmailTxt.getText().toString().trim();
        String password = loginPasswordTxt.getText().toString();

        if(email.isEmpty()){
            loginEmailTxt.setError("Please enter email");
            loginEmailTxt.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            loginEmailTxt.setError("Please enter a valid email address");
            loginEmailTxt.requestFocus();
            return;
        }

        if(password.isEmpty()){
            loginPasswordTxt.setError("Please enter password");
            loginPasswordTxt.requestFocus();
            return;
        }

        if(password.length() < 6)
        {
            loginPasswordTxt.setError("Min password length should be 6 characters");
            loginPasswordTxt.requestFocus();
            return;
        }

        TheProgressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(LoginPage.this, MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(LoginPage.this,"Failed to login! Please check your credentials", Toast.LENGTH_LONG).show();
                            TheProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

}