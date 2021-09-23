package com.run.ultimate_fitness;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.run.ultimate_fitness.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    public static final String IS_LOGGED_IN ="isLoggedIn";
    public static final String USER_PREFS ="userPrefs";


/*Home tab*/
    private int CurrentProgress = 0;
    private ProgressBar progressBar;
    private Button btnDrink;
    private TextView txtWaterDrankk;
    public int water = 0;

    /*Workouts Tab*/
    private RecyclerView homeWorkouts;
    private RecyclerView.Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        boolean loggedIn = sharedPreferences.getBoolean(IS_LOGGED_IN,true);
        if (loggedIn) {
            Intent intent = new Intent(MainActivity.this, LoginPage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_nutrition, R.id.navigation_workouts,
                R.id.navigation_inbox, R.id.navigation_appointments)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        getSupportActionBar().hide();


        /*Home tab*/
        txtWaterDrankk = findViewById(R.id.txtWaterDrank);
        btnDrink = findViewById(R.id.btnDrinkWater);
        progressBar = findViewById(R.id.progressBarSteps);
        progressBar.setMax(100);

        /*Workouts tab*/
        homeWorkouts = findViewById(R.id.Rvhome_workouts);
        homeWorkouts = (RecyclerView) findViewById(R.id.Rvhome_workouts);







    }



    public void drinkWater(View view)
        {
            water++;
             String waterDrank = String.valueOf(water);
             String waterProgress = waterDrank + "/8";
             txtWaterDrankk.setText("" + waterProgress );
            System.out.println(waterProgress);
            CurrentProgress = CurrentProgress + 10;
            progressBar.setProgress(CurrentProgress);
            if (water == 8)
            {
                new AlertDialog.Builder(this)
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
        }

        public void goToProfile(View view){
            Intent intent = new Intent(MainActivity.this, ProfilePage.class);
            startActivity(intent);
        }

}