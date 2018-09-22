package com.example.aydan.yorkuhacks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.MotionEvent;


import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;


public class MainActivity extends Activity implements SensorEventListener{
    public int direction = 0;

    private Sensor mySensor;
    private SensorManager SM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create our Sensor Manager
        SM = (SensorManager)getSystemService(SENSOR_SERVICE);

        // Accelerometer Sensor
        mySensor = SM.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        // Register sensor Listener
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not in use
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventaction = event.getAction();

        switch (eventaction) {
            case MotionEvent.ACTION_DOWN:
                switch(direction) {
                    case 1:
                        Log.d("MainActivity", "LEFT");

                        break;
                    case 2:
                        Log.d("MainActivity", "UP");
                        break;
                    case 3:
                        Log.d("MainActivity", "RIGHT");
                        break;
                    case 4:
                        Log.d("MainActivity", "DOWN");
                        break;
                }
                break;
            default:
                break;

        }
        return true;


    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.values[0] < -2)
        {
            direction = 1;
        }
        if(event.values[2] > 2)
        {
            direction = 2;
        }
        if(event.values[0] > 2)
        {
            direction = 3;
        }
        if(event.values[2] < -2)
        {
            direction = 4;
        }

    }
    //commited at 3:46



}