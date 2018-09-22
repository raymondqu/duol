package com.example.aydan.yorkuhacks;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.content.Intent;
import android.util.Log;

public class MainActivity extends Activity{

<<<<<<< HEAD
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate.Status;
import com.google.android.gms.nearby.connection.Strategy;


public class MainActivity extends Activity implements SensorEventListener{
    public int direction = 0;

    private Sensor mySensor;
    private SensorManager SM;

    private static final String[] REQUIRED_PERMISSIONS =
            new String[] {
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
            };

    private static final int REQUEST_CODE_REQUIRED_PERMISSIONS = 1;
=======
    public Boolean sensorToggled = false;
>>>>>>> 00109234b414c77557986eaa0d2a9c99d6b6aaad

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventaction = event.getAction();

        if(!sensorToggled){
            sensorToggled = true;
            Intent i=new Intent(this, SensorActivity.class);
            i.putExtra("sensorToggled", sensorToggled);
            startActivityForResult(i, 1);
        }

        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                int direction=data.getIntExtra("result", 1);

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
                sensorToggled = false;
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

<<<<<<< HEAD

    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.values[0] < -4)
        {
            direction = 1;
        }
        if(event.values[2] > 4)
        {
            direction = 2;
        }
        if(event.values[0] > 4)
        {
            direction = 3;
        }
        if(event.values[2] < -4)
        {
            direction = 4;
        }

    }
=======
>>>>>>> 00109234b414c77557986eaa0d2a9c99d6b6aaad
    //commited at 3:46



}