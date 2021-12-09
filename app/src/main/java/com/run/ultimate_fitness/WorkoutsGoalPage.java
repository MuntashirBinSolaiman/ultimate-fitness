package com.run.ultimate_fitness;

import static android.content.ContentValues.TAG;
import static com.run.ultimate_fitness.utils.Constants.UID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    private TextView txtNext, txtBack;
    int layoutsMove = 1;

    public String[] stepsValueSet, waterValueSet, caloriesValueSet;


    private Button summerBodyBtn, gainMuscleBtn, loseWeightBtn, loseQuickWeightBtn;

    String workoutGoalFinal;
    public int waterGoalFinal, caloriesGoalFinal, stepGoalFinal;

    private RelativeLayout fitnessLayout, stepsLayout, waterLayout, caloriesLayout;

    public static final String USER_PREFS = "userPrefs";
    public static final String GOALS_PREFS = "goalsPrefs";

    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";

    private static String fullName;


    public static final String WATER_GOAL = "water_goal";
    public static final String STEPS_GOAL = "steps_goal";
    public static final String CALORIES_GOAL = "calories_goal";
    public static final String WORKOUT_GOAL = "workout_goal";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouts_goal_page);
        getSupportActionBar().hide();

        summerBodyBtn = findViewById(R.id.summerBodyBtn);
        gainMuscleBtn = findViewById(R.id.gainMuscleBtn);
        loseWeightBtn = findViewById(R.id.loseWeightBtn);
        loseQuickWeightBtn = findViewById(R.id.loseQuickWeightBtn);


        stepsGoalPicker = findViewById(R.id.pickerStepsGoal);

        waterGoalPicker = findViewById(R.id.pickerWaterGoal);

        caloriesGoalPicker = findViewById(R.id.pickerCaloriesGoal);


        fitnessLayout = findViewById(R.id.layout_fitnessGoal);
        stepsLayout = findViewById(R.id.layout_stepsGoal);
        waterLayout = findViewById(R.id.layout_waterGoal);
        caloriesLayout = findViewById(R.id.layout_caloriesGoal);

        txtNext = findViewById(R.id.txtNext);
        txtBack = findViewById(R.id.txtBack);
        initOnClickListeners();

        initValueSets();

        stepsGoalPicker.setMaxValue(10);
        stepsGoalPicker.setMinValue(0);


        waterGoalPicker.setMaxValue(13);
        waterGoalPicker.setMinValue(0);
        waterGoalPicker.setValue(0);

        caloriesGoalPicker.setMaxValue(24);
        caloriesGoalPicker.setMinValue(0);


        stepsGoalPicker.setDisplayedValues(stepsValueSet);
        caloriesGoalPicker.setDisplayedValues(caloriesValueSet);
        waterGoalPicker.setDisplayedValues(waterValueSet);

        waterLayout.setTranslationX(650);
        stepsLayout.setTranslationX(650);
        caloriesLayout.setTranslationX(650);


        stepsGoalPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                //stepsGoalPicker.setValue((newVal < oldVal )? oldVal - 1000: oldVal + 1000);
                stepGoalFinal = Integer.parseInt(stepsValueSet[newVal]);
            }
        });

        waterGoalPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                waterGoalFinal = Integer.parseInt(waterValueSet[newVal]);

            }
        });


        caloriesGoalPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                caloriesGoalFinal = Integer.parseInt(caloriesValueSet[newVal]);
            }
        });

        moveLayouts();


    }

    private void initValueSets() {
        stepsValueSet = new String[11];
        for (int s = 0; s <= 10000; s += 1000) {
            stepsValueSet[s / 1000] = String.valueOf(s);
        }

        waterValueSet = new String[14];
        for (int w = 0; w <= 13; w += 1) {
            waterValueSet[w] = String.valueOf(w);
        }

        caloriesValueSet = new String[26];
        for (int c = 0; c <= 2500; c += 100) {
            caloriesValueSet[(c / 100)] = String.valueOf(c);
        }
    }


    @Override
    public void onBackPressed() {

    }

    private void moveLayouts() {
        switch (layoutsMove) {

            //display the fitness goals
            case 1:
                fitnessLayout.animate().translationX(0);
                fitnessLayout.animate().alpha(1.0F);


                waterLayout.animate().translationX(650);
                waterLayout.animate().alpha(0.0F);

                stepsLayout.animate().translationX(650);
                stepsLayout.animate().alpha(0.0F);

                caloriesLayout.animate().translationX(650);
                caloriesLayout.animate().alpha(0.0F);


                txtBack.animate().alpha(0.0F);
                break;

            //display the water goals

            case 2:
                fitnessLayout.animate().translationX(-650);
                //fitnessLayout.setVisibility(View.INVISIBLE);
                fitnessLayout.animate().alpha(0.0F);

                waterLayout.animate().translationX(0);
                waterLayout.animate().alpha(1.0F);

                stepsLayout.animate().translationX(650);
                stepsLayout.animate().alpha(0.0F);

                caloriesLayout.animate().translationX(650);
                caloriesLayout.animate().alpha(0.0F);


                txtBack.animate().alpha(1.0F);

                txtNext.setText("Next");

                break;
            //steps the fitness goals

            case 3:
                fitnessLayout.animate().translationX(-650);
                fitnessLayout.animate().alpha(0.0F);

                waterLayout.animate().translationX(-650);
                waterLayout.animate().alpha(0.0F);

                stepsLayout.animate().translationX(0);
                stepsLayout.animate().alpha(1.0F);

                caloriesLayout.animate().translationX(0);
                caloriesLayout.animate().alpha(1.0F);

                txtNext.setText("Finish");


                break;
            //display the calories goals

            //save the goals and go to the home page

            case 4:

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(GOALS_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(WATER_GOAL, waterGoalFinal);
                editor.putInt(CALORIES_GOAL, caloriesGoalFinal);
                editor.putInt(STEPS_GOAL, stepGoalFinal);
                editor.putString(WORKOUT_GOAL, workoutGoalFinal);
                System.out.println(workoutGoalFinal);
                editor.apply();

                Intent intent = new Intent(WorkoutsGoalPage.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                break;
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


    public void clickSummerBody(View view) {
        workoutGoalFinal = "Summer Body";

        summerBodyBtn.setBackgroundResource(R.drawable.btn_red);
        gainMuscleBtn.setBackgroundResource(R.drawable.btn_black);
        loseWeightBtn.setBackgroundResource(R.drawable.btn_black);
        loseQuickWeightBtn.setBackgroundResource(R.drawable.btn_black);

    }

    public void clickGainMuscle(View view) {
        workoutGoalFinal = "Gain Muscle";


        summerBodyBtn.setBackgroundResource(R.drawable.btn_black);
        gainMuscleBtn.setBackgroundResource(R.drawable.btn_red);
        loseWeightBtn.setBackgroundResource(R.drawable.btn_black);
        loseQuickWeightBtn.setBackgroundResource(R.drawable.btn_black);

    }

    public void clickLoseWeight(View view) {
        workoutGoalFinal = "Lose Weight";

        summerBodyBtn.setBackgroundResource(R.drawable.btn_black);
        gainMuscleBtn.setBackgroundResource(R.drawable.btn_black);
        loseWeightBtn.setBackgroundResource(R.drawable.btn_red);
        loseQuickWeightBtn.setBackgroundResource(R.drawable.btn_black);

    }

    public void clickLoseQuickWeight(View view) {
        workoutGoalFinal = "Lose Quick Weight";

        summerBodyBtn.setBackgroundResource(R.drawable.btn_black);
        gainMuscleBtn.setBackgroundResource(R.drawable.btn_black);
        loseWeightBtn.setBackgroundResource(R.drawable.btn_black);
        loseQuickWeightBtn.setBackgroundResource(R.drawable.btn_red);

    }
}

