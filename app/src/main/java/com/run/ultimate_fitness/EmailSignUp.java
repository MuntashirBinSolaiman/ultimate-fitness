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


public class EmailSignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailAddressTxt,passwordTxt,confirmPasswordTxt;
    private ProgressBar secondProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_sign_up);

        mAuth = FirebaseAuth.getInstance();

        emailAddressTxt = findViewById(R.id.signUpUsernameEditText);
        passwordTxt = findViewById(R.id.signUpPasswordEditText);
        confirmPasswordTxt = findViewById(R.id.confirmSignUpPassword);
        secondProgressBar = findViewById(R.id.emailProgressBar);
    }

    public void registerEmail(View view){
        try{AddToFirebase();}catch(Exception e){}

    }

    private void AddToFirebase(){
        String email = emailAddressTxt.getText().toString();
        String password = passwordTxt.getText().toString();
        String confirmPassword = confirmPasswordTxt.getText().toString();

        if(email.isEmpty()){
            emailAddressTxt.setError("Email is required");
            emailAddressTxt.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailAddressTxt.setError("Please enter a valid email address");
            emailAddressTxt.requestFocus();
            return;
        }

        if(password.isEmpty()){
            emailAddressTxt.setError("Password is required");
            emailAddressTxt.requestFocus();
            return;
        }

        if(confirmPassword.isEmpty()){
            confirmPasswordTxt.setError("Please confirm the password");
            confirmPasswordTxt.requestFocus();
            return;
        }

        if(password.length() < 6)
        {
            passwordTxt.setError("Min password length should be 6 characters");
            passwordTxt.requestFocus();
            return;
        }

        if(!password.equals(confirmPassword))
        {
            confirmPasswordTxt.setError("Passwords do mot match");
            confirmPasswordTxt.requestFocus();
            return;
        }

        //secondProgressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //progressBar.setVisibility(View.VISIBLE);
                            Toast.makeText(EmailSignUp.this,"Email and password registered successfully",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(EmailSignUp.this, SignUpInformation.class);
                            startActivity(intent);
                        }else{
                            //something went wrong
                            Toast.makeText(EmailSignUp.this,"Failed to registers email and password", Toast.LENGTH_LONG).show();
                            secondProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}