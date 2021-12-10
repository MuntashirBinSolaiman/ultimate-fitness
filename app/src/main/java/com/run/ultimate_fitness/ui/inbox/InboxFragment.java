package com.run.ultimate_fitness.ui.inbox;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.run.ultimate_fitness.R;
import com.run.ultimate_fitness.WebPage;
import com.run.ultimate_fitness.adapters.workout_adapters.InboxAdapter;
import com.run.ultimate_fitness.ui.workouts.WorkoutPage;
import com.run.ultimate_fitness.utils.Constants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class InboxFragment extends Fragment {

    public static final String PICTURE = "picture";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String USER_PREFS = "userPrefs";

    private ImageView profilePicImage, imgGymWorkouts, bookingImage;
    private TextView userName, lastMessage;
    private ProgressBar progressBar, loadingProressBar;

    public static final String CREDENTIALS_PREFS = "credentials";
    public static final String USER_UID = "uid";

    private CardView conversatonsCardView;
    public ListView chatsListView;
    private String userUID;

    public DatabaseReference root, children;
    ArrayList<InboxModel> arrayList;
    public ArrayList<String> uids = new ArrayList<>();


    public String client_uid;



    public static InboxFragment newInstance() {
        return new InboxFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(CREDENTIALS_PREFS, MODE_PRIVATE);
        userUID = sharedPreferences.getString(USER_UID, "");


        bookingImage = view.findViewById(R.id.bookingsImage);
        bookingImage.setOnClickListener(v -> {

            if (isNetworkAvailable()) {
                progressBar.setVisibility(View.VISIBLE);
                bookingImage.setVisibility(View.GONE);

                Intent intent = new Intent(view.getContext(), WebPage.class);
                startActivity(intent);

            } else {

                new AlertDialog.Builder(view.getContext())
                        .setTitle("Check Internet connection")
                        .setMessage("Please make sure you have an active internet connection")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, null)
                        .show();


            }

        });

        progressBar = view.findViewById(R.id.topBarProgress);
        profilePicImage = view.findViewById(R.id.icon_user);
        userName = view.findViewById(R.id.txtUsername);
        loadingProressBar = view.findViewById(R.id.loadingProgressBar);

        conversatonsCardView = view.findViewById(R.id.conversatonsCardView);
        conversatonsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    try {
                        Intent intent = new Intent(getContext(), ChatPage.class);
                        startActivity(intent);
                    } catch (Exception e) {
                    }
                } else {

                    new AlertDialog.Builder(getContext())
                            .setTitle("Check Internet connection")
                            .setMessage("Please make sure you have an active internet connection")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, null)
                            .show();
                }
            }
        });

        lastMessage = view.findViewById(R.id.messageTextView);
        chatsListView = view.findViewById(R.id.chatsListView);

        //getLastMessage();


        arrayList = new ArrayList<>();

        //loadChat();
        loadImage();

        //The listener that detects which list item has been selected.
        chatsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                int index = position;
                Bundle mBundle = new Bundle();
                client_uid = uids.get(index).toString();

                //A bundle is passed to the chat activity to determine which
                //chat was opened by the Admin
                mBundle.putString("client_uid", client_uid);
                Intent intent = new Intent(getContext(), ChatPage.class);
                intent.putExtra("client_uid", client_uid);
                startActivity(intent);
            }
        });


        return view;
    }

    private void getLastMessage() {

        root = FirebaseDatabase.getInstance("https://ultimate-storm-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference()
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("message");

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                lastMessage.setText(snapshot.getValue(String.class));

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void loadChat() {
        root = FirebaseDatabase.getInstance("https://ultimate-storm-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("users");

        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                lastMessage.setText(snapshot.child(userUID).child("message").getValue(String.class));

                collectChats((Map<String, Object>) snapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    ArrayList<String> images = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();



    //Loads all the chats for the admin
    private void collectChats(Map<String, Object> users) {


        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()) {

            //Get user map
            Map singleUser = (Map) entry.getValue();

            //Get phone field and append to list
            images.add((String) singleUser.get("image"));
            String temp_image = (String) singleUser.get("image");

            String temp_lastUID = (String) singleUser.get("lastUID");

            String temp_message = (String) singleUser.get("message");

            names.add((String) singleUser.get("name"));
            String temp_name = (String) singleUser.get("name");



            uids.add((String) singleUser.get("uid"));
            String uid = (String) singleUser.get("uid");

            String chat;
            String chat2 ="";

            String myUID =FirebaseAuth.getInstance().getCurrentUser().getUid();
            if (uid.equals(myUID)){

                if (!temp_lastUID.equals(Constants.MASTER_UID)) {
                    chat = "You: " + temp_message;

                }
                else{
                    chat = "Rukudzo" + ": " + temp_message;

                }


                lastMessage.setText(chat);
            }

            if (!temp_lastUID.equals(Constants.MASTER_UID)) {
                chat2 = temp_name+ ": " + temp_message;

            }
            else{
                chat2 = "You: " + temp_message;

            }


            //Adds chats to the list view
            arrayList.add(new InboxModel(uid, temp_name,
                    chat2, StringToBitMap(temp_image)));

        }

        loadingProressBar.setVisibility(View.GONE);
        checkUser();

        InboxAdapter inboxAdapter = new InboxAdapter(getContext(), R.layout.inbox_list_item, arrayList);
        chatsListView.setAdapter(inboxAdapter);
    }


    //Checks if the users is the admin
    private void checkUser() {
        //Loading the current user's UID from the shared preferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(CREDENTIALS_PREFS, MODE_PRIVATE);
        userUID = sharedPreferences.getString(USER_UID, "");

        if (userUID.equals(Constants.MASTER_UID)) {
            conversatonsCardView.setVisibility(View.GONE);
            chatsListView.setVisibility(View.VISIBLE);
        } else {
            conversatonsCardView.setVisibility(View.VISIBLE);
            chatsListView.setVisibility(View.GONE);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
        bookingImage.setVisibility(View.VISIBLE);

        loadChat();

        arrayList.clear();



    }

    public void loadImage() {
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        String picture = sharedPreferences.getString(PICTURE, "");
        String firstName = sharedPreferences.getString(FIRST_NAME, "");
        String lastName = sharedPreferences.getString(LAST_NAME, "");
        userName.setText("INBOX");
        profilePicImage.setImageBitmap(StringToBitMap(picture));
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}