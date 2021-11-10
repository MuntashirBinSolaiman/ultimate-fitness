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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.run.ultimate_fitness.R;
import com.run.ultimate_fitness.WebPage;
import com.run.ultimate_fitness.adapters.GymWorkoutsAdapter;
import com.run.ultimate_fitness.adapters.workout_adapters.InboxAdapter;
import com.run.ultimate_fitness.ui.workouts.WorkoutsModel;
import com.run.ultimate_fitness.utils.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class InboxFragment extends Fragment {

    public static final String PICTURE ="picture";
    public static final String FIRST_NAME ="firstName";
    public static final String LAST_NAME ="lastName";
    public static final String USER_PREFS ="userPrefs";

    private ImageView profilePicImage, imgGymWorkouts,bookingImage;
    private TextView userName;
    private ProgressBar progressBar;

    public static final String CREDENTIALS_PREFS = "credentials";
    public static final String USER_UID = "uid";

    private CardView conversatonsCardView;
    public ListView chatsListView;
    private String userUID;

    public DatabaseReference root, children;
    public Iterator i;



    public static InboxFragment newInstance() {
        return new InboxFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(CREDENTIALS_PREFS,MODE_PRIVATE);
        userUID = sharedPreferences.getString(USER_UID, "uid");


        bookingImage = view.findViewById(R.id.bookingsImage);
        progressBar = view.findViewById(R.id.topBarProgress);



        bookingImage.setOnClickListener(v -> {

            if (isNetworkAvailable())
            {
                progressBar.setVisibility(View.VISIBLE);
                bookingImage.setVisibility(View.GONE);

                Intent intent = new Intent(view.getContext(), WebPage.class);
                startActivity(intent);

            }else{

                new AlertDialog.Builder(view.getContext())
                        .setTitle("Check Internet connection")
                        .setMessage("Please make sure you have an active internet connection")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, null)
                        .show();


            }

        });

        profilePicImage = view.findViewById(R.id.icon_user);

        userName = view.findViewById(R.id.txtUsername);

        conversatonsCardView = view.findViewById(R.id.conversatonsCardView);
        conversatonsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    try {
                        Intent intent = new Intent(getContext(), ChatPage.class);
                        startActivity(intent);
                    }
                    catch (Exception e)
                    {
                    }
                }
                    else{

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

        chatsListView = view.findViewById(R.id.chatsListView);


        ArrayList<InboxModel> arrayList = new ArrayList<>();

        String temp_image = String.valueOf(R.drawable.ombati);
        //System.out.println(R.drawable.ombati);

        arrayList.add(new InboxModel("", "Warona Mogolwane" ,"Hello Sir",StringToBitMap(temp_image)));

        InboxAdapter inboxAdapter = new InboxAdapter(getContext(), R.layout.inbox_list_item,arrayList);
        chatsListView.setAdapter(inboxAdapter);






        //checkUser();
        loadImage();
        return view;
    }

    private void updateChats(DataSnapshot snapshot) {
        root = FirebaseDatabase.getInstance("https://ultimate-storm-default-rtdb.europe-west1.firebasedatabase.app").getReference().getRoot();
        i = snapshot.getChildren().iterator();
        while (i.hasNext()) {
            String temp_child = (String) snapshot.getValue();

            children = FirebaseDatabase.getInstance("https://ultimate-storm-default-rtdb.europe-west1.firebasedatabase.app").getReference().child(temp_child);


            children.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void checkUser() {
        if (userUID != Constants.MASTER_UID){
            conversatonsCardView.setVisibility(View.VISIBLE);
            chatsListView.setVisibility(View.GONE);
        }
        else{
            conversatonsCardView.setVisibility(View.GONE);
            chatsListView.setVisibility(View.VISIBLE);

        }
    }


    @Override
    public void onResume() {
        super.onResume();

        progressBar.setVisibility(View.GONE);
        bookingImage.setVisibility(View.VISIBLE);

    }

    public  void loadImage(){
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);
        String picture = sharedPreferences.getString(PICTURE,"");
        String firstName = sharedPreferences.getString(FIRST_NAME,"");
        String lastName = sharedPreferences.getString(LAST_NAME,"");


        userName.setText("INBOX");

        profilePicImage.setImageBitmap(StringToBitMap(picture));
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
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