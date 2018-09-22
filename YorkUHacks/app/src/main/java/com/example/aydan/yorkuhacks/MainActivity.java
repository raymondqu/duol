package com.example.aydan.yorkuhacks;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.content.Intent;
import android.util.Log;

public class MainActivity extends Activity{

    public Boolean sensorToggled = false;
    public static int TIMING_WINDOW = 1000;

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
            i.putExtra("TIMING_WINDOW", TIMING_WINDOW);
            startActivityForResult(i, 1);
        }

        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                int result=data.getIntExtra("result", 1);

                switch(result) {
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
                    case 0:
                        //fail to swipe in time
                        Log.d("MainActivity", "MISS!");
                        break;
                }
                sensorToggled = false;
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    //commited at 3:46



}