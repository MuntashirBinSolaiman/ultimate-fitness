package com.run.ultimate_fitness.ui.workouts;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.run.ultimate_fitness.R;
import com.run.ultimate_fitness.WebPage;
import com.run.ultimate_fitness.adapters.HomeWorkoutsAdapter;

import java.util.ArrayList;
import java.util.List;

public class WorkoutsFragment extends Fragment implements HomeWorkoutsAdapter.OnWorkoutListener {

    RecyclerView recyclerViewAbs, recyclerViewBack, recyclerViewBiceps, recyclerViewHamstrings, recyclerViewHiit;
    RecyclerView recyclerViewLegs, recyclerViewLower_Body, recyclerViewShoulders, recyclerViewTriceps, recyclerViewUpper_Body;

    List<WorkoutsModel> absWorkoutsList, backWorkoutsList,bicepsWorkoutsList,hamstringsWorkoutsList,hiitWorkoutsList ;
    List<WorkoutsModel> legsWorkoutsList, lower_BodyWorkoutsList,shouldersWorkoutsList,tricepsWorkoutsList,upper_BodyWorkoutsList ;

    private ImageView profilePicImage, imgGymWorkouts, bookingImage;
    private TextView userName;
    private ProgressBar progressBar;

    public ImageButton imgHideAbsWorkouts;

    public SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public static final String PICTURE ="picture";
    public static final String FIRST_NAME ="firstName";
    public static final String LAST_NAME ="lastName";
    public static final String USER_PREFS ="userPrefs";

    public static final String VISIBLE_ZONE_PREFS ="visibleZonePrefs";
    public static final String ZONE_ABS ="zoneAbs";

    public String zoneAbs = "VISIBLE";



    private WorkoutsViewModel mViewModel;

    public static WorkoutsFragment newInstance() {
        return new WorkoutsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workouts, container, false);

        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(VISIBLE_ZONE_PREFS,MODE_PRIVATE);
        zoneAbs = sharedPreferences.getString(ZONE_ABS, "VISIBLE");
        imgHideAbsWorkouts = view.findViewById(R.id.hide_Arrow);

        recyclerViewAbs = view.findViewById(R.id.RvAbs_workouts);
        recyclerViewBack = view.findViewById(R.id.RvBack_workouts);
        recyclerViewBiceps = view.findViewById(R.id.RvBiceps_workouts);
        recyclerViewHamstrings = view.findViewById(R.id.RvHamstrings_workouts);
        recyclerViewHiit = view.findViewById(R.id.RvHiit_workouts);
        recyclerViewLegs = view.findViewById(R.id.RvLegs_workouts);
        recyclerViewLower_Body = view.findViewById(R.id.RvLower_Body_workouts);
        recyclerViewShoulders = view.findViewById(R.id.RvShoulders_workouts);
        recyclerViewTriceps = view.findViewById(R.id.RvTriceps_workouts);
        recyclerViewUpper_Body = view.findViewById(R.id.RvUpper_Body_workouts);



        profilePicImage = view.findViewById(R.id.icon_user);

        userName = view.findViewById(R.id.txtUsername);

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




        imgHideAbsWorkouts.setOnClickListener(v ->{

            changeVisibility();


        });


        loadImage();
        checkVisibility();


        //recyclerView.setHasFixedSize(true);
        recyclerViewAbs.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewBack.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewBiceps.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewHamstrings.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewHiit.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewLegs.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewLower_Body.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewShoulders.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewTriceps.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewUpper_Body.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        //initData();
        recyclerViewAbs.setAdapter(new HomeWorkoutsAdapter(initAbsData(), this));
        recyclerViewBack.setAdapter(new HomeWorkoutsAdapter(initBackData(), this));
        recyclerViewBiceps.setAdapter(new HomeWorkoutsAdapter(initBicepsData(), this));
        recyclerViewHamstrings.setAdapter(new HomeWorkoutsAdapter(initHamstringsData(), this));
        recyclerViewHiit.setAdapter(new HomeWorkoutsAdapter(initHiitData(), this));
        recyclerViewLegs.setAdapter(new HomeWorkoutsAdapter(initLegsData(), this));
        recyclerViewLower_Body.setAdapter(new HomeWorkoutsAdapter(initLowerBodyData(), this));
        recyclerViewShoulders.setAdapter(new HomeWorkoutsAdapter(initShouldersData(), this));
        recyclerViewTriceps.setAdapter(new HomeWorkoutsAdapter(initTricepsData(), this));
        recyclerViewUpper_Body.setAdapter(new HomeWorkoutsAdapter(initUpperBodyData(), this));


