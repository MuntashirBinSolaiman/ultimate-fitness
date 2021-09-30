package com.run.ultimate_fitness.ui.home;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.run.ultimate_fitness.R;
import com.run.ultimate_fitness.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    public int water ;

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private ImageView profilePicImage;
    private TextView userName;
    private TextView text_view_progress;

    public static final String PICTURE ="picture";
    public static final String FIRST_NAME ="firstName";
    public static final String LAST_NAME ="lastName";
    public static final String WATER ="water";

    String waterDrank ;
    String waterProgress;



    public static final String USER_PREFS ="userPrefs";

    private static  int CurrentProgress = 0;
    private ProgressBar progressBar;
    public Button btnDrink;
    private TextView txtWaterDrankk;


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
        progressBar = root.findViewById(R.id.progress_bar);
        text_view_progress = root.findViewById(R.id.text_view_progress);

        txtWaterDrankk = root.findViewById(R.id.txtWaterDrank);
        btnDrink = root.findViewById(R.id.btnDrinkWater);
        progressBar.setMax(8);



        water = sharedPreferences.getInt(WATER, 0);
        txtWaterDrankk.setText(water + "/8");
        text_view_progress.setText("" + water );







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

    public void drinkWater(){
        water++;
        waterDrank = String.valueOf(water);
        waterProgress = waterDrank + "/8";
        txtWaterDrankk.setText("" + waterProgress );
        System.out.println(waterProgress);
        CurrentProgress = CurrentProgress + 10;
        progressBar.setProgress(water);
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