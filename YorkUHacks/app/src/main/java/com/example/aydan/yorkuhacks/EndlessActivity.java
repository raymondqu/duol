package com.example.aydan.yorkuhacks;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EndlessActivity extends Activity {

    public static int INIT_WINDOW = 1000; //TODO make this randomized eventually
    public static int START_WINDOW = 2000;
    public static int END_WINDOW = 2000;

    public boolean sensorToggled = false;
    public Intent sensorIntent;

    public TextView updateDefenseText;
    public TextView defenseStatement;

    private Handler roundHandler;

    Vibrator v;

    public int lives = 3;

    public int playState = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_screen);
        sensorIntent = new Intent();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("result"));

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); //initializes vibration

        sensorIntent = new Intent(EndlessActivity.this, SensorActivity.class);
        startService(sensorIntent);

        roundHandler = new Handler();

        roundHandler.post(initRound);

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            int result = intent.getIntExtra("result", 1);

            //Log.d("receiver", "Got message: " + Integer.toString(result));
            if(sensorToggled) {
                switch (result) {
                    case 1:
                        Log.d("EndlessActivity", "LEFT");
                        break;
                    case 2:
                        Log.d("EndlessActivity", "UP");
                        break;
                    case 3:
                        Log.d("EndlessActivity", "RIGHT");
                        break;
                    case 4:
                        Log.d("EndlessActivity", "DOWN");
                        break;
                    case 0:
                        Log.d("EndlessActivity", "RETURN");
                        break;
                }

                //defenseStatement = findViewById(R.id.defenseStatement);

                //displays results to player
                if(playState == result){
                    Log.d("MainActivity", "HIT!");
                    //defenseStatement.setText("Parried!");
                    Toast.makeText(getApplicationContext(), ("YOU PARRIED!"), Toast.LENGTH_SHORT).show();

                }else{
                    Log.d("MainActivity", "WRONG MOTION!");
                    //defenseStatement.setText("Missed!");
                    Toast.makeText(getApplicationContext(), ("YOU WERE HIT!"), Toast.LENGTH_SHORT).show();
                }

                sensorToggled = false;

                //end round early after receiving input
                roundHandler.removeCallbacks(endRound);
                roundHandler.post(endRound);
            }
        }
    };

    //waiting...
    private Runnable initRound = new Runnable(){
        @Override
        public void run(){
            if(lives <= 0){
               //go to resolve screen
            }

            Log.d("EndlessActivity", "STARTING NEW ROUND...");

            roundHandler.postDelayed(startRound, INIT_WINDOW);
        }

    };

    //waits for input
    //myHandler.removeCallbacks(myRunnable); use this to cancel early
    private Runnable startRound = new Runnable(){
        @Override
        public void run(){
            playState = (int)(Math.random()*4+1);

            switch(playState) {
                case 1:
                    Log.d("EndlessActivity", "SWIPE LEFT!");
                    break;
                case 2:
                    Log.d("EndlessActivity", "SWIPE UP!");
                    break;
                case 3:
                    Log.d("EndlessActivity", "SWIPE RIGHT!");
                    break;
                case 4:
                    Log.d("EndlessActivity", "SWIPE DOWN!");
                    break;
                default:
                    Log.d("EndlessActivity", "HOW DOES THIS EVEN HAPPEN");
                    break;
            }

            v.vibrate(100); //vibrate each new "turn"
            sensorToggled = true;

            roundHandler.postDelayed(endRound, START_WINDOW);
        }

    };

    //shows result
    private Runnable endRound = new Runnable(){
        @Override
        public void run(){

            Log.d("EndlessActivity", "ENDING ROUND");

            playState = -1;
            sensorToggled = false;

            roundHandler.postDelayed(initRound, END_WINDOW);
        }

    };


}
