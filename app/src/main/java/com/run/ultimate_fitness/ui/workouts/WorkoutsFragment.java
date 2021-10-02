package com.run.ultimate_fitness.ui.workouts;

import static android.content.Context.MODE_PRIVATE;

import androidx.lifecycle.ViewModelProvider;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.run.ultimate_fitness.R;
import com.run.ultimate_fitness.adapters.HomeWorkoutsAdapter;

import java.util.ArrayList;
import java.util.List;

public class WorkoutsFragment extends Fragment {

    RecyclerView recyclerView, recyclerView2;
    List<workoutsModel> workoutsList;
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
        recyclerView.setAdapter(new HomeWorkoutsAdapter(initData()));
        recyclerView2.setAdapter(new HomeWorkoutsAdapter(initData()));


        return view;
    }

    private List<workoutsModel> initData() {

        workoutsList = new ArrayList<>();

        workoutsList.add(new workoutsModel("Tricep Dips", "Tricep", R.drawable.img_home_workouts));

        workoutsList.add(new workoutsModel("Jump Squats", "Legs", R.drawable.img_home_workouts));
        workoutsList.add(new workoutsModel("Split Squats", "Legs", R.drawable.img_home_workouts));
        workoutsList.add(new workoutsModel("Forward Box Squat Lunges", "Legs", R.drawable.img_home_workouts));
        workoutsList.add(new workoutsModel("Reverse Box Squat Lunge", "Legs", R.drawable.img_home_workouts));
        workoutsList.add(new workoutsModel("Calf Raises", "Legs", R.drawable.img_home_workouts));

        workoutsList.add(new workoutsModel("Chest Incline", "Chest", R.drawable.img_home_workouts));
        workoutsList.add(new workoutsModel("Chest Decline", "Chest", R.drawable.img_home_workouts));
        workoutsList.add(new workoutsModel("Chest Standard", "Chest", R.drawable.img_home_workouts));
        workoutsList.add(new workoutsModel("Diamonds", "Chest", R.drawable.img_home_workouts));
        workoutsList.add(new workoutsModel("Zig-Zag", "Chest", R.drawable.img_home_workouts));

        workoutsList.add(new workoutsModel("Pull-Ups", "Back", R.drawable.img_home_workouts));


        return workoutsList;
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


        userName.setText("Welcome, " + firstName);

        profilePicImage.setImageBitmap(StringToBitMap(picture));
    }

}