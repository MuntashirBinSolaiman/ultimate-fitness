package com.run.ultimate_fitness;

import static android.content.ContentValues.TAG;

import static com.run.ultimate_fitness.utils.Constants.UID;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import com.run.ultimate_fitness.databinding.ActivityMainBinding;
import com.run.ultimate_fitness.utils.Constants;
import com.run.ultimate_fitness.water.Water_Tracker_Activity;
import  com.run.ultimate_fitness.ui.workouts.*;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ImageView profilePicImage;
    private TextView userName;

    public static final String IS_LOGGED_IN ="isLoggedIn";
    public static final String PICTURE ="picture";

    public static final String USER_PREFS ="userPrefs";



    /*Workouts Tab*/
    private RecyclerView homeWorkouts;
    private RecyclerView.Adapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        boolean loggedIn = sharedPreferences.getBoolean(IS_LOGGED_IN,false);
        if (!loggedIn) {
            Intent intent = new Intent(MainActivity.this, LoginPage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

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





        /*Food Database stuff----------------------------------------------------------------*/

        /*Stetho-------------------------------------------------*/
        Stetho.initializeWithDefaults(this);

        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        
        initChat();

    }

    private void initChat() {

        AppSettings appSettings=new AppSettings.AppSettingsBuilder().subscribePresenceForAllUsers().setRegion(Constants.REGION).build();

        CometChat.init(this, Constants.APP_ID,appSettings, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String successMessage) {
            }
            @Override
            public void onError(CometChatException e) {
            }
        });
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
           Intent intent = new Intent(MainActivity.this, StepCounter.class);
           startActivity(intent);

        }

    public void goToGymWorkouts(View view){
        Intent intent = new Intent(MainActivity.this, GymWorkouts.class);
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
            String picture = sharedPreferences.getString(PICTURE,"");
        profilePicImage.setImageBitmap(StringToBitMap(picture));

        }

        public void goToBookings(View view){
            Intent intent = new Intent(MainActivity.this, WebPage.class);
            startActivity(intent);
        }



}
