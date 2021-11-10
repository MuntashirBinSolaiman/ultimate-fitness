package com.run.ultimate_fitness.ui.workouts;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.run.ultimate_fitness.R;

public class WorkoutPage extends AppCompatActivity {

    private VideoView videoView;
    public ImageButton iconBack;
    private String videoPath;
    private Uri uri;
    private MediaController mediaController;
    private MediaPlayer mediaPlayer;
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
            videoPath = extras.getString("video");
        }

        iconBack = (ImageButton) findViewById(R.id.iconBack);



        //Creating the video view
        videoView = findViewById(R.id.video_Workout);

        System.out.println(videoPath);
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



    public void goBack(View view) {
        onBackPressed();
    }
}