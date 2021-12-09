package com.run.ultimate_fitness.ui.home;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.run.ultimate_fitness.R;
import com.run.ultimate_fitness.WebPage;
import com.run.ultimate_fitness.WorkoutsGoalPage;
import com.run.ultimate_fitness.database.DBHelper;
import com.run.ultimate_fitness.databinding.FragmentHomeBinding;
import com.run.ultimate_fitness.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    public int water, waterGoal,calories, caloriesGoal, steps ;
    private int stepsGoal;

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private ImageView profilePicImage, imgHideMainCard, imgShowMainCard,bookingImage;
    private TextView userName;
    private TextView txtWaterDrank2, txtSteps, txtCalories, txtGlasses;
    private ProgressBar progressBar;

    public static final String USER_PREFS ="userPrefs";
    public static final String IS_LOGGED_IN ="isLoggedIn";
    public static final String PICTURE ="picture";
    public static final String FIRST_NAME ="firstName";
    public static final String LAST_NAME ="lastName";
    public static final String WATER ="water";
    public static final String CALORIES ="calories";
    public static final String STEPS ="steps";

    public static final String GOALS_PREFS ="goalsPrefs";
    public static final String PROGRESS_PREFS ="progressPrefs";
    public static final String DATE_PREFS ="datePrefs";



    public static final String WATER_GOAL ="water_goal";
    public static final String CALORIES_GOAL ="calories_goal";
    public static final String STEPS_GOAL ="steps_goal";

    public static final String USER_UID = "uid";
    public static final String CREDENTIALS_PREFS = "credentials";


    public DatabaseReference root;
    String picture;

    String waterDrank ;
    String waterProgress;

    private RelativeLayout waterLayout, stepsLayout, caloriesLayout;
    private CardView waterCard, stepsCard, caloriesCard, mainCard;


    private static  int CurrentProgress = 0;
    public ProgressBar waterProgressBar, stepsProgressBar, caloriesProgressBar;
    public Button btnDrink;
    private TextView txtWaterDrank, txtStepsTaken, txtCaloriesEaten;
    private TextView stepsTakenText, caloriesEatenText;
    public String uid;
    private DBHelper dbHelper;

    public Date currentDate, dateYesterday;

    public String temp_date;

    SharedPreferences goalPrefs, credentialPrefs, userPrefs, progressPrefs;
    public String fullname;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        goalPrefs = getActivity().getApplicationContext().getSharedPreferences(GOALS_PREFS,MODE_PRIVATE);
        credentialPrefs = getActivity().getApplicationContext().getSharedPreferences(CREDENTIALS_PREFS,MODE_PRIVATE);
        userPrefs = getActivity().getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);
        progressPrefs = getActivity().getApplicationContext().getSharedPreferences(PROGRESS_PREFS,MODE_PRIVATE);

        dbHelper =new DBHelper(getActivity());

        try {
            checkDate();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        uid = credentialPrefs.getString(USER_UID, null);

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        //final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                //textView.setText(s);
            }
        });


        profilePicImage = root.findViewById(R.id.icon_user);

        bookingImage = root.findViewById(R.id.bookingsImage);
        progressBar = root.findViewById(R.id.topBarProgress);

        bookingImage.setOnClickListener(v -> {

            if (isNetworkAvailable())
            {
                progressBar.setVisibility(View.VISIBLE);
                bookingImage.setVisibility(View.GONE);

                Intent intent = new Intent(root.getContext(), WebPage.class);
                startActivity(intent);

            }else{

                new AlertDialog.Builder(root.getContext())
                        .setTitle("Check Internet connection")
                        .setMessage("Please make sure you have an active internet connection")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, null)
                        .show();


            }

        });



        profilePicImage = root.findViewById(R.id.icon_user);
        userName = root.findViewById(R.id.txtUsername);
        loadImage();
        writeImageToFirebase();



        waterLayout =  root.findViewById(R.id.layout_waterProgress);
        stepsLayout =  root.findViewById(R.id.layout_stepsProgress);
        caloriesLayout =  root.findViewById(R.id.layout_caloriesProgress);

        mainCard = root.findViewById(R.id.card_main);
        waterCard = root.findViewById(R.id.waterCard);
        stepsCard = root.findViewById(R.id.stepsCard);
        caloriesCard = root.findViewById(R.id.caloriesCard);



        waterProgressBar = root.findViewById(R.id.waterProgress_bar);

        stepsProgressBar = root.findViewById(R.id.stepsProgress_bar);
        caloriesProgressBar = root.findViewById(R.id.calorieProgress_bar);




        txtWaterDrank = root.findViewById(R.id.txtWaterDrank);

        stepsTakenText = root.findViewById(R.id.txtStepsTaken);
        caloriesEatenText = root.findViewById(R.id.txtCaloriesEaten);

        txtGlasses = root.findViewById(R.id.txtGlasses);
        txtGlasses.setText("Glasses");

        txtStepsTaken = root.findViewById(R.id.txtStepsProgress);


        txtWaterDrank2 = root.findViewById(R.id.txtWaterProgress);

        txtSteps = root.findViewById(R.id.txtSteps);
        txtSteps.setText("Steps");

        txtCalories = root.findViewById(R.id.txtCalories);
        txtCalories.setText("Calories");

        btnDrink = root.findViewById(R.id.btnDrinkWater);



        water = progressPrefs.getInt(WATER, 0);
        calories = progressPrefs.getInt(CALORIES, 0);
        steps = progressPrefs.getInt(STEPS, 0);

        waterGoal = goalPrefs.getInt(WATER_GOAL, 0);
        caloriesGoal = goalPrefs.getInt(CALORIES_GOAL, 0);
        stepsGoal = goalPrefs.getInt(STEPS_GOAL, 0);


        txtCaloriesEaten = root.findViewById(R.id.txtCalorieProgress);
        txtCaloriesEaten.setText(calories + "/" + caloriesGoal);

        caloriesEatenText.setText(calories + "/" + caloriesGoal);
        stepsTakenText.setText(steps + "/" + stepsGoal);
        txtStepsTaken.setText(steps + "/" + stepsGoal);






        waterProgressBar.setMax(waterGoal);
        caloriesProgressBar.setMax(caloriesGoal);



        txtWaterDrank.setText(water + "/" + waterGoal);
        txtWaterDrank2.setText(water + "/" + waterGoal);
        waterProgressBar.setProgress(water);

        //txtCaloriesEaten.setText(ca + "/" + waterGoal);
        caloriesProgressBar.setProgress(calories);




        imgHideMainCard = root.findViewById(R.id.imgHideMainCard);
        imgShowMainCard = root.findViewById(R.id.imgShowMainCard);


        imgHideMainCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgHideMainCard.setVisibility(View.INVISIBLE);
                imgShowMainCard.setVisibility(View.VISIBLE);
                mainCard.animate().translationY(500);
            }
        });
        imgShowMainCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgShowMainCard.setVisibility(View.INVISIBLE);
                imgHideMainCard.setVisibility(View.VISIBLE);
                mainCard.animate().translationY(0);

            }
        });



        initOnClickListeners();


        btnDrink.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                drinkWater();
                SharedPreferences.Editor editor = progressPrefs.edit();
                editor.putInt(WATER, water);
                editor.apply();


            }
        });


        checkGoals();

        return root;
    }

    private void checkGoals() {
        if (waterGoal == 0 || caloriesGoal == 0){
            new AlertDialog.Builder(this.getContext())
                    .setTitle("Attention")
                    .setMessage("You have not set all your fitness goals. Please set up your fitness profile.")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton("Setup", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getContext(), WorkoutsGoalPage.class);
                            startActivity(intent);

                        }
                    }).show();
