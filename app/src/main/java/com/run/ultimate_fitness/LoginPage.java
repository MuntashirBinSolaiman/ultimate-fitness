package com.run.ultimate_fitness;

import static com.run.ultimate_fitness.utils.Constants.UID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.run.ultimate_fitness.utils.Constants;


public class LoginPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText loginEmailTxt, loginPasswordTxt;
    private ProgressBar TheProgressBar;
    private String firstName,lastName, phoneNumber, weight,height;
    private TextView loginButton;

    public static final String IS_LOGGED_IN ="isLoggedIn";
    public static final String USER_PREFS ="userPrefs";
    public static final String FIRST_NAME ="firstName";
    public static final String LAST_NAME ="lastName";
    public static final String PHONE_NUMBER ="phoneNumber";
    public static final String WEIGHT ="weight";
    public static final String HEIGHT ="height";
    public static final String PICTURE ="picture";

    public static final String CREDENTIALS_PREFS = "credentials";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";
    public static final String USER_UID = "uid";

    private String uid ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page2);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();

        loginEmailTxt = findViewById(R.id.loginUsernameEditText);
        loginPasswordTxt = findViewById(R.id.loginPasswordEditText);
        TheProgressBar = findViewById(R.id.loginProgressbar);
        loginButton = findViewById(R.id.loginButton);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

   //Overrides back button to close app
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    public void login(View view){

        if (isNetworkAvailable())
        {
            logInUser();

        }else{

            new AlertDialog.Builder(this)
                    .setTitle("Check Internet connection")
                    .setMessage("Please make sure you have an active internet connection")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, null)
                    .show();

            TheProgressBar.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
        }

    }

    //Navigates to sign up page
    public void signUp(View view){
        Intent intent = new Intent(this, SendOTPActivity.class);
        startActivity(intent);
    }

    //Navigates to forgot password page
    public void forgotPassword(View view){
        Intent intent = new Intent(this, ForgotPassword.class);
        startActivity(intent);
    }

    //logs in user
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

        loginButton.setVisibility(View.GONE);
        TheProgressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        loadData();
                        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        saveCredentials(email,password);

                    }else{
                        Toast.makeText(LoginPage.this,"Failed to login! Please check your credentials", Toast.LENGTH_LONG).show();
                        TheProgressBar.setVisibility(View.GONE);
                        loginButton.setVisibility(View.VISIBLE);
                    }
                });
    }

    //Gets data from firestore database
    private void loadData(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference noteRef = db.collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        noteRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()){

                        String firstName = documentSnapshot.getString("firstName");
                        String lastName = documentSnapshot.getString("lastName");
                        String phoneNumber = documentSnapshot.getLong("phoneNumber").toString();
                        String weight = documentSnapshot.getDouble("weight").toString();
                        String height = documentSnapshot.getDouble("height").toString();
                        String picture = documentSnapshot.getString("picture").toString();

                        String temp_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        SharedPreferences sharedPreferences2 = getSharedPreferences(CREDENTIALS_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                        editor2.putString(USER_UID, temp_uid);
                        editor2.apply();




                        editor.putString(FIRST_NAME, firstName);
                        editor.putString(LAST_NAME, lastName);
                        editor.putString(PHONE_NUMBER, phoneNumber);
                        editor.putString(WEIGHT, weight);
                        editor.putString(HEIGHT, height);
                        editor.putString(PICTURE, picture);
                        editor.putBoolean(IS_LOGGED_IN, true);
                        editor.apply();

                        Intent intent = new Intent(LoginPage.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }else{
                        Toast.makeText(LoginPage.this,"Document does not exist",Toast.LENGTH_LONG).show();
                        loginButton.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(LoginPage.this,"Error!!",Toast.LENGTH_LONG).show());
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