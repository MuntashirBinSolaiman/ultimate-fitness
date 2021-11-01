package com.run.ultimate_fitness.ui.workouts;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.run.ultimate_fitness.R;
import com.run.ultimate_fitness.adapters.HomeWorkoutsAdapter;
import com.run.ultimate_fitness.ui.inbox.ChatPage;

import java.util.ArrayList;
import java.util.List;

public class WorkoutsFragment extends Fragment implements HomeWorkoutsAdapter.OnWorkoutListener {

    RecyclerView recyclerView, recyclerView2;
    List<workoutsModel> homeWorkoutsList;
    private ImageView profilePicImage, imgGymWorkouts;
    private TextView userName;

    public static final String PICTURE ="picture";
    public static final String FIRST_NAME ="firstName";
    public static final String LAST_NAME ="lastName";
    public static final String USER_PREFS ="userPrefs";

    private WorkoutsViewModel mViewModel;

    public static WorkoutsFragment newInstance() {
        return new WorkoutsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workouts, container, false);

        recyclerView = view.findViewById(R.id.Rvhome_workouts);
        recyclerView2 = view.findViewById(R.id.RvGym_workouts2);

        profilePicImage = view.findViewById(R.id.icon_user);

        userName = view.findViewById(R.id.txtUsername);




        loadImage();

        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        //initData();
        recyclerView.setAdapter(new HomeWorkoutsAdapter(initData(), this));
        recyclerView2.setAdapter(new HomeWorkoutsAdapter(initData(), this));


        return view;
    }

    private List<workoutsModel> initData() {

        homeWorkoutsList = new ArrayList<>();

        homeWorkoutsList.add(new workoutsModel("Tricep Dips", "Tricep", R.drawable.home_workout_tricep_dips, "Sets/reps for results: Aim for three sets of 10–15 reps, and try adding them into your workouts 2–3 times a week to add muscle definition to your arms and build strength."));

        homeWorkoutsList.add(new workoutsModel("Jump Squats", "Legs", R.drawable.home_workout_jump_sqauts, "Sets/reps for results: Aim for three sets of 10–15 reps, and try adding them into your workouts 2–3 times a week to add muscle definition to your arms and build strength."));
        homeWorkoutsList.add(new workoutsModel("Split Squats", "Legs", R.drawable.home_workout_split_squats,"Sets/reps for results: Aim for three sets of 10–15 reps, and try adding them into your workouts 2–3 times a week to add muscle definition to your arms and build strength."));
        homeWorkoutsList.add(new workoutsModel("Forward Box Squat Lunges", "Legs", R.drawable.img_home_workouts,"Sets/reps for results: Aim for three sets of 10–15 reps, and try adding them into your workouts 2–3 times a week to add muscle definition to your arms and build strength."));
        homeWorkoutsList.add(new workoutsModel("Calf Raises", "Legs", R.drawable.img_home_workouts, "Sets/reps for results: Aim for three sets of 10–15 reps, and try adding them into your workouts 2–3 times a week to add muscle definition to your arms and build strength."));

        homeWorkoutsList.add(new workoutsModel("Chest Incline", "Chest", R.drawable.img_home_workouts, "Sets/reps for results: Aim for three sets of 10–15 reps, and try adding them into your workouts 2–3 times a week to add muscle definition to your arms and build strength."));
        homeWorkoutsList.add(new workoutsModel("Chest Decline", "Chest", R.drawable.img_home_workouts, "Sets/reps for results: Aim for three sets of 10–15 reps, and try adding them into your workouts 2–3 times a week to add muscle definition to your arms and build strength."));
        homeWorkoutsList.add(new workoutsModel("Chest Standard", "Chest", R.drawable.img_home_workouts, "Sets/reps for results: Aim for three sets of 10–15 reps, and try adding them into your workouts 2–3 times a week to add muscle definition to your arms and build strength."));
        homeWorkoutsList.add(new workoutsModel("Diamonds", "Chest", R.drawable.img_home_workouts, "Sets/reps for results: Aim for three sets of 10–15 reps, and try adding them into your workouts 2–3 times a week to add muscle definition to your arms and build strength."));
        homeWorkoutsList.add(new workoutsModel("Zig-Zag", "Chest", R.drawable.img_home_workouts, "Sets/reps for results: Aim for three sets of 10–15 reps, and try adding them into your workouts 2–3 times a week to add muscle definition to your arms and build strength."));

        homeWorkoutsList.add(new workoutsModel("Pull-Ups", "Back", R.drawable.img_home_workouts, "Sets/reps for results: Aim for three sets of 10–15 reps, and try adding them into your workouts 2–3 times a week to add muscle definition to your arms and build strength."));


        return homeWorkoutsList;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(WorkoutsViewModel.class);
        // TODO: Use the ViewModel
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
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);
        String picture = sharedPreferences.getString(PICTURE,"");
        String firstName = sharedPreferences.getString(FIRST_NAME,"");
        String lastName = sharedPreferences.getString(LAST_NAME,"");


        userName.setText("WORKOUTS");

        profilePicImage.setImageBitmap(StringToBitMap(picture));
    }

    @Override
    public void onWorkoutClick(int position) {

        Log.d(TAG, "Clicked Clicked");
        Intent intent = new Intent(getContext(), WorkoutPage.class);

        intent.putExtra("workout_name", homeWorkoutsList.get(position).getWorkout_name());
        intent.putExtra("workout_zone", homeWorkoutsList.get(position).getWorkout_zone());
        intent.putExtra("workout_image", homeWorkoutsList.get(position).getWorkout_image());
        intent.putExtra("workout_description", homeWorkoutsList.get(position).getWorkout_description());

        startActivity(intent);
    }
}