/*
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.ok, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
 */
            water = 0;
        }
    }

    public void checkDate() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date dateToday = new Date();
        SharedPreferences datePrefs = this.getContext().getSharedPreferences(DATE_PREFS,MODE_PRIVATE);

        temp_date = datePrefs.getString(DATE_PREFS, "");


        currentDate = new Date();

        if(temp_date != "") {


            String temp_dateToday = dateFormat.format(dateToday);
            String temp_dateYesterday = datePrefs.getString(DATE_PREFS, "");


            if (!temp_dateToday.equals(temp_dateYesterday)) {
                resetProgress();
                System.out.println("Reset");
                temp_date = temp_dateToday;


            } else {
                System.out.println("Same day");
            }
        }
        else {
            temp_date = dateFormat.format(dateToday);
            System.out.println(temp_date);

        }


        SharedPreferences.Editor editor = datePrefs.edit();
        editor.putString(DATE_PREFS, temp_date);
        editor.apply();


    }

    //Writes image to Firebase RTDB for the Admin to read
    private void writeImageToFirebase() {
        boolean loggedIn = userPrefs.getBoolean(IS_LOGGED_IN, false);
        SharedPreferences.Editor editor = userPrefs.edit();
        editor.putBoolean(IS_LOGGED_IN, true);

        if (loggedIn == true) {
            if (isNetworkAvailable()) {
                if (!uid.equals(Constants.MASTER_UID)) {

                    uid = credentialPrefs.getString(USER_UID, "");

                    if (uid != "") {
                        try {
                            //Sets the the root to a specific child in the Firebase RTDB
                            root = FirebaseDatabase.getInstance("https://ultimate-storm-default-rtdb.europe-west1.firebasedatabase.app")
                                    .getReference()
                                    .child("users")
                                    .child(uid);

                            DatabaseReference message_root = root;
                            Map<String, Object> map = new HashMap<String, Object>();
                            //Writes these strings into the RTDB
                            map.put("name", fullname);
                            map.put("image", picture);
                            map.put("uid", uid);

                            message_root.updateChildren(map);
                        } catch (Exception e) { }
                    }
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //This method converts a string to a Bitmap image
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

    //Loads the profile image
    public  void loadImage(){

        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);
        picture = sharedPreferences.getString(PICTURE,"");
        String firstName = sharedPreferences.getString(FIRST_NAME,"");
        String lastName = sharedPreferences.getString(LAST_NAME,"");


        userName.setText("Welcome, " + firstName);
        fullname = firstName + " " + lastName;

        profilePicImage.setImageBitmap(StringToBitMap(picture));
    }

    //On click listeners for displaying respective card on click
    private void initOnClickListeners() {

        waterCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waterCardClicked();
            }
        });
        stepsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepsCardClicked();
            }
        });
        caloriesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                caloriesCardClicked();
            }
        });

    }

    //Displays the progress bar for each card on the home screen respectively
    public void waterCardClicked(){
        waterLayout.setVisibility(View.VISIBLE);
        stepsLayout.setVisibility(View.INVISIBLE);
        caloriesLayout.setVisibility(View.INVISIBLE);
    }
    public void stepsCardClicked(){
        waterLayout.setVisibility(View.INVISIBLE);
        stepsLayout.setVisibility(View.VISIBLE);
        caloriesLayout.setVisibility(View.INVISIBLE);
    }
    public void caloriesCardClicked(){
        waterLayout.setVisibility(View.INVISIBLE);
        stepsLayout.setVisibility(View.INVISIBLE);
        caloriesLayout.setVisibility(View.VISIBLE);
    }


    //Checks if the device is connected to the internet
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //This method adds a glass of water to the water progress
    public void drinkWater() {
        water++;
        waterDrank = String.valueOf(water);
        waterProgress = waterDrank + "/" + waterGoal;
        txtWaterDrank.setText("" + waterProgress);
        txtWaterDrank2.setText("" + waterProgress);
        CurrentProgress = CurrentProgress + 10;
        waterProgressBar.setProgress(water);
        if (water == waterGoal) {
            new AlertDialog.Builder(this.getContext())
                    .setTitle("Achievement")
                    .setMessage("You have completed your daily water goal!")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                        }
                    }).show();
/*
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.ok, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
*/
        }

        if (water > 13) {
            new AlertDialog.Builder(this.getContext())
                    .setTitle("Warning")
                    .setMessage("Too much water within a short period of time can cause water Intoxication")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                        }
                    }).show();
/*
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.ok, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
 */
        }
    }

    public void resetProgress(){
        dbHelper.deleteAllItems();
        SharedPreferences progressPrefs = this.getContext().getSharedPreferences(PROGRESS_PREFS,MODE_PRIVATE);

        water =0;
        calories=0;

        SharedPreferences.Editor editor = progressPrefs.edit();
        editor.putInt(WATER, 0);
        editor.putInt(CALORIES, 0);

        editor.apply();


        editor.clear();
        editor.apply();
    }


    @Override
    public void onResume() {
        super.onResume();
        //Ensures that the progress bar for opening the appointments page is GONE when returning
        //to the home page
        progressBar.setVisibility(View.GONE);
        bookingImage.setVisibility(View.VISIBLE);

    }
}


