package com.run.ultimate_fitness.ui.workouts;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.run.ultimate_fitness.R;
import com.run.ultimate_fitness.adapters.workoutsAdapter;

import java.util.ArrayList;
import java.util.List;

public class WorkoutsFragment extends Fragment {

    RecyclerView recyclerView;
    List<workoutsModel> workoutsList;

    private WorkoutsViewModel mViewModel;

    public static WorkoutsFragment newInstance() {
        return new WorkoutsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workouts, container, false);

        recyclerView = view.findViewById(R.id.Rvhome_workouts);

        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        //initData();
        recyclerView.setAdapter(new workoutsAdapter(initData()));

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

}