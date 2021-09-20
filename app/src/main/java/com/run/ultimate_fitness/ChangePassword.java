package com.run.ultimate_fitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {

    private TextView toolDisplay, toolLogout;
    private EditText oldPasswordTxt, newPasswordTxt, confirmPasswordTxt, emailTxt;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        toolDisplay = findViewById(R.id.toolbarTextView);
        toolLogout = findViewById(R.id.toolbarLogoutTextView);
        oldPasswordTxt = findViewById(R.id.oldPasswordChangeEditText);
        newPasswordTxt = findViewById(R.id.newPasswordChangeEditText);
        confirmPasswordTxt = findViewById(R.id.confirmPasswordChangeEditText);
        emailTxt = findViewById(R.id.emailChangeEditText);

        toolDisplay.setText("Change Password");
        toolLogout.setVisibility(View.GONE);
    }

    public void updatePassword(View view){
        uploadDetails();
    }

    public void forgotPassword(View view){
        Intent intent = new Intent(this, EmailSignUp.class);
        startActivity(intent);
    }

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
                            }else {
                                Toast.makeText(ChangePassword.this,"Password successfully updated",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(ChangePassword.this,"Authentication failed",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
