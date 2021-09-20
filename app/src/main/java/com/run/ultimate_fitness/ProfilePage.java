package com.run.ultimate_fitness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ProfilePage extends AppCompatActivity {

    TextView logoutText, firstNameTextView,lastNameTextView, phoneNumberTextView,heightTextView,weightTextView, fullnameTextView;

    public static final String USER_PREFS ="userPrefs";
    public static final String FIRST_NAME ="firstName";
    public static final String LAST_NAME ="lastName";
    public static final String PHONE_NUMBER ="phoneNumber";
    public static final String WEIGHT ="weight";
    public static final String HEIGHT ="height";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        logoutText = findViewById(R.id.toolbarLogoutTextView);
        firstNameTextView = findViewById(R.id.firstNameProfileTextView);
        lastNameTextView = findViewById(R.id.lastNameProfileTextView);
        fullnameTextView = findViewById(R.id.fullnameProfileTextView);
        phoneNumberTextView = findViewById(R.id.phoneNumberProfileTextView);
        heightTextView = findViewById(R.id.heightProfileTextView);
        weightTextView = findViewById(R.id.weightProfileTextView);

        loadValues();

        logoutText.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(ProfilePage.this, LoginPage.class);
            startActivity(intent);
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    public void editProfileButton(View view){
        Intent intent = new Intent(this, EditProfile.class);
        startActivity(intent);
    }

    public  void changePasswordsButton(View view){
        Intent intent = new Intent(this, ChangePassword.class);
        startActivity(intent);

    }

    public  void deleteProfileButton(View view){
        //Intent intent = new Intent(this, LoginPage.class);
        //startActivity(intent);
    }

    public void loadValues(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);

        String firstName = sharedPreferences.getString(FIRST_NAME,"");
        String lastName = sharedPreferences.getString(LAST_NAME,"");
        String phoneNumber = sharedPreferences.getString(PHONE_NUMBER,"");
        String weight = sharedPreferences.getString(WEIGHT,"");
        String height = sharedPreferences.getString(HEIGHT,"");
        String fullname = firstName +" "+ lastName;

        firstNameTextView.setText(firstName);
        lastNameTextView.setText(lastName);
        fullnameTextView.setText(fullname);
        phoneNumberTextView.setText(phoneNumber);
        heightTextView.setText(height);
        weightTextView.setText(weight);
    }
}