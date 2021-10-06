package com.run.ultimate_fitness.ui.inbox;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CalendarView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.run.ultimate_fitness.MainActivity;
import com.run.ultimate_fitness.R;
import com.run.ultimate_fitness.WebPage;
import com.run.ultimate_fitness.ui.workouts.WorkoutsViewModel;

import co.intentservice.chatui.ChatView;

public class InboxFragment extends Fragment {


    private InboxViewModel mViewModel;

    private CardView conversatonsCardView;

    public static InboxFragment newInstance() {
        return new InboxFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);


        conversatonsCardView = view.findViewById(R.id.conversatonsCardView);
        conversatonsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ChatPage.class);
                startActivity(intent);
            }
        });

        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(InboxViewModel.class);
        // TODO: Use the ViewModel
    }



}