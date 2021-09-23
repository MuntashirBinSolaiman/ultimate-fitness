package com.run.ultimate_fitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfilePage extends AppCompatActivity {

    TextView logoutText, firstNameTextView,lastNameTextView, phoneNumberTextView,heightTextView,weightTextView, fullNameTextView;
    private ImageView backButtonImage, displayImage;

    public static final String USER_PREFS ="userPrefs";
    public static final String FIRST_NAME ="firstName";
    public static final String LAST_NAME ="lastName";
    public static final String PHONE_NUMBER ="phoneNumber";
    public static final String WEIGHT ="weight";
    public static final String HEIGHT ="height";
    public static final String PICTURE ="picture";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        logoutText = findViewById(R.id.toolbarLogoutTextView);
        displayImage = findViewById(R.id.profilePicture);
        firstNameTextView = findViewById(R.id.firstNameProfileTextView);
        lastNameTextView = findViewById(R.id.lastNameProfileTextView);
        fullNameTextView = findViewById(R.id.fullnameProfileTextView);
        phoneNumberTextView = findViewById(R.id.phoneNumberProfileTextView);
        heightTextView = findViewById(R.id.heightProfileTextView);
        weightTextView = findViewById(R.id.weightProfileTextView);
        backButtonImage =findViewById(R.id.toolbarBackButton);

        loadValues();

        logoutText.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();


            Intent intent = new Intent(ProfilePage.this, LoginPage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            clearData();
            startActivity(intent);
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        backButtonImage.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilePage.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    public void editProfileButton(View view){
        Intent intent = new Intent(this, EditProfile.class);
        startActivity(intent);
    }

    public void changePasswordsButton(View view){
        Intent intent = new Intent(this, ChangePassword.class);
        startActivity(intent);
    }

    public void deleteProfileButton(View view){
        deleteProfile();
    }

    private void loadValues(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);

        String firstName = sharedPreferences.getString(FIRST_NAME,"");
        String lastName = sharedPreferences.getString(LAST_NAME,"");
        String phoneNumber = sharedPreferences.getString(PHONE_NUMBER,"");
        String weight = sharedPreferences.getString(WEIGHT,"");
        String height = sharedPreferences.getString(HEIGHT,"");
        String picture = sharedPreferences.getString(PICTURE,"");
        String fullName = firstName +" "+ lastName;

        firstNameTextView.setText(firstName);
        lastNameTextView.setText(lastName);
        fullNameTextView.setText(fullName);
        phoneNumberTextView.setText(phoneNumber);
        heightTextView.setText(height);
        weightTextView.setText(weight);
        displayImage.setImageBitmap(StringToBitMap(picture));
    }

    private void clearData(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private void deleteProfile(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(ProfilePage.this,"Profile deleted successfully", Toast.LENGTH_LONG).show();
                                                clearData();
                                                Intent intent = new Intent(ProfilePage.this, LoginPage.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);

                                            }else{
                                                Toast.makeText(ProfilePage.this,"Failed to delete profile please check internet connection", Toast.LENGTH_LONG).show();
                                                //progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        }else{
                            Toast.makeText(ProfilePage.this,"Failed to delete data!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}

