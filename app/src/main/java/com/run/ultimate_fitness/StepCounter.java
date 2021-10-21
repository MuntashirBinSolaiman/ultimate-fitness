package com.run.ultimate_fitness;

/* Author: Muntashir Bin Solaiman
 *  Last modified: 26-10-2021 */

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class StepCounter extends AppCompatActivity implements SensorEventListener {

    private boolean mRunning;
    private TextView mStepsValue,toolDisplayView , logoutText;
    private SensorManager mSensorManager;
    private ImageView backButtonImage;

//-------------------------------------------------------------------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_step_counter);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mStepsValue = findViewById(R.id.stepsValue);

        logoutText = findViewById(R.id.toolbarLogoutTextView);
        toolDisplayView = findViewById(R.id.toolbarTextView);
        backButtonImage =findViewById(R.id.toolbarBackButton);

        logoutText.setVisibility(View.GONE);
        backButtonImage.setOnClickListener(v -> {
            Intent intent = new Intent(StepCounter.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        toolDisplayView.setText("Step Counter");
    }

    //When the activity is in foreground counts steps by registering the listener
    @Override
    protected void onResume() {
        super.onResume();

        mRunning = true;
        Sensor stepSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (stepSensor == null) {

            Toast.makeText(this, "No Step Counter Sensor !", Toast.LENGTH_SHORT).show();
        } else {

            mSensorManager.registerListener((SensorEventListener) this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    //When the activity is not in foreground stops counting steps by unregistering the listener
    @Override
    protected void onPause() {

        super.onPause();
        mRunning = false;
        mSensorManager.unregisterListener( (SensorEventListener) this );
    }

//-------------------------------------------------------------------------------------------------


    //Listeners to read values from step counter sensor
    // If there are no sensors in the device throws a toast indicating so.
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if( mRunning ){

            mStepsValue.setText( "" + sensorEvent.values[0] );

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}