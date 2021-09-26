package com.run.ultimate_fitness;

/* Author: Muntashir Bin Solaiman
 *  Last modified: 26-10-2021 */

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class StepCounter extends AppCompatActivity implements SensorEventListener {

    private boolean mRunning;
    private TextView mStepsValue;
    private SensorManager mSensorManager;

//-------------------------------------------------------------------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mStepsValue = findViewById(R.id.stepsValue);
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