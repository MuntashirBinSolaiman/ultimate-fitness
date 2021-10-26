package com.run.ultimate_fitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {

    private TextView toolDisplay, toolLogout, updateButton, forgotPasswordButton;
    private EditText oldPasswordTxt, newPasswordTxt, confirmPasswordTxt, emailTxt;
    private ImageView backButtonImage;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
        updateButton = findViewById(R.id.updatePasswordButton);
        toolDisplay = findViewById(R.id.toolbarTextView);
        progressBar = findViewById(R.id.progressBar);
        toolLogout = findViewById(R.id.toolbarLogoutTextView);
        oldPasswordTxt = findViewById(R.id.oldPasswordChangeEditText);
        newPasswordTxt = findViewById(R.id.newPasswordChangeEditText);
        confirmPasswordTxt = findViewById(R.id.confirmPasswordChangeEditText);
        emailTxt = findViewById(R.id.emailChangeEditText);
        backButtonImage =findViewById(R.id.toolbarBackButton);

        toolDisplay.setText("Change Password");
        toolLogout.setVisibility(View.GONE);

        backButtonImage.setOnClickListener(v -> {
            Intent intent = new Intent(ChangePassword.this, ProfilePage.class);
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

    public void updatePassword(View view){

        if (isNetworkAvailable())
        {
            uploadDetails();

        }else{

            new AlertDialog.Builder(this)
                    .setTitle("Check Internet connection")
                    .setMessage("Please make sure you have an active internet connection")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, null)
                    .show();

            forgotPasswordButton.setVisibility(View.VISIBLE);
            updateButton.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    //Navigates to forgot password page
    public void forgotPassword(View view){
        Intent intent = new Intent(this, EmailSignUp.class);
        forgotPasswordButton.setVisibility(View.GONE);
        updateButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        startActivity(intent);
    }

    //Applies password changes to firestore
    private void uploadDetails(){
        String email = emailTxt.getText().toString();
        String oldPassword = oldPasswordTxt.getText().toString();
        String newPassword = newPasswordTxt.getText().toString();
        String confirmPassword = confirmPasswordTxt.getText().toString();

        if(email.isEmpty()){
            emailTxt.setError("Email is required");
            emailTxt.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailTxt.setError("Please enter a valid email address");
            emailTxt.requestFocus();
            return;
        }

        if(oldPassword.isEmpty()){
            oldPasswordTxt.setError("Password is required");
            oldPasswordTxt.requestFocus();
            return;
        }

        if(oldPassword.length() < 6){
            oldPasswordTxt.setError("Min password length should be 6 characters");
            oldPasswordTxt.requestFocus();
            return;
        }

        if(newPassword.isEmpty()){
            newPasswordTxt.setError("Password is required");
            newPasswordTxt.requestFocus();
            return;
        }

        if(newPassword.length() < 6){
            newPasswordTxt.setError("Min password length should be 6 characters");
            newPasswordTxt.requestFocus();
            return;
        }

        if(confirmPassword.isEmpty()){
            confirmPasswordTxt.setError("Password is required");
            confirmPasswordTxt.requestFocus();
            return;
        }

        if(confirmPassword.length() < 6){
            oldPasswordTxt.setError("Min password length should be 6 characters");
            oldPasswordTxt.requestFocus();
            return;
        }

        if(!newPassword.equals(confirmPassword)){
            confirmPasswordTxt.setError("Passwords do mot match");
            confirmPasswordTxt.requestFocus();
            return;
        }

        forgotPasswordButton.setVisibility(View.GONE);
        updateButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        AuthCredential credential = EmailAuthProvider.getCredential(email,oldPassword);

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    user.updatePassword(confirmPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(ChangePassword.this,"Something went wrong please try again later",Toast.LENGTH_LONG).show();
                                forgotPasswordButton.setVisibility(View.VISIBLE);
                                updateButton.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }else {
                                Toast.makeText(ChangePassword.this,"Password successfully updated",Toast.LENGTH_LONG).show();
                                forgotPasswordButton.setVisibility(View.VISIBLE);
                                updateButton.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }else {
                    Toast.makeText(ChangePassword.this,"Authentication failed",Toast.LENGTH_LONG).show();
                    forgotPasswordButton.setVisibility(View.VISIBLE);
                    updateButton.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}
