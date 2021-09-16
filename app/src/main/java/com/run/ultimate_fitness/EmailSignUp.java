package com.run.ultimate_fitness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EmailSignUp extends AppCompatActivity {

    private EditText emailAddressTxt;
    private EditText passwordTxt;
    private EditText confirmPasswordTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_sign_up);

        emailAddressTxt = findViewById(R.id.signUpUsernameEditText);
        passwordTxt = findViewById(R.id.signUpPasswordEditText);
        confirmPasswordTxt = findViewById(R.id.confirmSignUpPassword);
    }

    public void registerEmail(View view){
        String email = emailAddressTxt.getText().toString();
        String password = passwordTxt.getText().toString();
        String confirmPassword = confirmPasswordTxt.getText().toString();

        Intent intent = new Intent(this, SignUpInformation.class);
        startActivity(intent);
    }
}