        return view;
    }

    public void checkVisibility() {
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(VISIBLE_ZONE_PREFS,MODE_PRIVATE);
        zoneAbs = sharedPreferences.getString(ZONE_ABS, "INVISIBLE");

        switch (zoneAbs) {
            case "VISIBLE":

                imgHideAbsWorkouts.setImageResource(R.drawable.ic_down_arrow);
                recyclerViewAbs.setVisibility(View.VISIBLE);
                break;

            case "GONE":
                imgHideAbsWorkouts.setImageResource(R.drawable.ic_up_arrow);
                recyclerViewAbs.setVisibility(View.GONE);
                break;


        }
        editor = sharedPreferences.edit();
        editor.putString(ZONE_ABS, zoneAbs);
        editor.apply();



    }
    public void changeVisibility() {
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(VISIBLE_ZONE_PREFS,MODE_PRIVATE);
        zoneAbs = sharedPreferences.getString(ZONE_ABS, "INVISIBLE");

        switch (zoneAbs) {
            case "VISIBLE":

                zoneAbs = "GONE";
                imgHideAbsWorkouts.setImageResource(R.drawable.ic_up_arrow);
                recyclerViewAbs.setVisibility(View.GONE);
                break;

            case "GONE":
                zoneAbs = "VISIBLE";
                imgHideAbsWorkouts.setImageResource(R.drawable.ic_down_arrow);
                recyclerViewAbs.setVisibility(View.VISIBLE);
                break;


        }
        editor = sharedPreferences.edit();
        editor.putString(ZONE_ABS, zoneAbs);
        editor.apply();

    }


    @Override
    public void onResume() {
        super.onResume();

        progressBar.setVisibility(View.GONE);
        bookingImage.setVisibility(View.VISIBLE);
        checkVisibility();

    }

    private List<WorkoutsModel> initAbsData() {

        absWorkoutsList = new ArrayList<>();

        //Adding Abs workouts
        {
            absWorkoutsList.add(new WorkoutsModel("Bicycle", "Abs", R.drawable.bicycle, "" +
                "1. Start by lying on the ground, with your lower back pressed flat into the floor and your head and shoulders raised slightly above it.\n" +
                "\n" +
                "2. Place your hands lightly on the sides of your head; don’t knit your fingers behind.\n" +
                "\n" +
                "3. Lift one leg just off the ground and extend it out.\n" +
                "\n" +
                "4. Lift the other leg and bend your knee towards your chest.\n" +
                "\n" +
                "5. As you do so twist through your core so the opposite arm comes towards the raised knee. It might be best to think shoulder to knee as you move.\n" +
                "\n" +
                "6. Lower your leg and arm at the same time while bringing up the opposite two limbs to mirror the movement.\n" +
                "\n" +
                "7. Keep on alternating sides until you’ve managed 10 reps on each, aiming for three sets of 10 in total.\n"));}
        {
            absWorkoutsList.add(new WorkoutsModel("Flutter Kicks", "Abs", R.drawable.flutter_kicks, "" +
        "1. Lie down on your back, facing up.\n" +
                "2. Place both your hands underneath your buttocks.\n" +
                "\n" +
                "3. Keep your lower back on the ground as you lift the right leg off the ground slightly past hip height, and lift the left leg so it hovers a few inches off the floor.\n" +
                "\n" +
                "4. Hold for 2 seconds, then switch the position of the legs, making a flutter kick motion.\n" +
                "\n" +
                "5. For more of a challenge, lift your head and neck off the floor.\n" +
                "\n" +
                "6. Repeat this motion for up to 30 seconds  \n"));}
        {
            absWorkoutsList.add(new WorkoutsModel("Forearm Plank", "Abs", R.drawable.forearm_plank, "" +
                "1. Assume a push-up position but bend your arms at your elbows so your weight rests on your forearms. \n" +
                "\n" +
                "2. Tighten your abs, clench your glutes and keep your body straight from head to heels. \n" +
                "\n" +
                "3. Hold as long as you can.  "));}
        {
            absWorkoutsList.add(new WorkoutsModel("In And Out", "Abs", R.drawable.in_and_out,
"1. Start in a sitting position, arms either side of your body with your legs bent, feet together, flat on the floor in front of you.\n" +
        "\n" +
        "2. Tighten your core, and keep a straight back as you raise your hands and feet from the floor, and bring your knees up to your chest. You are now in the ‘in’ position\n" +
        "\n" +
        "3. Gently lean back, keeping your spine straight, as you straighten your legs out in front of you.\n" +
        "\n" +
        "4. As your shoulders come close to the floor, hold the position without allowing them, or any other part of your body except your bottom, make contact with the floor.\n" +
        "\n" +
        "5. Now lift your shoulders, and bend your legs back into the ‘in’ position. Remember to keep your core tight throughout.\n" +
        "\n" +
        "6. Repeat the ‘in’, ‘out’ movement for the desired number of repetitions."));}
        {
            absWorkoutsList.add(new WorkoutsModel("Leg Raises", "Abs", R.drawable.leg_raises,
"1. Lie on your back, legs straight and together. \n" +
        "\n" +
        "2. Keep your legs straight and lift them all the way up to the ceiling until your butt comes off the floor. \n" +
        "\n" +
        "3. Slowly lower your legs back down till they’re just above the floor. Hold for a moment.\n" +
        "\n" +
        "4. Raise your legs back up. Repeat."));}
        {
            absWorkoutsList.add(new WorkoutsModel("Plank To Forearm Plank", "Abs", R.drawable.plank_to_forearm_plank,
                "Alternate Between Planks and Forearm Planks. \n" +
                        "\n" +
                        "3 Sets of 10-20 alternations."));}
        {
            absWorkoutsList.add(new WorkoutsModel("Straight Arm Plank", "Abs", R.drawable.plank,
                "1.Get into plank position by supporting your body weight on your hands and toes.\n" +
                        "\n" +
                        "2.Hands should be placed directly under your shoulders.\n" +
                        "\n" +
                        "3.Keep your abdominals contracted and your back straight, eyes ahead of you.\n" +
                        "\n" +
                        "4.Hold this position for as long as you can, building up to 1 minute.\n"));}
        {
            absWorkoutsList.add(new WorkoutsModel("Reverse Crunch", "Abs", R.drawable.reverse_crunch,
"1. Lie face-up on a mat or other soft surface with your knees bent at 90 degrees and your feet flat on the floor. Keep your arms near your sides with your palms down.\n" +
        "\n" +
        "2. Exhale and brace your core. Lift your feet off the ground and raise your thighs until they’re vertical. Keep your knees bent at 90 degrees throughout the movement.\n" +
        "\n" +
        "3. Tuck your knees toward your face as far as you can comfortably go without lifting your mid-back from the mat. Your hips and lower back should lift off the ground.\n" +
        "\n" +
        "4. Hold for a moment and slowly lower your feet back toward the floor until they reach the ground.\n" +
        "\n" +
        "5. Repeat for at least 10-12 repetitions. Do one set to start, and increase the number of reps and sets as you get stronger.\n"));}
        {
            absWorkoutsList.add(new WorkoutsModel("Sit Ups", "Abs", R.drawable.sit_ups,
"1. Lie on the floor facing the ceiling with a slight bend in your knees and arms bent at the elbows and hands lightly touching your head by the ears.\n" +
        "\n" +
        "2. Engage your core and lift your upper body so your right elbow touches your left knee.\n" +
        "\n" +
        "3. Return to the start position then lift your upper body so your left elbow touches your right knee.\n" +
        "\n" +
        "4. Return to the start position."));}
        {
            absWorkoutsList.add(new WorkoutsModel("Toe Touches", "Abs", R.drawable.toe_touches,
                "1. Lie on your back and lift your legs and arms up so they are extended toward the ceiling. Lift your upper back off the floor, reaching your hands toward your feet.\n" +
                        "\n" +
                        "Lower your back and repeat the crunch motion to complete one rep."));}



        return absWorkoutsList;
    }
    private List<WorkoutsModel> initBackData() {

        backWorkoutsList = new ArrayList<>();

        //Adding Back workouts
        {
            backWorkoutsList.add(new WorkoutsModel("Bent Over Row", "Back", R.drawable.bent_over_row, "" +
                    "1. Start by lying on the ground, with your lower back pressed flat into the floor and your head and shoulders raised slightly above it.\n" +
                    "\n" +
                    "2. Place your hands lightly on the sides of your head; don’t knit your fingers behind. Be careful not to yank your head with your hands at any point during the exercise.\n" +
                    "\n" +
                    "3. Lift one leg just off the ground and extend it out.\n" +
                    "\n" +
                    "4. Lift the other leg and bend your knee towards your chest.\n" +
                    "\n" +
                    "5. As you do so twist through your core so the opposite arm comes towards the raised knee. You don’t need to touch elbow to knee, instead focus on moving through your core as you turn your torso. Your elbow should stay in same position relative to your head throughout – the turn that brings it closer to the knee comes from your core. It might be best to think shoulder to knee as you move, rather than elbow to knee.\n" +
                    "\n" +
                    "6. Lower your leg and arm at the same time while bringing up the opposite two limbs to mirror the movement.\n" +
                    "\n" +
                    "7. Keep on alternating sides until you’ve managed 10 reps on each, aiming for three sets of 10 in total, or add the bicycle crunch into circuit training and just keep going for as long as the timer runs.\n"));}

        return backWorkoutsList;
    }
    private List<WorkoutsModel> initBicepsData() {

        bicepsWorkoutsList = new ArrayList<>();

        //Adding Biceps workouts
        {
            bicepsWorkoutsList.add(new WorkoutsModel("Hammer Curls", "Biceps", R.drawable.hammer_curls, "" +
                    "1. Start by lying on the ground, with your lower back pressed flat into the floor and your head and shoulders raised slightly above it.\n" +
                    "\n" +
                    "2. Place your hands lightly on the sides of your head; don’t knit your fingers behind. Be careful not to yank your head with your hands at any point during the exercise.\n" +
                    "\n" +
                    "3. Lift one leg just off the ground and extend it out.\n" +
                    "\n" +
                    "4. Lift the other leg and bend your knee towards your chest.\n" +
                    "\n" +
                    "5. As you do so twist through your core so the opposite arm comes towards the raised knee. You don’t need to touch elbow to knee, instead focus on moving through your core as you turn your torso. Your elbow should stay in same position relative to your head throughout – the turn that brings it closer to the knee comes from your core. It might be best to think shoulder to knee as you move, rather than elbow to knee.\n" +
                    "\n" +
                    "6. Lower your leg and arm at the same time while bringing up the opposite two limbs to mirror the movement.\n" +
                    "\n" +
                    "7. Keep on alternating sides until you’ve managed 10 reps on each, aiming for three sets of 10 in total, or add the bicycle crunch into circuit training and just keep going for as long as the timer runs.\n"));}
        {
            bicepsWorkoutsList.add(new WorkoutsModel("Single Arm Dumbbell Curl", "Biceps", R.drawable.single_arm_dummbell_curls, "" +
                    "1. Start by lying on the ground, with your lower back pressed flat into the floor and your head and shoulders raised slightly above it.\n" +
                    "\n" +
                    "2. Place your hands lightly on the sides of your head; don’t knit your fingers behind. Be careful not to yank your head with your hands at any point during the exercise.\n" +
                    "\n" +
                    "3. Lift one leg just off the ground and extend it out.\n" +
                    "\n" +
                    "4. Lift the other leg and bend your knee towards your chest.\n" +
                    "\n" +
                    "5. As you do so twist through your core so the opposite arm comes towards the raised knee. You don’t need to touch elbow to knee, instead focus on moving through your core as you turn your torso. Your elbow should stay in same position relative to your head throughout – the turn that brings it closer to the knee comes from your core. It might be best to think shoulder to knee as you move, rather than elbow to knee.\n" +
                    "\n" +
                    "6. Lower your leg and arm at the same time while bringing up the opposite two limbs to mirror the movement.\n" +
                    "\n" +
                    "7. Keep on alternating sides until you’ve managed 10 reps on each, aiming for three sets of 10 in total, or add the bicycle crunch into circuit training and just keep going for as long as the timer runs.\n"));}


        return bicepsWorkoutsList;
    }
    private List<WorkoutsModel> initHamstringsData() {

        hamstringsWorkoutsList = new ArrayList<>();

        //Adding Hamstrings workouts
        {
            hamstringsWorkoutsList.add(new WorkoutsModel("Ramanian Deadlifts", "Hamstrings", R.drawable.romanian_deadlift, "" +
                    "1. Start by lying on the ground, with your lower back pressed flat into the floor and your head and shoulders raised slightly above it.\n" +
                    "\n" +
                    "2. Place your hands lightly on the sides of your head; don’t knit your fingers behind. Be careful not to yank your head with your hands at any point during the exercise.\n" +
                    "\n" +
                    "3. Lift one leg just off the ground and extend it out.\n" +
                    "\n" +
                    "4. Lift the other leg and bend your knee towards your chest.\n" +
                    "\n" +
                    "5. As you do so twist through your core so the opposite arm comes towards the raised knee. You don’t need to touch elbow to knee, instead focus on moving through your core as you turn your torso. Your elbow should stay in same position relative to your head throughout – the turn that brings it closer to the knee comes from your core. It might be best to think shoulder to knee as you move, rather than elbow to knee.\n" +
                    "\n" +
                    "6. Lower your leg and arm at the same time while bringing up the opposite two limbs to mirror the movement.\n" +
                    "\n" +
                    "7. Keep on alternating sides until you’ve managed 10 reps on each, aiming for three sets of 10 in total, or add the bicycle crunch into circuit training and just keep going for as long as the timer runs.\n"));}


        return hamstringsWorkoutsList;
    }
    private List<WorkoutsModel> initHiitData() {

        hiitWorkoutsList = new ArrayList<>();

        //Adding HIIT workouts
        {
            hiitWorkoutsList.add(new WorkoutsModel("Burpees (Home)", "HIIT", R.drawable.burpees_home, "" +
                    "1. Start by lying on the ground, with your lower back pressed flat into the floor and your head and shoulders raised slightly above it.\n" +
                    "\n" +
                    "2. Place your hands lightly on the sides of your head; don’t knit your fingers behind. Be careful not to yank your head with your hands at any point during the exercise.\n" +
                    "\n" +
                    "3. Lift one leg just off the ground and extend it out.\n" +
                    "\n" +
                    "4. Lift the other leg and bend your knee towards your chest.\n" +
                    "\n" +
                    "5. As you do so twist through your core so the opposite arm comes towards the raised knee. You don’t need to touch elbow to knee, instead focus on moving through your core as you turn your torso. Your elbow should stay in same position relative to your head throughout – the turn that brings it closer to the knee comes from your core. It might be best to think shoulder to knee as you move, rather than elbow to knee.\n" +
                    "\n" +
                    "6. Lower your leg and arm at the same time while bringing up the opposite two limbs to mirror the movement.\n" +
                    "\n" +
                    "7. Keep on alternating sides until you’ve managed 10 reps on each, aiming for three sets of 10 in total, or add the bicycle crunch into circuit training and just keep going for as long as the timer runs.\n"));}
        {
            hiitWorkoutsList.add(new WorkoutsModel("Dumbbell Thruster", "HIIT", R.drawable.dumbell_thruster, "" +
                    "1. Start by lying on the ground, with your lower back pressed flat into the floor and your head and shoulders raised slightly above it.\n" +
                    "\n" +
                    "2. Place your hands lightly on the sides of your head; don’t knit your fingers behind. Be careful not to yank your head with your hands at any point during the exercise.\n" +
                    "\n" +
                    "3. Lift one leg just off the ground and extend it out.\n" +
                    "\n" +
                    "4. Lift the other leg and bend your knee towards your chest.\n" +
                    "\n" +
                    "5. As you do so twist through your core so the opposite arm comes towards the raised knee. You don’t need to touch elbow to knee, instead focus on moving through your core as you turn your torso. Your elbow should stay in same position relative to your head throughout – the turn that brings it closer to the knee comes from your core. It might be best to think shoulder to knee as you move, rather than elbow to knee.\n" +
                    "\n" +
                    "6. Lower your leg and arm at the same time while bringing up the opposite two limbs to mirror the movement.\n" +
                    "\n" +
                    "7. Keep on alternating sides until you’ve managed 10 reps on each, aiming for three sets of 10 in total, or add the bicycle crunch into circuit training and just keep going for as long as the timer runs.\n"));}
        {
            hiitWorkoutsList.add(new WorkoutsModel("Push Up Burpees", "HIIT", R.drawable.pushup_burpees, "" +
                    "1. Start by lying on the ground, with your lower back pressed flat into the floor and your head and shoulders raised slightly above it.\n" +
                    "\n" +
                    "2. Place your hands lightly on the sides of your head; don’t knit your fingers behind. Be careful not to yank your head with your hands at any point during the exercise.\n" +
                    "\n" +
                    "3. Lift one leg just off the ground and extend it out.\n" +
                    "\n" +
                    "4. Lift the other leg and bend your knee towards your chest.\n" +
                    "\n" +
                    "5. As you do so twist through your core so the opposite arm comes towards the raised knee. You don’t need to touch elbow to knee, instead focus on moving through your core as you turn your torso. Your elbow should stay in same position relative to your head throughout – the turn that brings it closer to the knee comes from your core. It might be best to think shoulder to knee as you move, rather than elbow to knee.\n" +
                    "\n" +
                    "6. Lower your leg and arm at the same time while bringing up the opposite two limbs to mirror the movement.\n" +
                    "\n" +
                    "7. Keep on alternating sides until you’ve managed 10 reps on each, aiming for three sets of 10 in total, or add the bicycle crunch into circuit training and just keep going for as long as the timer runs.\n"));}
        {
            hiitWorkoutsList.add(new WorkoutsModel("Renegade Rows", "HIIT", R.drawable.renegade_rows, "" +
                    "1. Start by lying on the ground, with your lower back pressed flat into the floor and your head and shoulders raised slightly above it.\n" +
                    "\n" +
                    "2. Place your hands lightly on the sides of your head; don’t knit your fingers behind. Be careful not to yank your head with your hands at any point during the exercise.\n" +
                    "\n" +
                    "3. Lift one leg just off the ground and extend it out.\n" +
                    "\n" +
                    "4. Lift the other leg and bend your knee towards your chest.\n" +
                    "\n" +
                    "5. As you do so twist through your core so the opposite arm comes towards the raised knee. You don’t need to touch elbow to knee, instead focus on moving through your core as you turn your torso. Your elbow should stay in same position relative to your head throughout – the turn that brings it closer to the knee comes from your core. It might be best to think shoulder to knee as you move, rather than elbow to knee.\n" +
                    "\n" +
                    "6. Lower your leg and arm at the same time while bringing up the opposite two limbs to mirror the movement.\n" +
                    "\n" +
                    "7. Keep on alternating sides until you’ve managed 10 reps on each, aiming for three sets of 10 in total, or add the bicycle crunch into circuit training and just keep going for as long as the timer runs.\n"));}

        return hiitWorkoutsList;
    }
    private List<WorkoutsModel> initLegsData() {

        legsWorkoutsList = new ArrayList<>();

        //Adding Legs workouts
        {
            legsWorkoutsList.add(new WorkoutsModel("Back Squats", "Legs", R.drawable.back_squats, "" +
                    "1. Start by lying on the ground, with your lower back pressed flat into the floor and your head and shoulders raised slightly above it.\n" +
                    "\n" +
                    "2. Place your hands lightly on the sides of your head; don’t knit your fingers behind. Be careful not to yank your head with your hands at any point during the exercise.\n" +
                    "\n" +
                    "3. Lift one leg just off the ground and extend it out.\n" +
                    "\n" +
                    "4. Lift the other leg and bend your knee towards your chest.\n" +
                    "\n" +
                    "5. As you do so twist through your core so the opposite arm comes towards the raised knee. You don’t need to touch elbow to knee, instead focus on moving through your core as you turn your torso. Your elbow should stay in same position relative to your head throughout – the turn that brings it closer to the knee comes from your core. It might be best to think shoulder to knee as you move, rather than elbow to knee.\n" +
                    "\n" +
                    "6. Lower your leg and arm at the same time while bringing up the opposite two limbs to mirror the movement.\n" +
                    "\n" +
                    "7. Keep on alternating sides until you’ve managed 10 reps on each, aiming for three sets of 10 in total, or add the bicycle crunch into circuit training and just keep going for as long as the timer runs.\n"));}
        {
            legsWorkoutsList.add(new WorkoutsModel("Dumbbell Lunges", "Legs", R.drawable.dumbell_lunges, "" +
                    "1. Start by lying on the ground, with your lower back pressed flat into the floor and your head and shoulders raised slightly above it.\n" +
                    "\n" +
                    "2. Place your hands lightly on the sides of your head; don’t knit your fingers behind. Be careful not to yank your head with your hands at any point during the exercise.\n" +
                    "\n" +
                    "3. Lift one leg just off the ground and extend it out.\n" +
                    "\n" +
                    "4. Lift the other leg and bend your knee towards your chest.\n" +
                    "\n" +
                    "5. As you do so twist through your core so the opposite arm comes towards the raised knee. You don’t need to touch elbow to knee, instead focus on moving through your core as you turn your torso. Your elbow should stay in same position relative to your head throughout – the turn that brings it closer to the knee comes from your core. It might be best to think shoulder to knee as you move, rather than elbow to knee.\n" +
                    "\n" +
                    "6. Lower your leg and arm at the same time while bringing up the opposite two limbs to mirror the movement.\n" +
                    "\n" +
                    "7. Keep on alternating sides until you’ve managed 10 reps on each, aiming for three sets of 10 in total, or add the bicycle crunch into circuit training and just keep going for as long as the timer runs.\n"));}
        {
            legsWorkoutsList.add(new WorkoutsModel("Dumbbell Suitcase Squats", "Legs", R.drawable.dumbell_suitcase_squats, "" +
                    "1. Start by lying on the ground, with your lower back pressed flat into the floor and your head and shoulders raised slightly above it.\n" +
                    "\n" +
                    "2. Place your hands lightly on the sides of your head; don’t knit your fingers behind. Be careful not to yank your head with your hands at any point during the exercise.\n" +
                    "\n" +
                    "3. Lift one leg just off the ground and extend it out.\n" +
                    "\n" +
                    "4. Lift the other leg and bend your knee towards your chest.\n" +
                    "\n" +
                    "5. As you do so twist through your core so the opposite arm comes towards the raised knee. You don’t need to touch elbow to knee, instead focus on moving through your core as you turn your torso. Your elbow should stay in same position relative to your head throughout – the turn that brings it closer to the knee comes from your core. It might be best to think shoulder to knee as you move, rather than elbow to knee.\n" +
                    "\n" +
                    "6. Lower your leg and arm at the same time while bringing up the opposite two limbs to mirror the movement.\n" +
                    "\n" +
                    "7. Keep on alternating sides until you’ve managed 10 reps on each, aiming for three sets of 10 in total, or add the bicycle crunch into circuit training and just keep going for as long as the timer runs.\n"));}
        {
            legsWorkoutsList.add(new WorkoutsModel("Reverse Dumbbell Lunges", "Legs", R.drawable.reverse_dumbbell_lunges, "" +
                    "1. Start by lying on the ground, with your lower back pressed flat into the floor and your head and shoulders raised slightly above it.\n" +
                    "\n" +
                    "2. Place your hands lightly on the sides of your head; don’t knit your fingers behind. Be careful not to yank your head with your hands at any point during the exercise.\n" +
                    "\n" +
                    "3. Lift one leg just off the ground and extend it out.\n" +
                    "\n" +
                    "4. Lift the other leg and bend your knee towards your chest.\n" +
                    "\n" +
                    "5. As you do so twist through your core so the opposite arm comes towards the raised knee. You don’t need to touch elbow to knee, instead focus on moving through your core as you turn your torso. Your elbow should stay in same position relative to your head throughout – the turn that brings it closer to the knee comes from your core. It might be best to think shoulder to knee as you move, rather than elbow to knee.\n" +
                    "\n" +
                    "6. Lower your leg and arm at the same time while bringing up the opposite two limbs to mirror the movement.\n" +
                    "\n" +
                    "7. Keep on alternating sides until you’ve managed 10 reps on each, aiming for three sets of 10 in total, or add the bicycle crunch into circuit training and just keep going for as long as the timer runs.\n"));}

        return legsWorkoutsList;
    }
    private List<WorkoutsModel> initLowerBodyData() {

        lower_BodyWorkoutsList = new ArrayList<>();

        //Adding Lower Body workouts
        {
            lower_BodyWorkoutsList.add(new WorkoutsModel("Box Squats", "Lower Body", R.drawable.box_squats, "" +
                    "1. Start by lying on the ground, with your lower back pressed flat into the floor and your head and shoulders raised slightly above it.\n" +
                    "\n" +
                    "2. Place your hands lightly on the sides of your head; don’t knit your fingers behind. Be careful not to yank your head with your hands at any point during the exercise.\n" +
                    "\n" +
                    "3. Lift one leg just off the ground and extend it out.\n" +
                    "\n" +
                    "4. Lift the other leg and bend your knee towards your chest.\n" +
                    "\n" +
                    "5. As you do so twist through your core so the opposite arm comes towards the raised knee. You don’t need to touch elbow to knee, instead focus on moving through your core as you turn your torso. Your elbow should stay in same position relative to your head throughout – the turn that brings it closer to the knee comes from your core. It might be best to think shoulder to knee as you move, rather than elbow to knee.\n" +
                    "\n" +
                    "6. Lower your leg and arm at the same time while bringing up the opposite two limbs to mirror the movement.\n" +
                    "\n" +
                    "7. Keep on alternating sides until you’ve managed 10 reps on each, aiming for three sets of 10 in total, or add the bicycle crunch into circuit training and just keep going for as long as the timer runs.\n"));}
        {
            lower_BodyWorkoutsList.add(new WorkoutsModel("Bulgarian Split Squats", "Lower Body", R.drawable.bulgarian_split_squats, "" +
                    "1. Start by lying on the ground, with your lower back pressed flat into the floor and your head and shoulders raised slightly above it.\n" +
                    "\n" +
                    "2. Place your hands lightly on the sides of your head; don’t knit your fingers behind. Be careful not to yank your head with your hands at any point during the exercise.\n" +
                    "\n" +
                    "3. Lift one leg just off the ground and extend it out.\n" +
                    "\n" +
                    "4. Lift the other leg and bend your knee towards your chest.\n" +
                    "\n" +
                    "5. As you do so twist through your core so the opposite arm comes towards the raised knee. You don’t need to touch elbow to knee, instead focus on moving through your core as you turn your torso. Your elbow should stay in same position relative to your head throughout – the turn that brings it closer to the knee comes from your core. It might be best to think shoulder to knee as you move, rather than elbow to knee.\n" +
                    "\n" +
                    "6. Lower your leg and arm at the same time while bringing up the opposite two limbs to mirror the movement.\n" +
                    "\n" +
                    "7. Keep on alternating sides until you’ve managed 10 reps on each, aiming for three sets of 10 in total, or add the bicycle crunch into circuit training and just keep going for as long as the timer runs.\n"));}
        {
            lower_BodyWorkoutsList.add(new WorkoutsModel("Jump Squats (Home)", "Lower Body", R.drawable.jump_sqauts_home, "" +
                    "1. Start by lying on the ground, with your lower back pressed flat into the floor and your head and shoulders raised slightly above it.\n" +
                    "\n" +
                    "2. Place your hands lightly on the sides of your head; don’t knit your fingers behind. Be careful not to yank your head with your hands at any point during the exercise.\n" +
                    "\n" +
                    "3. Lift one leg just off the ground and extend it out.\n" +
                    "\n" +
                    "4. Lift the other leg and bend your knee towards your chest.\n" +
                    "\n" +
                    "5. As you do so twist through your core so the opposite arm comes towards the raised knee. You don’t need to touch elbow to knee, instead focus on moving through your core as you turn your torso. Your elbow should stay in same position relative to your head throughout – the turn that brings it closer to the knee comes from your core. It might be best to think shoulder to knee as you move, rather than elbow to knee.\n" +
                    "\n" +
                    "6. Lower your leg and arm at the same time while bringing up the opposite two limbs to mirror the movement.\n" +
                    "\n" +
                    "7. Keep on alternating sides until you’ve managed 10 reps on each, aiming for three sets of 10 in total, or add the bicycle crunch into circuit training and just keep going for as long as the timer runs.\n"));}
        {
            lower_BodyWorkoutsList.add(new WorkoutsModel("Jumping Lunges (Home)", "Lower Body", R.drawable.jumping_lunges_home, "" +
                    "1. Start by lying on the ground, with your lower back pressed flat into the floor and your head and shoulders raised slightly above it.\n" +
                    "\n" +
                    "2. Place your hands lightly on the sides of your head; don’t knit your fingers behind. Be careful not to yank your head with your hands at any point during the exercise.\n" +
                    "\n" +
                    "3. Lift one leg just off the ground and extend it out.\n" +
                    "\n" +
                    "4. Lift the other leg and bend your knee towards your chest.\n" +
                    "\n" +
                    "5. As you do so twist through your core so the opposite arm comes towards the raised knee. You don’t need to touch elbow to knee, instead focus on moving through your core as you turn your torso. Your elbow should stay in same position relative to your head throughout – the turn that brings it closer to the knee comes from your core. It might be best to think shoulder to knee as you move, rather than elbow to knee.\n" +
                    "\n" +
                    "6. Lower your leg and arm at the same time while bringing up the opposite two limbs to mirror the movement.\n" +
                    "\n" +
                    "7. Keep on alternating sides until you’ve managed 10 reps on each, aiming for three sets of 10 in total, or add the bicycle crunch into circuit training and just keep going for as long as the timer runs.\n"));}
        {
            lower_BodyWorkoutsList.add(new WorkoutsModel("Lunges (Home)", "Lower Body", R.drawable.lunges_home, "" +
                    "1. Start by lying on the ground, with your lower back pressed flat into the floor and your head and shoulders raised slightly above it.\n" +
                    "\n" +
                    "2. Place your hands lightly on the sides of your head; don’t knit your fingers behind. Be careful not to yank your head with your hands at any point during the exercise.\n" +
                    "\n" +
                    "3. Lift one leg just off the ground and extend it out.\n" +
                    "\n" +
                    "4. Lift the other leg and bend your knee towards your chest.\n" +
                    "\n" +
                    "5. As you do so twist through your core so the opposite arm comes towards the raised knee. You don’t need to touch elbow to knee, instead focus on moving through your core as you turn your torso. Your elbow should stay in same position relative to your head throughout – the turn that brings it closer to the knee comes from your core. It might be best to think shoulder to knee as you move, rather than elbow to knee.\n" +
                    "\n" +
                    "6. Lower your leg and arm at the same time while bringing up the opposite two limbs to mirror the movement.\n" +
                    "\n" +
                    "7. Keep on alternating sides until you’ve managed 10 reps on each, aiming for three sets of 10 in total, or add the bicycle crunch into circuit training and just keep going for as long as the timer runs.\n"));}
        {
            lower_BodyWorkoutsList.add(new WorkoutsModel("Reverse Lunges (Home)", "Lower Body", R.drawable.reverse_lunges_home, "" +
                    "1. Start by lying on the ground, with your lower back pressed flat into the floor and your head and shoulders raised slightly above it.\n" +
                    "\n" +
                    "2. Place your hands lightly on the sides of your head; don’t knit your fingers behind. Be careful not to yank your head with your hands at any point during the exercise.\n" +
                    "\n" +
                    "3. Lift one leg just off the ground and extend it out.\n" +
                    "\n" +
                    "4. Lift the other leg and bend your knee towards your chest.\n" +
                    "\n" +
                    "5. As you do so twist through your core so the opposite arm comes towards the raised knee. You don’t need to touch elbow to knee, instead focus on moving through your core as you turn your torso. Your elbow should stay in same position relative to your head throughout – the turn that brings it closer to the knee comes from your core. It might be best to think shoulder to knee as you move, rather than elbow to knee.\n" +
                    "\n" +
                    "6. Lower your leg and arm at the same time while bringing up the opposite two limbs to mirror the movement.\n" +
                    "\n" +
                    "7. Keep on alternating sides until you’ve managed 10 reps on each, aiming for three sets of 10 in total, or add the bicycle crunch into circuit training and just keep going for as long as the timer runs.\n"));}
        {
            lower_BodyWorkoutsList.add(new WorkoutsModel("Split Squats", "Lower Body", R.drawable.split_squats, "" +
                    "1. Start by lying on the ground, with your lower back pressed flat into the floor and your head and shoulders raised slightly above it.\n" +
                    "\n" +
                    "2. Place your hands lightly on the sides of your head; don’t knit your fingers behind. Be careful not to yank your head with your hands at any point during the exercise.\n" +
                    "\n" +
                    "3. Lift one leg just off the ground and extend it out.\n" +
                    "\n" +
                    "4. Lift the other leg and bend your knee towards your chest.\n" +
                    "\n" +
                    "5. As you do so twist through your core so the opposite arm comes towards the raised knee. You don’t need to touch elbow to knee, instead focus on moving through your core as you turn your torso. Your elbow should stay in same position relative to your head throughout – the turn that brings it closer to the knee comes from your core. It might be best to think shoulder to knee as you move, rather than elbow to knee.\n" +
                    "\n" +
                    "6. Lower your leg and arm at the same time while bringing up the opposite two limbs to mirror the movement.\n" +
                    "\n" +
                    "7. Keep on alternating sides until you’ve managed 10 reps on each, aiming for three sets of 10 in total, or add the bicycle crunch into circuit training and just keep going for as long as the timer runs.\n"));}
        {
            lower_BodyWorkoutsList.add(new WorkoutsModel("Squats (Home)", "Lower Body", R.drawable.squats_home, "" +
                    "1. Start by lying on the ground, with your lower back pressed flat into the floor and your head and shoulders raised slightly above it.\n" +
                    "\n" +
                    "2. Place your hands lightly on the sides of your head; don’t knit your fingers behind. Be careful not to yank your head with your hands at any point during the exercise.\n" +
                    "\n" +
                    "3. Lift one leg just off the ground and extend it out.\n" +
                    "\n" +
                    "4. Lift the other leg and bend your knee towards your chest.\n" +
                    "\n" +
                    "5. As you do so twist through your core so the opposite arm comes towards the raised knee. You don’t need to touch elbow to knee, instead focus on moving through your core as you turn your torso. Your elbow should stay in same position relative to your head throughout – the turn that brings it closer to the knee comes from your core. It might be best to think shoulder to knee as you move, rather than elbow to knee.\n" +
                    "\n" +
                    "6. Lower your leg and arm at the same time while bringing up the opposite two limbs to mirror the movement.\n" +
                    "\n" +
                    "7. Keep on alternating sides until you’ve managed 10 reps on each, aiming for three sets of 10 in total, or add the bicycle crunch into circuit training and just keep going for as long as the timer runs.\n"));}


        return lower_BodyWorkoutsList;
    }
    private List<WorkoutsModel> initShouldersData() {

        shouldersWorkoutsList = new ArrayList<>();

        //Adding Shoulders workouts
        {
            shouldersWorkoutsList.add(new WorkoutsModel("Dumbbell Press", "Shoulders", R.drawable.dumbbell_press, "" +
                    "1. Start by lying on the ground, with your lower back pressed flat into the floor and your head and shoulders raised slightly above it.\n" +
                    "\n" +
                    "2. Place your hands lightly on the sides of your head; don’t knit your fingers behind. Be careful not to yank your head with your hands at any point during the exercise.\n" +
                    "\n" +
                    "3. Lift one leg just off the ground and extend it out.\n" +
                    "\n" +
                    "4. Lift the other leg and bend your knee towards your chest.\n" +
                    "\n" +
                    "5. As you do so twist through your core so the opposite arm comes towards the raised knee. You don’t need to touch elbow to knee, instead focus on moving through your core as you turn your torso. Your elbow should stay in same position relative to your head throughout – the turn that brings it closer to the knee comes from your core. It might be best to think shoulder to knee as you move, rather than elbow to knee.\n" +
                    "\n" +
                    "6. Lower your leg and arm at the same time while bringing up the opposite two limbs to mirror the movement.\n" +
                    "\n" +
                    "7. Keep on alternating sides until you’ve managed 10 reps on each, aiming for three sets of 10 in total, or add the bicycle crunch into circuit training and just keep going for as long as the timer runs.\n"));}
        {
            shouldersWorkoutsList.add(new WorkoutsModel("Military Press", "Shoulders", R.drawable.military_press, "" +
                    "1. Start by lying on the ground, with your lower back pressed flat into the floor and your head and shoulders raised slightly above it.\n" +
                    "\n" +
                    "2. Place your hands lightly on the sides of your head; don’t knit your fingers behind. Be careful not to yank your head with your hands at any point during the exercise.\n" +
                    "\n" +
                    "3. Lift one leg just off the ground and extend it out.\n" +
                    "\n" +
                    "4. Lift the other leg and bend your knee towards your chest.\n" +
                    "\n" +
                    "5. As you do so twist through your core so the opposite arm comes towards the raised knee. You don’t need to touch elbow to knee, instead focus on moving through your core as you turn your torso. Your elbow should stay in same position relative to your head throughout – the turn that brings it closer to the knee comes from your core. It might be best to think shoulder to knee as you move, rather than elbow to knee.\n" +
                    "\n" +
                    "6. Lower your leg and arm at the same time while bringing up the opposite two limbs to mirror the movement.\n" +
                    "\n" +
                    "7. Keep on alternating sides until you’ve managed 10 reps on each, aiming for three sets of 10 in total, or add the bicycle crunch into circuit training and just keep going for as long as the timer runs.\n"));}
        {
            shouldersWorkoutsList.add(new WorkoutsModel("Shoulder Fly", "Shoulders", R.drawable.shoulder_fly, "" +
                    "1. Start by lying on the ground, with your lower back pressed flat into the floor and your head and shoulders raised slightly above it.\n" +
                    "\n" +
                    "2. Place your hands lightly on the sides of your head; don’t knit your fingers behind. Be careful not to yank your head with your hands at any point during the exercise.\n" +
                    "\n" +
                    "3. Lift one leg just off the ground and extend it out.\n" +
                    "\n" +
                    "4. Lift the other leg and bend your knee towards your chest.\n" +
                    "\n" +
                    "5. As you do so twist through your core so the opposite arm comes towards the raised knee. You don’t need to touch elbow to knee, instead focus on moving through your core as you turn your torso. Your elbow should stay in same position relative to your head throughout – the turn that brings it closer to the knee comes from your core. It might be best to think shoulder to knee as you move, rather than elbow to knee.\n" +
                    "\n" +
                    "6. Lower your leg and arm at the same time while bringing up the opposite two limbs to mirror the movement.\n" +
                    "\n" +
                    "7. Keep on alternating sides until you’ve managed 10 reps on each, aiming for three sets of 10 in total, or add the bicycle crunch into circuit training and just keep going for as long as the timer runs.\n"));}
        {
            shouldersWorkoutsList.add(new WorkoutsModel("Upright Row", "Shoulders", R.drawable.upright_row, "" +
                    "1. Start by lying on the ground, with your lower back pressed flat into the floor and your head and shoulders raised slightly above it.\n" +
                    "\n" +
                    "2. Place your hands lightly on the sides of your head; don’t knit your fingers behind. Be careful not to yank your head with your hands at any point during the exercise.\n" +
                    "\n" +
                    "3. Lift one leg just off the ground and extend it out.\n" +
                    "\n" +
                    "4. Lift the other leg and bend your knee towards your chest.\n" +
                    "\n" +
                    "5. As you do so twist through your core so the opposite arm comes towards the raised knee. You don’t need to touch elbow to knee, instead focus on moving through your core as you turn your torso. Your elbow should stay in same position relative to your head throughout – the turn that brings it closer to the knee comes from your core. It might be best to think shoulder to knee as you move, rather than elbow to knee.\n" +
                    "\n" +
                    "6. Lower your leg and arm at the same time while bringing up the opposite two limbs to mirror the movement.\n" +
                    "\n" +
                    "7. Keep on alternating sides until you’ve managed 10 reps on each, aiming for three sets of 10 in total, or add the bicycle crunch into circuit training and just keep going for as long as the timer runs.\n"));}

        return shouldersWorkoutsList;
    }
    private List<WorkoutsModel> initTricepsData() {

        tricepsWorkoutsList = new ArrayList<>();

        //Adding Triceps workouts
        {
            tricepsWorkoutsList.add(new WorkoutsModel("Tricep Kickback", "Shoulders", R.drawable.tricep_kickback, "" +
                    "1. Start by lying on the ground, with your lower back pressed flat into the floor and your head and shoulders raised slightly above it.\n" +
                    "\n" +
                    "2. Place your hands lightly on the sides of your head; don’t knit your fingers behind. Be careful not to yank your head with your hands at any point during the exercise.\n" +
                    "\n" +
                    "3. Lift one leg just off the ground and extend it out.\n" +
                    "\n" +
                    "4. Lift the other leg and bend your knee towards your chest.\n" +
                    "\n" +
                    "5. As you do so twist through your core so the opposite arm comes towards the raised knee. You don’t need to touch elbow to knee, instead focus on moving through your core as you turn your torso. Your elbow should stay in same position relative to your head throughout – the turn that brings it closer to the knee comes from your core. It might be best to think shoulder to knee as you move, rather than elbow to knee.\n" +
                    "\n" +
                    "6. Lower your leg and arm at the same time while bringing up the opposite two limbs to mirror the movement.\n" +
                    "\n" +
                    "7. Keep on alternating sides until you’ve managed 10 reps on each, aiming for three sets of 10 in total, or add the bicycle crunch into circuit training and just keep going for as long as the timer runs.\n"));}


        return tricepsWorkoutsList;
    }
    private List<WorkoutsModel> initUpperBodyData() {

        upper_BodyWorkoutsList = new ArrayList<>();

        //Adding Upper Body workouts
        {
            upper_BodyWorkoutsList.add(new WorkoutsModel("Declined Push Ups (Home) ", "Upper Body", R.drawable.decline_pushups_home, "" +
                    "1. Start by lying on the ground, with your lower back pressed flat into the floor and your head and shoulders raised slightly above it.\n" +
                    "\n" +
                    "2. Place your hands lightly on the sides of your head; don’t knit your fingers behind. Be careful not to yank your head with your hands at any point during the exercise.\n" +
                    "\n" +
                    "3. Lift one leg just off the ground and extend it out.\n" +
                    "\n" +
                    "4. Lift the other leg and bend your knee towards your chest.\n" +
                    "\n" +
                    "5. As you do so twist through your core so the opposite arm comes towards the raised knee. You don’t need to touch elbow to knee, instead focus on moving through your core as you turn your torso. Your elbow should stay in same position relative to your head throughout – the turn that brings it closer to the knee comes from your core. It might be best to think shoulder to knee as you move, rather than elbow to knee.\n" +
                    "\n" +
                    "6. Lower your leg and arm at the same time while bringing up the opposite two limbs to mirror the movement.\n" +
                    "\n" +
                    "7. Keep on alternating sides until you’ve managed 10 reps on each, aiming for three sets of 10 in total, or add the bicycle crunch into circuit training and just keep going for as long as the timer runs.\n"));}
        {
            upper_BodyWorkoutsList.add(new WorkoutsModel("Inclined Push Ups (Home) ", "Upper Body", R.drawable.incline_pushups_home, "" +
                    "1. Start by lying on the ground, with your lower back pressed flat into the floor and your head and shoulders raised slightly above it.\n" +
                    "\n" +
                    "2. Place your hands lightly on the sides of your head; don’t knit your fingers behind. Be careful not to yank your head with your hands at any point during the exercise.\n" +
                    "\n" +
                    "3. Lift one leg just off the ground and extend it out.\n" +
                    "\n" +
                    "4. Lift the other leg and bend your knee towards your chest.\n" +
                    "\n" +
                    "5. As you do so twist through your core so the opposite arm comes towards the raised knee. You don’t need to touch elbow to knee, instead focus on moving through your core as you turn your torso. Your elbow should stay in same position relative to your head throughout – the turn that brings it closer to the knee comes from your core. It might be best to think shoulder to knee as you move, rather than elbow to knee.\n" +
                    "\n" +
                    "6. Lower your leg and arm at the same time while bringing up the opposite two limbs to mirror the movement.\n" +
                    "\n" +
                    "7. Keep on alternating sides until you’ve managed 10 reps on each, aiming for three sets of 10 in total, or add the bicycle crunch into circuit training and just keep going for as long as the timer runs.\n"));}
        {
            upper_BodyWorkoutsList.add(new WorkoutsModel("Push Ups (Home) ", "Upper Body", R.drawable.pushups_home, "" +
                    "1. Start by lying on the ground, with your lower back pressed flat into the floor and your head and shoulders raised slightly above it.\n" +
                    "\n" +
                    "2. Place your hands lightly on the sides of your head; don’t knit your fingers behind. Be careful not to yank your head with your hands at any point during the exercise.\n" +
                    "\n" +
                    "3. Lift one leg just off the ground and extend it out.\n" +
                    "\n" +
                    "4. Lift the other leg and bend your knee towards your chest.\n" +
                    "\n" +
                    "5. As you do so twist through your core so the opposite arm comes towards the raised knee. You don’t need to touch elbow to knee, instead focus on moving through your core as you turn your torso. Your elbow should stay in same position relative to your head throughout – the turn that brings it closer to the knee comes from your core. It might be best to think shoulder to knee as you move, rather than elbow to knee.\n" +
                    "\n" +
                    "6. Lower your leg and arm at the same time while bringing up the opposite two limbs to mirror the movement.\n" +
                    "\n" +
                    "7. Keep on alternating sides until you’ve managed 10 reps on each, aiming for three sets of 10 in total, or add the bicycle crunch into circuit training and just keep going for as long as the timer runs.\n"));}
        {
            upper_BodyWorkoutsList.add(new WorkoutsModel("Tricep Dips (Home) ", "Upper Body", R.drawable.tricep_dips_home, "" +
                    "1. Start by lying on the ground, with your lower back pressed flat into the floor and your head and shoulders raised slightly above it.\n" +
                    "\n" +
                    "2. Place your hands lightly on the sides of your head; don’t knit your fingers behind. Be careful not to yank your head with your hands at any point during the exercise.\n" +
                    "\n" +
                    "3. Lift one leg just off the ground and extend it out.\n" +
                    "\n" +
                    "4. Lift the other leg and bend your knee towards your chest.\n" +
                    "\n" +
                    "5. As you do so twist through your core so the opposite arm comes towards the raised knee. You don’t need to touch elbow to knee, instead focus on moving through your core as you turn your torso. Your elbow should stay in same position relative to your head throughout – the turn that brings it closer to the knee comes from your core. It might be best to think shoulder to knee as you move, rather than elbow to knee.\n" +
                    "\n" +
                    "6. Lower your leg and arm at the same time while bringing up the opposite two limbs to mirror the movement.\n" +
                    "\n" +
                    "7. Keep on alternating sides until you’ve managed 10 reps on each, aiming for three sets of 10 in total, or add the bicycle crunch into circuit training and just keep going for as long as the timer runs.\n"));}

        return upper_BodyWorkoutsList;
    }




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(WorkoutsViewModel.class);
        // TODO: Use the ViewModel
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
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(USER_PREFS,MODE_PRIVATE);
        String picture = sharedPreferences.getString(PICTURE,"");
        String firstName = sharedPreferences.getString(FIRST_NAME,"");
        String lastName = sharedPreferences.getString(LAST_NAME,"");


        userName.setText("WORKOUTS");

        profilePicImage.setImageBitmap(StringToBitMap(picture));
    }

    @Override
    public void onWorkoutClick(int position) {

        Log.d(TAG, "Clicked Clicked");
        Intent intent = new Intent(getContext(), WorkoutPage.class);

        intent.putExtra("workout_name", absWorkoutsList.get(position).getWorkout_name());
        intent.putExtra("workout_zone", absWorkoutsList.get(position).getWorkout_zone());
        intent.putExtra("workout_image", absWorkoutsList.get(position).getWorkout_image());
        intent.putExtra("workout_description", absWorkoutsList.get(position).getWorkout_description());

        startActivity(intent);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void onHideAbs(){

    }
}