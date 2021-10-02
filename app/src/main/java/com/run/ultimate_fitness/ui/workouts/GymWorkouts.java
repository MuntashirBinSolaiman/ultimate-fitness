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

        ArrayList<workoutsModel> arrayList = new ArrayList<>();
        arrayList.add(new workoutsModel("Push Ups", "Tricep", R.drawable.img_home_workouts));
        arrayList.add(new workoutsModel("Push Ups", "Tricep", R.drawable.home_workouts));
        arrayList.add(new workoutsModel("Push Ups", "Tricep", R.drawable.ombati));



        GymWorkoutsAdapter gymWorkoutsAdapter = new GymWorkoutsAdapter(this, R.layout.gym_workout_item,arrayList);

        listView.setAdapter(gymWorkoutsAdapter);
    }
}