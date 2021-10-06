package com.run.ultimate_fitness;

import static android.content.ContentValues.TAG;
import static com.run.ultimate_fitness.utils.Constants.UID;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.google.firebase.auth.FirebaseAuth;
import com.run.ultimate_fitness.utils.Constants;
import com.run.ultimate_fitness.water.Water_Tracker_Activity;

import com.cometchat.pro.models.User;


public class WorkoutsGoalPage extends AppCompatActivity {

    private NumberPicker stepsGoalPicker, waterGoalPicker, caloriesGoalPicker;
    String[] stepsGoal, waterGoal;
    private TextView  txtNext, txtBack;
    int layoutsMove = 1;

    String stepGoalFinal, workoutGoalFinal;
    public int waterGoalFinal, caloriesGoalFinal;

    private RelativeLayout fitnessLayout, stepsLayout, waterLayout, caloriesLayout;

    public static final String USER_PREFS ="userPrefs";

    public static final String FIRST_NAME ="firstName";
    public static final String LAST_NAME ="lastName";

    private static  String fullName;


    public static final String WATER_GOAL ="water_goal";
    public static final String STEPS_GOAL ="steps_goal";
    public static final String CALORIES_GOAL ="calories_goal";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouts_goal_page);
        getSupportActionBar().hide();


        stepsGoalPicker = findViewById(R.id.pickerStepsGoal);
        stepsGoal = getResources().getStringArray(R.array.stepsArray);

        waterGoalPicker = findViewById(R.id.pickerWaterGoal);

        caloriesGoalPicker = findViewById(R.id.pickerCaloriesGoal);


        fitnessLayout = findViewById(R.id.layout_fitnessGoal);
        stepsLayout = findViewById(R.id.layout_stepsGoal);
        waterLayout = findViewById(R.id.layout_waterGoal);
        caloriesLayout = findViewById(R.id.layout_caloriesGoal);

        txtNext = findViewById(R.id.txtNext);
        txtBack = findViewById(R.id.txtBack);
        initOnClickListeners();


        stepsGoalPicker.setMaxValue(4);
        stepsGoalPicker.setMinValue(0);

        waterGoalPicker.setMaxValue(13);
        waterGoalPicker.setMinValue(0);

        caloriesGoalPicker.setMaxValue(2500);
        caloriesGoalPicker.setMinValue(1200);



        stepsGoalPicker.setDisplayedValues(stepsGoal);
        waterGoalPicker.setDisplayedValues(waterGoal);

        waterLayout.setTranslationX(650);
        stepsLayout.setTranslationX(650);
        caloriesLayout.setTranslationX(650);




        stepsGoalPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int steps) {
                stepGoalFinal  = stepsGoal[steps];
            }
        });

        waterGoalPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int water) {
                waterGoalFinal = water;

            }
        });


        caloriesGoalPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int calories) {
                caloriesGoalFinal = calories;
            }
        });

        moveLayouts();


        registerChatUser();


    }

    private void registerChatUser() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);
        fullName = sharedPreferences.getString(FIRST_NAME, "0") + " " + sharedPreferences.getString(LAST_NAME, "0");

        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        User user = new User();
        user.setUid(UID); // Replace with the UID for the user to be created
        user.setName(fullName); // Replace with the name of the user


        CometChat.createUser(user, Constants.AUTH_KEY, new CometChat.CallbackListener<User>() {
            @Override
            public void onSuccess(User user) {
                Log.d("createUser", user.toString());
            }

            @Override
            public void onError(CometChatException e) {
                Log.e("createUser", e.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed()
    {

    }

    private void moveLayouts() {
        switch (layoutsMove) {
            case 1:
                fitnessLayout.animate().translationX(0);
                waterLayout.animate().translationX(650);
                stepsLayout.animate().translationX(650);
                caloriesLayout.animate().translationX(650);

                txtBack.setVisibility(View.INVISIBLE);
                break;

            case 2:
                fitnessLayout.animate().translationX(-650);
                waterLayout.animate().translationX(0);
                stepsLayout.animate().translationX(650);
                caloriesLayout.animate().translationX(650);

                txtBack.setVisibility(View.VISIBLE);




                break;

            case 3:
                fitnessLayout.animate().translationX(-650);
                waterLayout.animate().translationX(-650);
                stepsLayout.animate().translationX(0);
                caloriesLayout.animate().translationX(650);
                txtNext.setText("Next");
                System.out.println(waterGoalFinal);


                break;

            case 4:
                fitnessLayout.animate().translationX(-650);
                waterLayout.animate().translationX(-650);
                stepsLayout.animate().translationX(-650);
                caloriesLayout.animate().translationX(0);
                txtNext.setText("Finish");

                break;

            case 5:

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(WATER_GOAL, waterGoalFinal);
                editor.putInt(CALORIES_GOAL, caloriesGoalFinal);
                System.out.println(caloriesGoalFinal);
                editor.putString(STEPS_GOAL, stepGoalFinal);
                editor.apply();


                chatLogin();


                Intent intent = new Intent(WorkoutsGoalPage.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                break;
        }

        }

    private void chatLogin() {

        if (CometChat.getLoggedInUser() == null) {
            CometChat.login(UID, Constants.AUTH_KEY, new CometChat.CallbackListener<User>() {

                @Override
                public void onSuccess(User user) {
                    Log.d(TAG, "Login Successful : " + user.toString());
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "Login failed with exception: " + e.getMessage());
                }
            });
        } else {
            // User already logged in
        }
    }

    private void initOnClickListeners() {
        txtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutsMove++;
                moveLayouts();

            }
        });

        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                layoutsMove--;
                moveLayouts();

            }
        });
    }


    }

