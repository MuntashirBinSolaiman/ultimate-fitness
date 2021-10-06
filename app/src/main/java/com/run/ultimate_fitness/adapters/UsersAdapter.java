package com.run.ultimate_fitness.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.models.Group;
import com.run.ultimate_fitness.R;
import com.run.ultimate_fitness.User;

import com.cometchat.pro.*;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    List<User>users;
    private Context context;

    public UsersAdapter(List<User> users, Context context){

        this.users = users;
        this.context = context;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //holder.txtWorkoutZone.setText(workoutsList1.get(position).getWorkout_zone());
        //holder.bind(users.get(position));



    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView userNameTextView;
        LinearLayout containerLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            containerLayout = itemView.findViewById(R.id.containerLayout);

        }

        public void bind(com.cometchat.pro.models.User user) {
            userNameTextView.setText(user.getName());
            //containerLayout.setOnClickListener(view -> Chat);
        }
    }
}
