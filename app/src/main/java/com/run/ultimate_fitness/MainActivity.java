package com.run.ultimate_fitness;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.run.ultimate_fitness.databinding.ActivityMainBinding;
import com.run.ultimate_fitness.stepCounter.StepCounterActivity;
import com.run.ultimate_fitness.utils.Constants;
import com.run.ultimate_fitness.water.Water_Tracker_Activity;
import  com.run.ultimate_fitness.ui.workouts.*;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ImageView profilePicImage;
    private TextView userName;

    public static final String IS_LOGGED_IN ="isLoggedIn";
    public static final String PICTURE ="picture";

    public static final String USER_PREFS ="userPrefs";
    public static final String CREDENTIALS_PREFS = "credentials";
    public static final String USER_UID = "uid";


    String UID1 = "user1"; // Replace with the UID of the user to login


    private String picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        SharedPreferences sharedPreferences2 = getSharedPreferences(CREDENTIALS_PREFS, MODE_PRIVATE);


        boolean loggedIn = sharedPreferences.getBoolean(IS_LOGGED_IN,false);
        if (!loggedIn) {
            Intent intent = new Intent(MainActivity.this, LoginPage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_nutrition, R.id.navigation_workouts,
                R.id.navigation_inbox)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        getSupportActionBar().hide();

        profilePicImage = findViewById(R.id.icon_user);
        loadImage();

        UID1 = sharedPreferences2.getString(USER_UID, ""); // Replace with the UID of the user to login
    }

    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    public void goToProfile(View view){
            Intent intent = new Intent(MainActivity.this, ProfilePage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    public void goToWaterTracker(View view) {
        Intent intent = new Intent(MainActivity.this, Water_Tracker_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void goToStepCounter(View view){
           Intent intent = new Intent(MainActivity.this, StepCounterActivity.class);
           startActivity(intent);

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


    public  void loadImage(){
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);
            picture = sharedPreferences.getString(PICTURE,"");
        profilePicImage.setImageBitmap(StringToBitMap(picture));

        }

}
