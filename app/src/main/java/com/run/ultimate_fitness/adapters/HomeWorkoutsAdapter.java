package com.run.ultimate_fitness.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.run.ultimate_fitness.R;
import com.run.ultimate_fitness.ui.workouts.workoutsModel;

import java.util.List;

public class HomeWorkoutsAdapter extends RecyclerView.Adapter<HomeWorkoutsAdapter.ViewHolder> {

    List<workoutsModel>workoutsList1;

    public HomeWorkoutsAdapter(List<workoutsModel> workoutsList){

        this.workoutsList1 = workoutsList;
    }



    @NonNull
    @Override
    public HomeWorkoutsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workouts_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeWorkoutsAdapter.ViewHolder holder, int position) {

        holder.workoutImage.setImageResource(workoutsList1.get(position).getWorkout_image());
        holder.txtWorkoutName.setText(workoutsList1.get(position).getWorkout_name());
        holder.txtWorkoutZone.setText(workoutsList1.get(position).getWorkout_zone());

    }

    @Override
    public int getItemCount() {
        return workoutsList1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView workoutImage;
        TextView txtWorkoutName;
        TextView txtWorkoutZone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            workoutImage = itemView.findViewById(R.id.img_workout);
            txtWorkoutName = itemView.findViewById(R.id.txtWorkoutName);
            txtWorkoutZone = itemView.findViewById(R.id.txtWorkoutZone);

        }
    }
}
