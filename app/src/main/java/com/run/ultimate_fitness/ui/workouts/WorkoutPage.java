package com.run.ultimate_fitness.ui.workouts;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.run.ultimate_fitness.R;

public class WorkoutPage extends AppCompatActivity {

    private VideoView videoView;
    private String videoPath;
    private Uri uri;
    private MediaController mediaController;
    private TextView txtWorkoutName, txtWorkoutZone, txtWorkoutDescription;

    String workoutName, workoutZone, workoutDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_page);
        getSupportActionBar().hide();

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            workoutName = extras.getString("workout_name");
            workoutZone = extras.getString("workout_zone");
            workoutDescription = extras.getString("workout_description");

        }

        //Creating the video view
        videoView = findViewById(R.id.video_Workout);
        videoPath = "android.resource://" + getPackageName() + "/" + R.raw.push_ups;
        uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);
        videoView.seekTo(400);



        mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        txtWorkoutName = findViewById(R.id.txt_workoutName);
        txtWorkoutName.setText(workoutName);

        txtWorkoutZone = findViewById(R.id.txt_workoutZone);
        txtWorkoutZone.setText(workoutZone);

        txtWorkoutDescription = findViewById(R.id.txt_workoutDescription);
        txtWorkoutDescription.setText(workoutDescription);


    }
}