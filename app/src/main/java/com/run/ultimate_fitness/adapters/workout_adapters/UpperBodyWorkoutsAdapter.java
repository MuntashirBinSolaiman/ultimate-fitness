package com.run.ultimate_fitness.adapters.workout_adapters;

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

public class UpperBodyWorkoutsAdapter extends RecyclerView.Adapter<UpperBodyWorkoutsAdapter.ViewHolder> {

    List<WorkoutsModel>workoutsList1;
    private OnWorkoutListener mOnWorkoutListener;

    public UpperBodyWorkoutsAdapter(List<WorkoutsModel> workoutsList, OnWorkoutListener onWorkoutListener){

        this.workoutsList1 = workoutsList;
        this.mOnWorkoutListener = onWorkoutListener;
    }



    @NonNull
    @Override
    public UpperBodyWorkoutsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workouts_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, mOnWorkoutListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UpperBodyWorkoutsAdapter.ViewHolder holder, int position) {

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

            onWorkoutListener.onUpperBodyWorkoutClick(getAbsoluteAdapterPosition());

        }
    }

    public interface OnWorkoutListener{
        void onUpperBodyWorkoutClick(int position);
    }
}
