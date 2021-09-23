package com.run.ultimate_fitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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


public class LoginPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText loginEmailTxt, loginPasswordTxt;
    private ProgressBar TheProgressBar;
    private String firstName,lastName, phoneNumber, weight,height;
    private TextView loginButton;

    public static final String USER_PREFS ="userPrefs";
    public static final String FIRST_NAME ="firstName";
    public static final String LAST_NAME ="lastName";
    public static final String PHONE_NUMBER ="phoneNumber";
    public static final String WEIGHT ="weight";
    public static final String HEIGHT ="height";
    public static final String IS_LOGGED_IN ="isLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page2);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        boolean loggedIn = sharedPreferences.getBoolean(IS_LOGGED_IN,false);
        if (loggedIn) {
            Intent intent = new Intent(LoginPage.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        mAuth = FirebaseAuth.getInstance();

        loginEmailTxt = findViewById(R.id.loginUsernameEditText);
        loginPasswordTxt = findViewById(R.id.loginPasswordEditText);
        TheProgressBar = findViewById(R.id.loginProgressbar);
        loginButton = findViewById(R.id.loginButton);
    }

    public void login(View view){
        logInUser();
    }

    public void signUp(View view){
        Intent intent = new Intent(this, EmailSignUp.class);
        startActivity(intent);
    }

    public void forgotPassword(View view){
        Intent intent = new Intent(this, ForgotPassword.class);
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

        loginButton.setVisibility(View.GONE);
        TheProgressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            loadData();
                        }else{
                            Toast.makeText(LoginPage.this,"Failed to login! Please check your credentials", Toast.LENGTH_LONG).show();
                            TheProgressBar.setVisibility(View.GONE);
                            loginButton.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private void loadData(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference noteRef = db.collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){

                            String firstName = documentSnapshot.getString("firstName");
                            String lastName = documentSnapshot.getString("lastName");
                            String phoneNumber = documentSnapshot.getLong("phoneNumber").toString();
                            String weight = documentSnapshot.getDouble("weight").toString();
                            String height = documentSnapshot.getDouble("height").toString();

                            SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString(FIRST_NAME, firstName);
                            editor.putString(LAST_NAME, lastName);
                            editor.putString(PHONE_NUMBER, phoneNumber);
                            editor.putString(WEIGHT, weight);
                            editor.putString(HEIGHT, height);
                            editor.putBoolean(IS_LOGGED_IN, true);
                            editor.commit();

                            Intent intent = new Intent(LoginPage.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }else{
                            Toast.makeText(LoginPage.this,"Document does not exist",Toast.LENGTH_LONG).show();
                            loginButton.setVisibility(View.VISIBLE);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginPage.this,"Error!!",Toast.LENGTH_LONG).show();
                    }
                });
    }

}