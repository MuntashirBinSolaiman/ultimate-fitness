package com.run.ultimate_fitness.ui.inbox;

import static android.content.ContentValues.TAG;

import static com.run.ultimate_fitness.utils.Constants.UID;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.UsersRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.run.ultimate_fitness.R;
import com.run.ultimate_fitness.utils.Constants;

public class InboxFragment extends Fragment {

    private InboxViewModel mViewModel;



    public static InboxFragment newInstance() {
        return new InboxFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);


        chatLogin();
        UsersRequest usersRequest = new UsersRequest.UsersRequestBuilder().setLimit(30).build();

        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(InboxViewModel.class);
        // TODO: Use the ViewModel
    }

    public void chatLogin() {

        if (CometChat.getLoggedInUser() == null) {
            CometChat.login(UID, Constants.AUTH_KEY, new CometChat.CallbackListener<com.cometchat.pro.models.User>() {

                @Override
                public void onSuccess(User user) {
                    Log.d(TAG, "Login Successful : " + user.toString());
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "Login failed with exception: " + e.getMessage());
                }
            });
        } else {
            // User already logged in
        }
    }

}