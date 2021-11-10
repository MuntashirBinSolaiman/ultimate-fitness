package com.run.ultimate_fitness.adapters.workout_adapters;

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
import com.run.ultimate_fitness.ui.inbox.InboxModel;

import java.util.ArrayList;

public class InboxAdapter extends ArrayAdapter<InboxModel> {

    private Context mContext;
    private int mResource;

    public InboxAdapter(@NonNull Context context, int resource, @NonNull ArrayList<InboxModel> objects) {
        super(context, resource, objects);

        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        convertView = layoutInflater.inflate(mResource,parent,false);

        ImageView chatImage = convertView.findViewById(R.id.avatarImageView);
        TextView chatName = convertView.findViewById(R.id.userNameTextView);
        TextView chatMessage = convertView.findViewById(R.id.messageTextView);



        chatName.setText(getItem(position).getName());
        chatMessage.setText(getItem(position).getMessage());
        chatImage.setImageBitmap(getItem(position).getImage());




        return convertView;
    }
}
