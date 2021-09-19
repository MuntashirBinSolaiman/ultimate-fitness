package com.run.ultimate_fitness;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.run.ultimate_fitness.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private int CurrentProgress = 0;
    private ProgressBar progressBar;
    private Button btnDrink;
    private TextView txtWaterDrankk;
    public int water = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        txtWaterDrankk = findViewById(R.id.txtWaterDrank);
        btnDrink = findViewById(R.id.btnDrinkWater);
        progressBar = findViewById(R.id.progressBarSteps);
        //progressBar.setMax(100);

    //countDownTimer.start();



    }
    CountDownTimer countDownTimer = new CountDownTimer(11*1000,1000) {
        @Override
        public void onTick(long l) {
            CurrentProgress = CurrentProgress + 10;
            progressBar.setProgress(CurrentProgress);

        }

        @Override
        public void onFinish() {

        }

    };

        public void drinkWater(View view)
        {
            water++;
             String waterDrank = String.valueOf(water);
             String waterProgress = waterDrank + "/8";
             txtWaterDrankk.setText("" + water );
            System.out.println(waterProgress);
        }

        public void goToProfile(View view){
            Intent intent = new Intent(MainActivity.this, ProfilePage.class);
            startActivity(intent);
        }

}