package com.run.ultimate_fitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class EmailSignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailAddressTxt,passwordTxt,confirmPasswordTxt;
    private TextView addEmailButton;
    private ProgressBar progressBar;

    private String uid ="";

    public static final String CREDENTIALS_PREFS = "credentials";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";
    public static final String USER_UID = "uid";

    public DatabaseReference root ;
    private String temp_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_sign_up);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        mAuth = FirebaseAuth.getInstance();

        emailAddressTxt = findViewById(R.id.signUpUsernameEditText);
        addEmailButton =findViewById(R.id.addEmailButton);
        passwordTxt = findViewById(R.id.signUpPasswordEditText);
        confirmPasswordTxt = findViewById(R.id.confirmSignUpPassword);
        progressBar = findViewById(R.id.loginProgressbar);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void registerEmail(View view){

        if (isNetworkAvailable())
        {
            AddToFirebase();

        }else{

            new AlertDialog.Builder(this)
                    .setTitle("Check Internet connection")
                    .setMessage("Please make sure you have an active internet connection")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, null)
                    .show();

            progressBar.setVisibility(View.GONE);
            addEmailButton.setVisibility(View.VISIBLE);
        }
    }

    //Adds email and password to firebase authenticator
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

        progressBar.setVisibility(View.VISIBLE);
        addEmailButton.setVisibility(View.GONE);

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressBar.setVisibility(View.VISIBLE);
                            addEmailButton.setVisibility(View.GONE);

                            saveCredentials(email,password);

                            Toast.makeText(EmailSignUp.this,"Email and password registered successfully",Toast.LENGTH_LONG).show();

                            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


                            Intent intent = new Intent(EmailSignUp.this, SignUpInformation.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }else{
                            //something went wrong
                            Toast.makeText(EmailSignUp.this,"Failed to registers email and password", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            addEmailButton.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }


    //Saves login credentials to shared preferences
    private void saveCredentials(String email, String password){
        SharedPreferences sharedPreferences = getSharedPreferences(CREDENTIALS_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_UID,uid);
        editor.putString(EMAIL,email);
        editor.putString(PASSWORD,password);
        editor.apply();
    }
}