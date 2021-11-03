package com.run.ultimate_fitness.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.run.ultimate_fitness.R;
import com.run.ultimate_fitness.ui.workouts.WorkoutsModel;

import java.util.List;

public class HomeWorkoutsAdapter extends RecyclerView.Adapter<HomeWorkoutsAdapter.ViewHolder> {

    List<WorkoutsModel>workoutsList1;
    private OnWorkoutListener mOnWorkoutListener;

    public HomeWorkoutsAdapter(List<WorkoutsModel> workoutsList, OnWorkoutListener onWorkoutListener){

        this.workoutsList1 = workoutsList;
        this.mOnWorkoutListener = onWorkoutListener;
    }



    @NonNull
    @Override
    public HomeWorkoutsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workouts_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, mOnWorkoutListener);
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnWorkoutListener onWorkoutListener;

        ImageView workoutImage;
        TextView txtWorkoutName;
        TextView txtWorkoutZone;

        public ViewHolder(@NonNull View itemView, OnWorkoutListener onWorkoutListener) {
            super(itemView);

            workoutImage = itemView.findViewById(R.id.img_workout);
            txtWorkoutName = itemView.findViewById(R.id.txtWorkoutName);
            txtWorkoutZone = itemView.findViewById(R.id.txtWorkoutZone);
            this.onWorkoutListener = onWorkoutListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            onWorkoutListener.onWorkoutClick(getAbsoluteAdapterPosition());

        }
    }

    public interface OnWorkoutListener{
        void onWorkoutClick(int position);
    }
}
