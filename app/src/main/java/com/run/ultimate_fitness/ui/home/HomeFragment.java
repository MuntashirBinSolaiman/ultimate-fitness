package com.run.ultimate_fitness.ui.home;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Layout;
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

import com.run.ultimate_fitness.R;
import com.run.ultimate_fitness.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    public int water ;

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private ImageView profilePicImage, imgHideMainCard, imgShowMainCard;
    private TextView userName;
    private TextView txtWaterDrank2, txtSteps, txtCalories, txtGlasses;

    public static final String PICTURE ="picture";
    public static final String FIRST_NAME ="firstName";
    public static final String LAST_NAME ="lastName";
    public static final String WATER ="water";

    String waterDrank ;
    String waterProgress;

    private RelativeLayout waterLayout, stepsLayout, caloriesLayout;
    private CardView waterCard, stepsCard, caloriesCard, mainCard;



    public static final String USER_PREFS ="userPrefs";

    private static  int CurrentProgress = 0;
    private ProgressBar waterProgressBar, stepsProgressBar, caloriesProgressBar;
    public Button btnDrink;
    private TextView txtWaterDrank, txtStepsTaken, txtCaloriesEaten;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);


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
        userName = root.findViewById(R.id.txtUsername);
        loadImage();


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

        txtGlasses = root.findViewById(R.id.txtGlasses);
        txtGlasses.setText("Glasses");

        txtStepsTaken = root.findViewById(R.id.txtStepsProgress);
        txtStepsTaken.setText("0/0");

        txtCaloriesEaten = root.findViewById(R.id.txtCalorieProgress);
        txtCaloriesEaten.setText("0/0");

        txtWaterDrank2 = root.findViewById(R.id.txtWaterProgress);

        txtSteps = root.findViewById(R.id.txtSteps);
        txtSteps.setText("Steps");

        txtCalories = root.findViewById(R.id.txtCalories);
        txtCalories.setText("Calories");

        btnDrink = root.findViewById(R.id.btnDrinkWater);
        waterProgressBar.setMax(8);



        water = sharedPreferences.getInt(WATER, 0);
        txtWaterDrank.setText(water + "/8");
        txtWaterDrank2.setText(water + "/8");


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
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(WATER, water);
                editor.apply();


            }
        });



        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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

    public  void loadImage(){
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);
        String picture = sharedPreferences.getString(PICTURE,"");
        String firstName = sharedPreferences.getString(FIRST_NAME,"");
        String lastName = sharedPreferences.getString(LAST_NAME,"");


        userName.setText("Welcome, " + firstName);

        profilePicImage.setImageBitmap(StringToBitMap(picture));
    }

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



    public void drinkWater(){
        water++;
        waterDrank = String.valueOf(water);
        waterProgress = waterDrank + "/8";
        txtWaterDrank.setText("" + waterProgress );
        txtWaterDrank2.setText("" + waterProgress);
        System.out.println(waterProgress);
        CurrentProgress = CurrentProgress + 10;
        //progressBar.setProgress(water);
        if (water >= 8)
        {
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
            water = 0;

        }
    }





}