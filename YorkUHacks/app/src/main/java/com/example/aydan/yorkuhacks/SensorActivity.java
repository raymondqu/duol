package com.example.aydan.yorkuhacks;

import android.app.Service;
import android.os.Bundle;
import android.app.Activity;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import android.content.Intent;

public class SensorActivity extends Service implements SensorEventListener{

    private Sensor mySensor;
    private SensorManager SM;

    private static int THRESHOLD = 2;

    @Override
    public void onCreate() {
        super.onCreate();


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
    public void onSensorChanged(SensorEvent event) {
        //Log.d("x:", Float.toString(event.values[0]));

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


    }
    //commited at 3:46

    public void sendDirection(int direction){
        Intent returnIntent = new Intent("result");
        returnIntent.putExtra("result", direction);

        LocalBroadcastManager.getInstance(this).sendBroadcast(returnIntent);
        //Log.d("help", "help");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        SM.unregisterListener(this);

    }
    //commited at 5:16 by Aydan
}