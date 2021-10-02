package com.run.ultimate_fitness.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.run.ultimate_fitness.R;
import com.run.ultimate_fitness.ui.workouts.workoutsModel;

import java.util.ArrayList;

public class GymWorkoutsAdapter extends ArrayAdapter<workoutsModel> {

    private Context mContext;
    private int mResource;

    public GymWorkoutsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<workoutsModel> objects) {
        super(context, resource, objects);

        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        convertView = layoutInflater.inflate(mResource,parent,false);

        ImageView workoutImage = convertView.findViewById(R.id.imgGymWorkouts);
        TextView workoutName = convertView.findViewById(R.id.txtWorkoutName);
        TextView workoutZone = convertView.findViewById(R.id.txtWorkoutZone);


        workoutName.setText(getItem(position).getWorkout_name());
        workoutZone.setText(getItem(position).getWorkout_zone());
        workoutImage.setImageResource(getItem(position).getWorkout_image());



        return convertView;
    }
}
