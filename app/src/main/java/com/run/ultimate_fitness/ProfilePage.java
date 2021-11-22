package com.run.ultimate_fitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.run.ultimate_fitness.utils.Constants;

public class ProfilePage extends AppCompatActivity {

    TextView logoutText, firstNameTextView,lastNameTextView, phoneNumberTextView,heightTextView,weightTextView, fullNameTextView, deleteBtnTextView;
    private ImageView backButtonImage, displayImage;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public static final String CREDENTIALS_PREFS = "credentials";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";

    public static final String USER_UID = "uid";

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

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        logoutText = findViewById(R.id.toolbarLogoutTextView);
        displayImage = findViewById(R.id.profilePicture);
        firstNameTextView = findViewById(R.id.firstNameProfileTextView);
        lastNameTextView = findViewById(R.id.lastNameProfileTextView);
        fullNameTextView = findViewById(R.id.fullnameProfileTextView);
        phoneNumberTextView = findViewById(R.id.phoneNumberProfileTextView);
        heightTextView = findViewById(R.id.heightProfileTextView);
        weightTextView = findViewById(R.id.weightProfileTextView);
        backButtonImage = findViewById(R.id.toolbarBackButton);
        deleteBtnTextView = findViewById(R.id.deleteBtn);

        protectUserAccount();

        loadValues();

        logoutText.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            clearData();
            clearAuth();

            Intent intent = new Intent(ProfilePage.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        backButtonImage.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilePage.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void protectUserAccount(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(CREDENTIALS_PREFS,MODE_PRIVATE);

        String checkUserRights = sharedPreferences.getString(USER_UID, "");

        if(checkUserRights.equals(Constants.MASTER_UID)){
            deleteBtnTextView.setVisibility(View.GONE);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void editProfileButton(View view){
        Intent intent = new Intent(this, EditProfile.class);
        startActivity(intent);
    }

    public void changePasswordsButton(View view){
        Intent intent = new Intent(this, ChangePassword.class);
        startActivity(intent);
    }

    //Deletes user from device and firestore database
    public void deleteProfileButton(View view){

        if (isNetworkAvailable())
        {
            //deleteProfile();

            new AlertDialog.Builder(this)
                    .setTitle("Delete Profile")
                    .setMessage("Are you sure you want to delete your profile?")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                            deleteProfile();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    //.setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }else{

            new AlertDialog.Builder(this)
                    .setTitle("Check Internet connection")
                    .setMessage("Please make sure you have an active internet connection")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, null)
                    .show();
        }


    }

    // Loads values from shared preferences
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
        heightTextView.setText(height + "m");
        weightTextView.setText(weight + "Kgs");
        displayImage.setImageBitmap(StringToBitMap(picture));
    }

    //Clears shred preferences
    private void clearData(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private void clearRTDB(){

        String temp_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance("https://ultimate-storm-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference()
                .child("users")
                .child(temp_uid)
                .removeValue();

    }

    private void clearAuth(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(CREDENTIALS_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    //Connects to the database
    private void deleteProfile(){
        SharedPreferences sharedPreferences = getSharedPreferences(CREDENTIALS_PREFS,MODE_PRIVATE);

        String email = sharedPreferences.getString(EMAIL,"");
        String password = sharedPreferences.getString(PASSWORD,"");

        AuthCredential credential = EmailAuthProvider.getCredential(email,password);

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Users")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    clearRTDB();
                                    user.delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(ProfilePage.this,"Profile deleted successfully", Toast.LENGTH_LONG).show();
                                                        clearData();
                                                        clearAuth();
                                                        Intent intent = new Intent(ProfilePage.this, MainActivity.class);
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
        });
    }

    //Converst string to bitmap
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

