package com.run.ultimate_fitness.ui.workouts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.run.ultimate_fitness.R;

public class GymWorkouts extends AppCompatActivity{

    private ImageView profilePicImage;
    private TextView userName;
    private ListView listView;

    public static final String PICTURE ="picture";
    public static final String FIRST_NAME ="firstName";
    public static final String LAST_NAME ="lastName";
    public static final String USER_PREFS ="userPrefs";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_workouts);

        getSupportActionBar().hide();

        profilePicImage = findViewById(R.id.icon_user);
        userName = findViewById(R.id.txtUsername);
        loadImage();


    }

    public  void loadImage(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);
        String picture = sharedPreferences.getString(PICTURE,"");
        String firstName = sharedPreferences.getString(FIRST_NAME,"");
        String lastName = sharedPreferences.getString(LAST_NAME,"");


        userName.setText(firstName + " " + lastName);

        profilePicImage.setImageBitmap(StringToBitMap(picture));
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