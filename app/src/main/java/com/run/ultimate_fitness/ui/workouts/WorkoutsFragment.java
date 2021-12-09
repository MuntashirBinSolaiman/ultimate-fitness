package com.run.ultimate_fitness.ui.workouts;

import static android.content.ContentValues.TAG;
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
import com.run.ultimate_fitness.adapters.workout_adapters.*;

import java.util.ArrayList;

public class WorkoutsFragment extends Fragment implements AbsWorkoutsAdapter.OnWorkoutListener, BackWorkoutsAdapter.OnWorkoutListener,
        BicepsWorkoutsAdapter.OnWorkoutListener, HamstringsWorkoutsAdapter.OnWorkoutListener,
        HIITWorkoutsAdapter.OnWorkoutListener, LegsWorkoutsAdapter.OnWorkoutListener, LowerBodyWorkoutsAdapter.OnWorkoutListener,
        ShouldersWorkoutsAdapter.OnWorkoutListener, TricepsWorkoutsAdapter.OnWorkoutListener,
        UpperBodyWorkoutsAdapter.OnWorkoutListener {

    RecyclerView recyclerViewAbs, recyclerViewBack, recyclerViewBiceps, recyclerViewHamstrings, recyclerViewHiit;
    RecyclerView recyclerViewLegs, recyclerViewLower_Body, recyclerViewShoulders, recyclerViewTriceps, recyclerViewUpper_Body;

    ArrayList<WorkoutsModel> absWorkoutsList, backWorkoutsList, bicepsWorkoutsList, hamstringsWorkoutsList, hiitWorkoutsList;
    ArrayList<WorkoutsModel> legsWorkoutsList, lower_BodyWorkoutsList, shouldersWorkoutsList, tricepsWorkoutsList, upper_BodyWorkoutsList;

    private ImageView profilePicImage, bookingImage;
    private TextView userName;
    private ProgressBar progressBar;

    public ImageButton imgHideAbsWorkouts;

    public SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public static final String PICTURE = "picture";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String USER_PREFS = "userPrefs";

    public static final String VISIBLE_ZONE_PREFS = "visibleZonePrefs";
    public static final String ZONE_ABS = "zoneAbs";

    public String zoneAbs = "VISIBLE";
    private String videoPath;


    public static WorkoutsFragment newInstance() {
        return new WorkoutsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workouts, container, false);

        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(VISIBLE_ZONE_PREFS, MODE_PRIVATE);
        zoneAbs = sharedPreferences.getString(ZONE_ABS, "VISIBLE");

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
        recyclerViewAbs.setAdapter(new AbsWorkoutsAdapter(initAbsData(), this));
        recyclerViewBack.setAdapter(new BackWorkoutsAdapter(initBackData(), this));
        recyclerViewBiceps.setAdapter(new BicepsWorkoutsAdapter(initBicepsData(), this));
        recyclerViewHamstrings.setAdapter(new HamstringsWorkoutsAdapter(initHamstringsData(), this));
        recyclerViewHiit.setAdapter(new HIITWorkoutsAdapter(initHiitData(), this));
        recyclerViewLegs.setAdapter(new LegsWorkoutsAdapter(initLegsData(), this));
        recyclerViewLower_Body.setAdapter(new LowerBodyWorkoutsAdapter(initLowerBodyData(), this));
        recyclerViewShoulders.setAdapter(new ShouldersWorkoutsAdapter(initShouldersData(), this));
        recyclerViewTriceps.setAdapter(new TricepsWorkoutsAdapter(initTricepsData(), this));
        recyclerViewUpper_Body.setAdapter(new UpperBodyWorkoutsAdapter(initUpperBodyData(), this));


        return view;
    }

    public void checkVisibility() {
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(VISIBLE_ZONE_PREFS, MODE_PRIVATE);
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

    @Override
    public void onResume() {
        super.onResume();

        progressBar.setVisibility(View.GONE);
        bookingImage.setVisibility(View.VISIBLE);
        checkVisibility();

    }

    private ArrayList<WorkoutsModel> initAbsData() {

        absWorkoutsList = new ArrayList<>();
        //Adding Abs workouts
        {
            //Selecting the workout video
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.bicycle;

            absWorkoutsList.add(new WorkoutsModel("Bicycle", "Abs", R.drawable.bicycle,
                    "Lift one leg just off the ground and extend it out. \n" +
                            "\n" +
                            "Lift the other leg and bend your knee towards your chest. \n" +
                            "\n" +
                            "As you do so twist through your core so the opposite arm comes towards the raised knee. \n" +
                            "\n" +
                            "You don't need to touch elbow to knee, instead focus on moving through your core as you turn your torso. \n" +
                            "\n" +
                            "(4sets//30secs) \n"
                    ,videoPath));
        }
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.flutter_kicks;

            absWorkoutsList.add(new WorkoutsModel("Flutter Kicks", "Abs", R.drawable.flutter_kicks,
                    "A Flutter Kick is an exercise that specifically targets the lower abdominal wall. \n" +
                            "\n" +
                            "You perform this move by lying on your back and using your core to “flutter” your legs up and down.\n" +
                            "\n" +
                            "(3sets//45secs)\n"
                    ,videoPath));
        }
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.forearm_plank;

            absWorkoutsList.add(new WorkoutsModel("Forearm Plank", "Abs", R.drawable.forearm_plank, "" +
                    "1. Assume a push-up position but bend your arms at your elbows so your weight rests on your forearms. \n" +
                    "\n" +
                    "2. Tighten your abs, clench your glutes and keep your body straight from head to heels. \n" +
                    "\n" +
                    "3. Hold as long as you can.", videoPath));
        }
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.in_and_out;

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
                            "6. Repeat the ‘in’, ‘out’ movement for the desired number of repetitions.",
                    videoPath));
        }
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.leg_raises;

            absWorkoutsList.add(new WorkoutsModel("Leg Raises", "Abs", R.drawable.leg_raises,
                    "Lie on your back, legs straight and together. \n" +
                            "\n" +
                            "Keep your legs straight and lift them all the way up to the ceiling until your butt comes off the floor. \n" +
                            "\n" +
                            "Slowly lower your legs back down till they're just above the floor. \n" +
                            "\n" +
                            "Hold for a moment. \n" +
                            "\n" +
                            "Raise your legs back up. Repeat. \n" +
                            "\n" +
                            "(4sets// 15-15reps)\n"
                    ,videoPath));
        }
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.plank_to_forearm_plank;

            absWorkoutsList.add(new WorkoutsModel("High Plank to Low Plank", "Abs", R.drawable.plank_to_forearm_plank,
                    "This exercise is the high plank to the low plank you're going to start in a high plank position arm by arm you lower yourself down to a low plank. \n" +
                            "\n" +
                            "And then push back up to a high plank. (3sets//10reps)\n"
                    ,videoPath));
        }
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.plank;

            absWorkoutsList.add(new WorkoutsModel("Straight Arm Plank", "Abs", R.drawable.plank,
                    "1.Get into plank position by supporting your body weight on your hands and toes.\n" +
                            "\n" +
                            "2.Hands should be placed directly under your shoulders.\n" +
                            "\n" +
                            "3.Keep your abdominals contracted and your back straight, eyes ahead of you.\n" +
                            "\n" +
                            "4.Hold this position for as long as you can, building up to 1 minute.\n",
                    videoPath));
        }
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.reverse_crunch;

            absWorkoutsList.add(new WorkoutsModel("Reverse Crunch", "Abs", R.drawable.reverse_crunch,
                   "The reverse crunch is an intermediate level variation of the popular abdominal crunch exercise. \n" +
                           "\n" +
                           "Your upper body remains on the mat as you contract your abs to draw your legs towards your chest. \n" +
                           "\n" +
                           "It exercises the full length of the rectus abdominis muscle (the six-pack muscle), getting to the deep lower abs. \n" +
                           "\n" +
                           "(4sets//15-20reps)\n"
                    ,videoPath));
        }
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.sit_ups;

            absWorkoutsList.add(new WorkoutsModel("Sit Ups", "Abs", R.drawable.sit_ups,
                    "Lie down on your back, with your feet on the floor, knees bent. \n" +
                            "\n" +
                            "Place your hands on either side of your head in a comfortable position. \n" +
                            "\n" +
                            "Bend your hips and waist to raise your body off the ground. \n" +
                            "\n" +
                            "Lower your body back to the ground into the starting position.\n" +
                            "\n" +
                            "Repeat. \n" +
                            "\n" +
                            "(3sets// 20reps)\n"
                    ,videoPath));
        }
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.toe_touches;

            absWorkoutsList.add(new WorkoutsModel("Toe Touches", "Abs", R.drawable.toe_touches,
                    "Lie on your back holding a dumbbell (or not), and lift your legs until they’re perpendicular to the floor. \n" +
                            "\n" +
                            "Extend your arms, lift your shoulders off the floor and try touching your feet with the dumbbell. \n" +
                            "\n" +
                            "Return to the starting position and repeat. \n" +
                            "\n" +
                            "(3sets// 20reps)\n"
                    ,videoPath));
        }


        return absWorkoutsList;
    }
    private ArrayList<WorkoutsModel> initBackData() {

        backWorkoutsList = new ArrayList<>();

        //Adding Back workouts
        {            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.bent_over_row;

            backWorkoutsList.add(new WorkoutsModel("Bent Over Row", "Back", R.drawable.bent_over_row,
                    "Stand with a shoulder-width stance.\n" +
                            "\n" +
                            "Grab the barbell, wider than shoulder-width, with an overhand grip.\n" +
                            "\n" +
                            "Bending your knees slightly, and your core tight, bend over at the waist keeping your lower back tight.\n" +
                            "\n" +
                            "Bending over until your upper body is at a 45-degree bend or lower, pull the bar up towards your lower chest.\n" +
                            "\n" +
                            "Keep your elbows as close to your sides as possible.\n" +
                            "\n" +
                            "At the top of the movement, you should feel like you are pinching your shoulder blades towards each other.\n" +
                            "\n" +
                            "Pause, and return the barbell to its starting position.\n",
                    videoPath));}

        return backWorkoutsList;
    }
    private ArrayList<WorkoutsModel> initBicepsData() {

        bicepsWorkoutsList = new ArrayList<>();

        //Adding Biceps workouts
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.hammer_curls;

            bicepsWorkoutsList.add(new WorkoutsModel("Hammer Curls", "Biceps", R.drawable.hammer_curls,
                    "Stand up straight with a dumbbell in each hand, holding them alongside you.\n" +
                            "\n" +
                            "Keep your biceps stationary and start bending at your elbows, lifting both dumbbells.\n" +
                            "\n" +
                            "Lift until the dumbbells reach shoulder-level, but don’t actually touch your shoulders. \n" +
                            "\n" +
                            "Hold this contraction briefly, then lower back to the starting position and repeat." ,
                    videoPath));}
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.single_arm_dummbell_culrs;


            bicepsWorkoutsList.add(new WorkoutsModel("Single Arm Dumbbell Curl", "Biceps", R.drawable.single_arm_dummbell_curls,
                    "Bending at the elbow, lift one dumbbell toward your shoulder, rotating your arm as it moves up so that the palm with the dumbbell faces up during the movement and eventually faces the shoulder.\n" +
                            "\n" +
                            "Lower the weight to the starting position and perform the same movement with the other arm.\n" +
                            "\n" +
                            "Continue to alternate until the set is complete.\n" ,
                    videoPath));}


        return bicepsWorkoutsList;
    }
    private ArrayList<WorkoutsModel> initHamstringsData() {

        hamstringsWorkoutsList = new ArrayList<>();

        //Adding Hamstrings workouts
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.romanian_deadlift;

            hamstringsWorkoutsList.add(new WorkoutsModel("Ramanian Deadlifts", "Hamstrings", R.drawable.romanian_deadlift,
                    "The deadlift is a weight training exercise in which a loaded barbell or bar is lifted off the ground to the level of the hips, torso perpendicular to the floor, before being placed back on the ground.\n" +
                            "\n" +
                            "(4sets//8-12reps)\n"
                    ,videoPath));}


        return hamstringsWorkoutsList;
    }
    private ArrayList<WorkoutsModel> initHiitData() {

        hiitWorkoutsList = new ArrayList<>();

        //Adding HIIT workouts
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.burpees_home;

            hiitWorkoutsList.add(new WorkoutsModel("Burpees (Home)", "HIIT", R.drawable.burpees_home,
                    "Place the palms of the hands on the floor in front of the feet, jumps back into a push-up position, in some cases completes one push-up, returns to the squat position, and then jumps up into the air while extending the arms overhead\n" +
                            "\n" +
                            "(3sets/10-12reps)\n"
                    ,videoPath));}
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.dumbbell_thruster;

            hiitWorkoutsList.add(new WorkoutsModel("Dumbbell Thruster", "HIIT", R.drawable.dumbell_thruster,
                    "Stand with your feet hip-width apart, holding a pair of dumbbells in front of your shoulders with your palms facing each other.\n" +
                            "\n" +
                            "Keeping your abs engaged, push your hips back, bend your knees, and lower your body into a squat.\n" +
                            "\n" +
                            "Drive through your heels, extend your hips, and, as you straighten your legs, press the dumbbells directly above your shoulders.\n" +
                            "\n" +
                            "Lower the dumbbells to shoulder height as you return to a squat.",
                    videoPath));}
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.pushup_burpees;

            hiitWorkoutsList.add(new WorkoutsModel("Push Up Burpees", "HIIT", R.drawable.pushup_burpees,
                    "Stand with your feet shoulder-width apart. Squat as deeply as you can and place your hands on the floor. \n" +
                            "\n" +
                            "Kick back into a push-up position. \n" +
                            "\n" +
                            "Do one push-up. \n" +
                            "\n" +
                            "Bring your legs back to a squat and jump up, throwing your hands above your head.\n" +
                            "\n" +
                            "Land and repeat.",
                    videoPath));}
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.renegade_rows;

            hiitWorkoutsList.add(new WorkoutsModel("Renegade Rows", "HIIT", R.drawable.renegade_rows,
                    "Place two dumbbells on the floor shoulder width apart.\n" +
                            "\n" +
                            "Assume a plank position with your feet wider than shoulder-distance apart.\n" +
                            "\n" +
                            "Grasp the dumbbells so your hands are elevated off the floor, maintaining a neutral wrist position. \n" +
                            "\n" +
                            "Drive your right arm through the dumbbell into the floor, stiffen your entire body, and row the left dumbbell up and to the side of your rib cage—your elbow should be pointed up and back. \n" +
                            "\n" +
                            "Keep your body stable as you slowly lower the dumbbell back to the floor. \n" +
                            "\n" +
                            "(4sets// 10reps each arm)\n"
                    ,videoPath));}

        return hiitWorkoutsList;
    }
    private ArrayList<WorkoutsModel> initLegsData() {

        legsWorkoutsList = new ArrayList<>();

        //Adding Legs workouts
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.back_squats;

            legsWorkoutsList.add(new WorkoutsModel("Back Squats", "Legs", R.drawable.back_squats,
                    "Safely load the barbell onto your traps and shoulders. \n" +
                            "\n" +
                            "Stand with your feet shoulder-width apart, toes slightly out, core braced, and chest up.\n" +
                            "\n" +
                            "Initiate a basic squat movement — hips back, knees bent, ensuring they fall out, not in. \n" +
                            "\n" +
                            "Pause when your thighs reach about parallel to the ground.\n" +
                            "\n" +
                            "Push through your entire foot to return to start.",
                    videoPath));}
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.dumbbell_lunges;

            legsWorkoutsList.add(new WorkoutsModel("Dumbbell Lunges", "Legs", R.drawable.dumbell_lunges,
                    "Stand upright with dumbbells at your side, palms facing your body.\n" +
                            "\n" +
                            "Lunge forward as far as you can with your right leg, bending your trailing knee so it almost brushes the floor. \n" +
                            "\n" +
                            "Use the heel of your right foot to push your upper body back to the starting position. Repeat with the opposite leg.",
                    videoPath));}
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.dumbell_suitcase_squats;

            legsWorkoutsList.add(new WorkoutsModel("Dumbbell Suitcase Squats", "Legs", R.drawable.dumbell_suitcase_squats,
                    "Hold a dumbbell in one hand and stand with your feet shoulder-width apart and toes pointing slightly out.\n" +
                            "\n" +
                            "Push your hips back and squat down until your thighs are at least parallel to floor, keeping your heels flat on the ground, chest up, and knees out. \n" +
                            "\n" +
                            "Keep the weight at your side. Pause and return to starting position.",
                    videoPath));}
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.reverse_dumbell_lunges;

            legsWorkoutsList.add(new WorkoutsModel("Reverse Dumbbell Lunges", "Legs", R.drawable.reverse_dumbbell_lunges,
                    "Perform the dumbbell reverse lunge in the standing position with a dumbbell firmly grasped in each hand and your feet set shoulder-width apart. \n" +
                            "\n" +
                            "Hold your torso upright and take a big step backwards, lowering your rear knee to the ground. \n" +
                            "\n" +
                            "Return to the standing position and repeat.\n" +
                            "\n" +
                            "Alternate legs for a full leg workout.",
                    videoPath));}

        return legsWorkoutsList;
    }
    private ArrayList<WorkoutsModel> initLowerBodyData() {

        lower_BodyWorkoutsList = new ArrayList<>();

        //Adding Lower Body workouts
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.box_squats;

            lower_BodyWorkoutsList.add(new WorkoutsModel("Box Squats", "Lower Body", R.drawable.box_squats,
                    "Perform box squats by using a wide stance with your feet slightly beyond shoulder-width apart. \n" +
                            "\n" +
                            "Take a deep breath, brace your core, and unrack the barbell. \n" +
                            "\n" +
                            "While resting the barbell on your upper back, lower your body until you are sitting on the plyometric box at the bottom of the squat, then return to standing." ,
                    videoPath));}
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.bulgarian_split_squats;

            lower_BodyWorkoutsList.add(new WorkoutsModel("Bulgarian Split Squats", "Lower Body", R.drawable.bulgarian_split_squats,
                    "Stand 2 to 3 feet in front of a knee-high platform. Extend your right leg behind you and rest your toes on the bench. \n" +
                            "\n" +
                            "Toes can be flat or tucked, according to personal preference. \n" +
                            "\n" +
                            "Square your hips and shoulders.\n" +
                            "\n" +
                            "Keeping your torso upright, slowly lower your right knee toward the floor. Your front knee will form approximately a 90-degree angle (it may go farther, depending on your mobility; just make sure your.\n" +
                            "\n" +
                            "Don’t shift forward on the toes or exaggerate pushing through the heel.\n" +
                            "\n" +
                            "Reverse the move and return to the starting position.",
                    videoPath));}
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.jump_squats_home;

            lower_BodyWorkoutsList.add(new WorkoutsModel("Jump Squats (Home)", "Lower Body", R.drawable.jump_sqauts_home,
                    "Stand with your feet shoulder-width apart. \n" +
                            "\n" +
                            "Start by doing a regular squat, engage your core, and jump up explosively. \n" +
                            "\n" +
                            "When you land, lower your body back into the squat position to complete one rep. Make sure you land with your entire foot"
                    ,videoPath));}
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.jumping_lunges_home;

            lower_BodyWorkoutsList.add(new WorkoutsModel("Jumping Lunges (Home)", "Lower Body", R.drawable.jumping_lunges_home,
                    "Jump up, quickly switching the position of your feet while mid-air so your right leg moves back behind you and your left leg comes forward. \n" +
                            "\n" +
                            "To help you move explosively, propel your arms into the air while you jump. Gently land back on the floor in a basic lunge position\n"
                    ,videoPath));}
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.lunges_home;

            lower_BodyWorkoutsList.add(new WorkoutsModel("Lunges (Home)", "Lower Body", R.drawable.lunges_home,
                    "A lunge can refer to any position of the human body where one leg is positioned forward with knee bent and foot flat on the ground while the other leg is positioned behind. \n" +
                            "\n" +
                            "In contrast to the split squat exercise, during the lunge the rear leg is also activated\n"
                    ,videoPath));}
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.reverse_lunges_home;

            lower_BodyWorkoutsList.add(new WorkoutsModel("Reverse Lunges (Home)", "Lower Body", R.drawable.reverse_lunges_home,
                    "Start by standing straight and bracing your core muscles. \n" +
                            "\n" +
                            "Then take a giant step backwards with your left foot. \n" +
                            "\n" +
                            "Bend your right knee until it's at 90°, and lower your left knee until it is also bent at a right angle. \n" +
                            "\n" +
                            "Then push back up and return to the starting position."
                    ,videoPath));}
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.split_squats;

            lower_BodyWorkoutsList.add(new WorkoutsModel("Split Squats", "Lower Body", R.drawable.split_squats,
                    "From a standing position, take a long step forwards as if performing a lunge. \n" +
                            "\n" +
                            "The heel of your back foot should be raised. \n" +
                            "\n" +
                            "Keeping your torso straight, lower slowly until your back knee almost touches the floor, then push back up. \n" +
                            "\n" +
                            "Complete all your reps on one leg, then switch to the other",
                    videoPath));}
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.squats_home;

            lower_BodyWorkoutsList.add(new WorkoutsModel("Squats (Home)", "Lower Body", R.drawable.squats_home,
                    "A squat is a strength exercise in which the trainee lowers their hips from a standing position and then stands back up.\n" +
                            "\n" +
                            "During the descent of a squat, the hip and knee joints flex while the ankle joint dorsiflexes; conversely the hip and knee joints extend and the ankle joint plantar-flexes when standing up.",
                    videoPath));}


        return lower_BodyWorkoutsList;
    }
    private ArrayList<WorkoutsModel> initShouldersData() {

        shouldersWorkoutsList = new ArrayList<>();

        //Adding Shoulders workouts
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.dumbbell_press;

            shouldersWorkoutsList.add(new WorkoutsModel("Dumbbell Press", "Shoulders", R.drawable.dumbbell_press,
                    "Bend down with your knees to pick up the dumbbells.\n" +
                            "\n" +
                            "Stand with your feet shoulder-width apart and raise the dumbbells to shoulder height.\n" +
                            "\n" +
                            "Once you have the correct stance, begin pressing the dumbbells above your head until your arms fully extend. \n" +
                            "\n" +
                            "Complete the desired number of reps.",
                    videoPath));}
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.military_press;

            shouldersWorkoutsList.add(new WorkoutsModel("Military Press", "Shoulders", R.drawable.military_press,
                    "Tighten your core, squeeze your shoulder blades together, and press the barbell overhead as you exhale.\n" +
                            "\n" +
                            "Continue to press until your arms are locked out. \n" +
                            "\n" +
                            "This movement should feel like you are pressing your head through the “window” made by your arms. \n" +
                            "\n" +
                            "Engage your back muscles and, with control, lower the barbell back to the front-rack position while inhaling.  \n" +
                            "\n" +
                            "Repeat these steps to do more reps or place the bar back on the power rack to end this exercise.\n",
                    videoPath));}
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.shoulder_fly;

            shouldersWorkoutsList.add(new WorkoutsModel("Shoulder Fly", "Shoulders", R.drawable.shoulder_fly,
                    "Stand up tall with your feet shoulder-width apart. \n" +
                            "\n" +
                            "Keep 1 dumbbell in each hand.\n" +
                            "\n" +
                            "Bring your arms up straight in front of you so they're at chest level, palms facing each other.\n" +
                            "\n" +
                            "Extend arms out to the sides, until your arms are extended.\n" +
                            "\n" +
                            "Bring them back to centre.\n",
                    videoPath));}
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.upright_row;

            shouldersWorkoutsList.add(new WorkoutsModel("Upright Row", "Shoulders", R.drawable.upright_row,
                    "Breathe in and brace the abdominals. Keep your back straight, chest up, and eyes focused forward.\n" +
                            "\n" +
                            "Lift the barbell straight up (toward the chin) as you exhale. \n" +
                            "\n" +
                            "Pause at the top of the lift. \n" +
                            "\n" +
                            "Lower the barbell as you inhale, returning it to the starting position. \n" +
                            "\n" +
                            "(3sets//12reps)\n"
                    ,videoPath));}

        return shouldersWorkoutsList;
    }
    private ArrayList<WorkoutsModel> initTricepsData() {

        tricepsWorkoutsList = new ArrayList<>();

        //Adding Triceps workouts
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.tricep_kickback;

            tricepsWorkoutsList.add(new WorkoutsModel("Tricep Kickback", "Shoulders", R.drawable.tricep_kickback,
                    "Place one hand on your thigh for support. \n" +
                            "\n" +
                            "On an exhale, engage your triceps as you slowly extend your arm back as far as you can, keeping your arm in tight by your side. \n" +
                            "\n" +
                            "Pause here, then inhale as your return your arm to the starting position. \n" +
                            "\n" +
                            "(3 sets// 10-15 reps)\n"
                    ,videoPath));}


        return tricepsWorkoutsList;
    }
    private ArrayList<WorkoutsModel> initUpperBodyData() {

        upper_BodyWorkoutsList = new ArrayList<>();

        //Adding Upper Body workouts
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.declined_pushups_home;

            upper_BodyWorkoutsList.add(new WorkoutsModel("Declined Push Ups (Home) ", "Upper Body", R.drawable.decline_pushups_home,
                    "An incline pushup is an elevated form of a traditional pushup. \n" +
                            "\n" +
                            "Your upper body is elevated with an exercise box or other piece of equipment. \n" +
                            "\n" +
                            "While traditional pushups work your chest, arms, and shoulders, incline pushups take some of the pressure off your arms and shoulders to give you a solid chest workout.",
                    videoPath));}
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.inclined_pushups_home;

            upper_BodyWorkoutsList.add(new WorkoutsModel("Inclined Push Ups (Home) ", "Upper Body", R.drawable.incline_pushups_home,
                    "    An incline pushup is an elevated form of a traditional pushup. \n" +
                            "    Your upper body is elevated with an exercise box or other piece of equipment. \n" +
                            "    While traditional pushups work your chest, arms, and shoulders, incline pushups take some of the pressure off your arms and shoulders to give you a solid chest workout.\n",
                    videoPath));}
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.pushups_home;

            upper_BodyWorkoutsList.add(new WorkoutsModel("Push Ups (Home) ", "Upper Body", R.drawable.pushups_home, "" +
                    "The decline pushup is a variation of the basic pushup.\n" +
                    " \n" +
                    "It's done with your feet on an elevated surface, which puts your body at a downward angle. \n" +
                    "\n" +
                    "When you do pushups in this position, you work more of your upper pectoral muscles and front shoulders. "
                    ,videoPath));}
        {
            videoPath = "android.resource://com.run.ultimate_fitness/" + R.raw.tricep_dips_home;

            upper_BodyWorkoutsList.add(new WorkoutsModel("Tricep Dips (Home) ", "Upper Body", R.drawable.tricep_dips_home,
                    "Tricep dips can be performed on parallel bars at your gym or even on a playground. \n" +
                            "\n" +
                            "You hold your entire body weight up with your arms extended and feet hovering over the floor, ankles crossed. \n" +
                            "\n" +
                            "Lower your body until your elbows reach a 90-degree angle before returning to your start position. \n",
                    videoPath));}

        return upper_BodyWorkoutsList;
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

    public void loadImage() {
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        String picture = sharedPreferences.getString(PICTURE, "");
        String firstName = sharedPreferences.getString(FIRST_NAME, "");
        String lastName = sharedPreferences.getString(LAST_NAME, "");


        userName.setText("WORKOUTS");

        profilePicImage.setImageBitmap(StringToBitMap(picture));
    }

    @Override
    public void onAbsWorkoutClick(int position) {

        Log.d(TAG, "Clicked Clicked");
        Intent intent = new Intent(getContext(), WorkoutPage.class);

        intent.putExtra("workout_name", absWorkoutsList.get(position).getWorkout_name());
        intent.putExtra("workout_zone", absWorkoutsList.get(position).getWorkout_zone());
        intent.putExtra("workout_image", absWorkoutsList.get(position).getWorkout_image());
        intent.putExtra("workout_description", absWorkoutsList.get(position).getWorkout_description());
        intent.putExtra("video", absWorkoutsList.get(position).getWorkout_video());
        startActivity(intent);
    }

    @Override
    public void onBackWorkoutClick(int position) {

        Log.d(TAG, "Clicked Clicked");
        Intent intent = new Intent(getContext(), WorkoutPage.class);

        intent.putExtra("workout_name", backWorkoutsList.get(position).getWorkout_name());
        intent.putExtra("workout_zone", backWorkoutsList.get(position).getWorkout_zone());
        intent.putExtra("workout_image", backWorkoutsList.get(position).getWorkout_image());
        intent.putExtra("workout_description", backWorkoutsList.get(position).getWorkout_description());
        intent.putExtra("video", backWorkoutsList.get(position).getWorkout_video());


        startActivity(intent);
    }

    @Override
    public void onBicepsWorkoutClick(int position) {

        Log.d(TAG, "Clicked Clicked");
        Intent intent = new Intent(getContext(), WorkoutPage.class);

        intent.putExtra("workout_name", bicepsWorkoutsList.get(position).getWorkout_name());
        intent.putExtra("workout_zone", bicepsWorkoutsList.get(position).getWorkout_zone());
        intent.putExtra("workout_image", bicepsWorkoutsList.get(position).getWorkout_image());
        intent.putExtra("workout_description", bicepsWorkoutsList.get(position).getWorkout_description());
        intent.putExtra("video", bicepsWorkoutsList.get(position).getWorkout_video());

        startActivity(intent);
    }

    @Override
    public void onHamstringsWorkoutClick(int position) {

        Log.d(TAG, "Clicked Clicked");
        Intent intent = new Intent(getContext(), WorkoutPage.class);

        intent.putExtra("workout_name", hamstringsWorkoutsList.get(position).getWorkout_name());
        intent.putExtra("workout_zone", hamstringsWorkoutsList.get(position).getWorkout_zone());
        intent.putExtra("workout_image", hamstringsWorkoutsList.get(position).getWorkout_image());
        intent.putExtra("workout_description", hamstringsWorkoutsList.get(position).getWorkout_description());
        intent.putExtra("video", hamstringsWorkoutsList.get(position).getWorkout_video());

        startActivity(intent);
    }

    @Override
    public void onHIITWorkoutClick(int position) {

        Log.d(TAG, "Clicked Clicked");
        Intent intent = new Intent(getContext(), WorkoutPage.class);

        intent.putExtra("workout_name", hiitWorkoutsList.get(position).getWorkout_name());
        intent.putExtra("workout_zone", hiitWorkoutsList.get(position).getWorkout_zone());
        intent.putExtra("workout_image", hiitWorkoutsList.get(position).getWorkout_image());
        intent.putExtra("workout_description", hiitWorkoutsList.get(position).getWorkout_description());
        intent.putExtra("video", hiitWorkoutsList.get(position).getWorkout_video());

        startActivity(intent);
    }

    @Override
    public void onLegsWorkoutClick(int position) {

        Log.d(TAG, "Clicked Clicked");
        Intent intent = new Intent(getContext(), WorkoutPage.class);

        intent.putExtra("workout_name", legsWorkoutsList.get(position).getWorkout_name());
        intent.putExtra("workout_zone", legsWorkoutsList.get(position).getWorkout_zone());
        intent.putExtra("workout_image", legsWorkoutsList.get(position).getWorkout_image());
        intent.putExtra("workout_description", legsWorkoutsList.get(position).getWorkout_description());
        intent.putExtra("video", legsWorkoutsList.get(position).getWorkout_video());

        startActivity(intent);
    }

    @Override
    public void onLowerBodyWorkoutClick(int position) {

        Log.d(TAG, "Clicked Clicked");
        Intent intent = new Intent(getContext(), WorkoutPage.class);

        intent.putExtra("workout_name", lower_BodyWorkoutsList.get(position).getWorkout_name());
        intent.putExtra("workout_zone", lower_BodyWorkoutsList.get(position).getWorkout_zone());
        intent.putExtra("workout_image", lower_BodyWorkoutsList.get(position).getWorkout_image());
        intent.putExtra("workout_description", lower_BodyWorkoutsList.get(position).getWorkout_description());
        intent.putExtra("video", lower_BodyWorkoutsList.get(position).getWorkout_video());

        startActivity(intent);
    }

    @Override
    public void onShouldersWorkoutClick(int position) {

        Log.d(TAG, "Clicked Clicked");
        Intent intent = new Intent(getContext(), WorkoutPage.class);

        intent.putExtra("workout_name", shouldersWorkoutsList.get(position).getWorkout_name());
        intent.putExtra("workout_zone", shouldersWorkoutsList.get(position).getWorkout_zone());
        intent.putExtra("workout_image", shouldersWorkoutsList.get(position).getWorkout_image());
        intent.putExtra("workout_description", shouldersWorkoutsList.get(position).getWorkout_description());
        intent.putExtra("video", shouldersWorkoutsList.get(position).getWorkout_video());

        startActivity(intent);
    }

    @Override
    public void onTricepsWorkoutClick(int position) {

        Log.d(TAG, "Clicked Clicked");
        Intent intent = new Intent(getContext(), WorkoutPage.class);

        intent.putExtra("workout_name", tricepsWorkoutsList.get(position).getWorkout_name());
        intent.putExtra("workout_zone", tricepsWorkoutsList.get(position).getWorkout_zone());
        intent.putExtra("workout_image", tricepsWorkoutsList.get(position).getWorkout_image());
        intent.putExtra("workout_description", tricepsWorkoutsList.get(position).getWorkout_description());
        intent.putExtra("video", tricepsWorkoutsList.get(position).getWorkout_video());

        startActivity(intent);
    }

    @Override
    public void onUpperBodyWorkoutClick(int position) {

        Log.d(TAG, "Clicked Clicked");
        Intent intent = new Intent(getContext(), WorkoutPage.class);

        intent.putExtra("workout_name", upper_BodyWorkoutsList.get(position).getWorkout_name());
        intent.putExtra("workout_zone", upper_BodyWorkoutsList.get(position).getWorkout_zone());
        intent.putExtra("workout_image", upper_BodyWorkoutsList.get(position).getWorkout_image());
        intent.putExtra("workout_description", upper_BodyWorkoutsList.get(position).getWorkout_description());
        intent.putExtra("video", upper_BodyWorkoutsList.get(position).getWorkout_video());

        startActivity(intent);
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}