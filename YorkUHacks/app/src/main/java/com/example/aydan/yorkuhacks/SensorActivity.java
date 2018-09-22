package com.example.aydan.yorkuhacks;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import android.content.Intent;

public class SensorActivity extends Activity implements SensorEventListener{

    private Sensor mySensor;
    private SensorManager SM;

    private Intent intent;
    private int TIMING_WINDOW;

    private Timer timer;
    private TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = getIntent();
        TIMING_WINDOW = intent.getIntExtra("TIMING_WINDOW", 1000);

        timer = new Timer(true);

        timerTask = new TimerTask(){
            @Override
            public void run() {
                sendDirection(0);
            }
        };

        timer.schedule(timerTask, TIMING_WINDOW);

        // Create our Sensor Manager
        SM = (SensorManager)getSystemService(SENSOR_SERVICE);

        // Accelerometer Sensor
        mySensor = SM.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        // Register sensor Listener
        super.onResume();
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not in use
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.values[0] < -2)
        {
            sendDirection(1);
        }
        if(event.values[2] > 2)
        {
            sendDirection(2);
        }
        if(event.values[0] > 2)
        {
            sendDirection(3);
        }
        if(event.values[2] < -2)
        {
            sendDirection(4);
        }

        //Log.d("y:", Float.toString(event.values[1]));
    }
    //commited at 3:46

    public void sendDirection(int direction){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", direction);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();

        super.onPause();
        SM.unregisterListener(this);
    }
    //commited at 5:16 by Aydan
}