package com.run.ultimate_fitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

    public class ForgotPassword extends AppCompatActivity {
    private EditText emailTxt;
    private ImageView backImage;
    private TextView submitButton, toolBarLogOut, toolBarTitle;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();

        toolBarTitle = findViewById(R.id.toolbarTextView);
        backImage = findViewById(R.id.toolbarBackButton);
        toolBarLogOut = findViewById(R.id.toolbarLogoutTextView);
        emailTxt = findViewById(R.id.emailForgotPasswordEditText);
        progressBar = findViewById(R.id.forgotEmailProgressbar);
        submitButton = findViewById(R.id.submitForgotPasswordButton);

        toolBarLogOut.setVisibility(View.GONE);
        toolBarTitle.setText("Password Recovery");
        backImage.setOnClickListener(v -> {

            Intent intent = new Intent(ForgotPassword.this, LoginPage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //gets text from fields
    //connects to firebase to send recovery email
    public void forgotPassword(View view) {

        if (isNetworkAvailable())
        {
            sendEmail();

        }else{

            new AlertDialog.Builder(this)
                    .setTitle("Check Internet connection")
                    .setMessage("Please make sure you have an active internet connection")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, null)
                    .show();

            progressBar.setVisibility(View.GONE);
            submitButton.setVisibility(View.VISIBLE);
        }
    }

    private void sendEmail(){

        String email = emailTxt.getText().toString().trim();

        if(email.isEmpty()){
            emailTxt.setError("Please enter email");
            emailTxt.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailTxt.setError("Please enter a valid email address");
            emailTxt.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        submitButton.setVisibility(View.GONE);


        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this,"Password link sent to email", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ForgotPassword.this, LoginPage.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(ForgotPassword.this,"Failed to send password reset link! Please try again", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    submitButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}