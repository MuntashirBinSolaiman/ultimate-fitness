package com.run.ultimate_fitness.ui.workouts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.run.ultimate_fitness.R;
import com.run.ultimate_fitness.adapters.GymWorkoutsAdapter;

import java.util.ArrayList;

public class GymWorkouts extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_workouts);
        getSupportActionBar().hide();


        listView = findViewById(R.id.listGymWorkouts);

        //Create Data

        ArrayList<WorkoutsModel> arrayList = new ArrayList<>();
        //arrayList.add(new WorkoutsModel("Push Ups", "Tricep", R.drawable.img_home_workouts,"Sets/reps for results: Aim for three sets of 10–15 reps, and try adding them into your workouts 2–3 times a week to add muscle definition to your arms and build strength."));
        //arrayList.add(new workoutsModel("Push Ups", "Tricep", R.drawable.home_workouts, "Sets/reps for results: Aim for three sets of 10–15 reps, and try adding them into your workouts 2–3 times a week to add muscle definition to your arms and build strength."));
        //arrayList.add(new WorkoutsModel("Push Ups", "Tricep", R.drawable.ombati, "Sets/reps for results: Aim for three sets of 10–15 reps, and try adding them into your workouts 2–3 times a week to add muscle definition to your arms and build strength."));



        GymWorkoutsAdapter gymWorkoutsAdapter = new GymWorkoutsAdapter(this, R.layout.gym_workout_item,arrayList);

        listView.setAdapter(gymWorkoutsAdapter);
    }
}