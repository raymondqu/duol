package com.example.aydan.yorkuhacks;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.content.Intent;
import android.util.Log;


import android.Manifest;


import android.widget.Toast;
import static java.nio.charset.StandardCharsets.UTF_8;
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


public class MainActivity extends Activity{
    public int result = 0;
    /*
    wifi bullshit starts here

     */
    private final String codeName = "testuser";
    private static final String[] REQUIRED_PERMISSIONS =
            new String[] {
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
            };
    private static final int REQUEST_CODE_REQUIRED_PERMISSIONS = 1;

    private static final Strategy STRATEGY = Strategy.P2P_STAR;


    private ConnectionsClient connectionsClient;
    private int myScore;
    private String opponentEndpointId;
    private String opponentName;
    private int opponentScore;
    private int atkChoice;
    private int defChoice;
    private TextView opponentText;
    private TextView statusText;
    private TextView scoreText;
    private Button findOpponentButton;
    private Button disconnectButton;
    private final PayloadCallback payloadCallback =
            new PayloadCallback() {
                @Override
                public void onPayloadReceived(String endpointId, Payload payload) {
                    defChoice = new int(payload.asBytes(), UTF_8);
                }

                @Override
                public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate update) {
                    if (update.getStatus() == Status.SUCCESS && atkChoice != null && defChoice != null) {
                        finishRound();
                    }
                }
            };
    private final EndpointDiscoveryCallback endpointDiscoveryCallback =
            new EndpointDiscoveryCallback() {
                @Override
                public void onEndpointFound(String endpointId, DiscoveredEndpointInfo info) {
                    Log.i("MainActivity", "onEndpointFound: endpoint found, connecting");
                    connectionsClient.requestConnection(codeName, endpointId, connectionLifecycleCallback);
                }

                @Override
                public void onEndpointLost(String endpointId) {}
            };

    private final ConnectionLifecycleCallback connectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
                @Override
                public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo) {
                    Log.i("MainActivity", "onConnectionInitiated: accepting connection");
                    connectionsClient.acceptConnection(endpointId, payloadCallback);
                    opponentName = connectionInfo.getEndpointName();
                }

                @Override
                public void onConnectionResult(String endpointId, ConnectionResolution result) {
                    if (result.getStatus().isSuccess()) {
                        Log.i("MainActivity", "onConnectionResult: connection successful");

                        connectionsClient.stopDiscovery();
                        connectionsClient.stopAdvertising();

                        opponentEndpointId = endpointId;
                        setOpponentName(opponentName);
                        setStatusText("status_connected");
                        setButtonState(true);
                    } else {
                        Log.i("MainActivity", "onConnectionResult: connection failed");
                    }
                }

                @Override
                public void onDisconnected(String endpointId) {
                    Log.i("MainActivity", "onDisconnected: disconnected from the opponent");
                    resetGame();
                }
            };

    @Override
    protected void onStart() {
        super.onStart();

        if (!hasPermissions(this, REQUIRED_PERMISSIONS)) {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_REQUIRED_PERMISSIONS);
        }
    }

    @Override
    protected void onStop() {
        connectionsClient.stopAllEndpoints();
        resetGame();

        super.onStop();
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @CallSuper
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != REQUEST_CODE_REQUIRED_PERMISSIONS) {
            return;
        }

        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "error missing permissions", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
        }
        recreate();
    }

    /** Finds an opponent to play the game with using Nearby Connections. */
    public void findOpponent(View view) {
        startAdvertising();
        startDiscovery();
        setStatusText("Searching...");
        findOpponentButton.setEnabled(false);
    }

    /** Disconnects from the opponent and reset the UI. */
    public void disconnect(View view) {
        connectionsClient.disconnectFromEndpoint(opponentEndpointId);
        resetGame();
    }

    public void makeMove() {
        sendGameChoice(result);


    }

    /** Starts looking for other players using Nearby Connections. */
    private void startDiscovery() {
        // Note: Discovery may fail. To keep this demo simple, we don't handle failures.
        connectionsClient.startDiscovery(
                getPackageName(), endpointDiscoveryCallback, new DiscoveryOptions(STRATEGY));
    }

    private void startAdvertising() {
        // Note: Advertising may fail. To keep this demo simple, we don't handle failures.
        connectionsClient.startAdvertising(
                codeName, getPackageName(), connectionLifecycleCallback, new AdvertisingOptions(STRATEGY));
    }
    private void resetGame() {
        opponentEndpointId = null;
        opponentName = null;
        defChoice = null;
        opponentScore = 0;
        atkChoice = null;
        myScore = 0;

        setOpponentName("no_opponent");
        setStatusText("status_disconnected");
        updateScore(myScore, opponentScore);
        setButtonState(false);
    }

    private void sendGameChoice() {

        connectionsClient.sendPayload(
                opponentEndpointId, Payload.fromBytes(Integer.toString(result).getBytes(UTF_8)));

        setStatusText("placeholder");
        // No changing your mind!
        //setGameChoicesEnabled(false);
    }
    private void finishRound() {
        if (atkChoice != defChoice) {
            // Win!
            Toast toast = Toast.makeText(getApplicationContext(), "Attacker Wins", Toast.LENGTH_LONG);
            toast.show();
            myScore++;


        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Defender wins", Toast.LENGTH_LONG);
            toast.show();
            // Loss

            opponentScore++;
        }
        atkChoice = null;
        defChoice = null;

        updateScore(myScore, opponentScore);

        // Ready for another round

    }

    private void setButtonState(boolean connected) {
        findOpponentButton.setEnabled(true);
        findOpponentButton.setVisibility(connected ? View.GONE : View.VISIBLE);
        disconnectButton.setVisibility(connected ? View.VISIBLE : View.GONE);

        //setGameChoicesEnabled(connected);
    }

    private void setStatusText(String text) {
        statusText.setText(text);
    }

    private void setOpponentName(String opponentName) {
        opponentText.setText(getString(R.string.opponent_name, opponentName));
    }

    private void updateScore(int myScore, int opponentScore) {
        scoreText.setText(getString(R.string.game_score, myScore, opponentScore));
    }

/*
WIFI BULLSHIT ENDS HERE

 */

    public Boolean sensorToggled = false;
    public static int TIMING_WINDOW = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    /*

        opponentText = findViewById(R.id.opponent_name);
    statusText = findViewById(R.id.status);
    scoreText = findViewById(R.id.score);

    TextView nameView = findViewById(R.id.name);
    nameView.setText(getString(R.string.codename, codeName));

    connectionsClient = Nearby.getConnectionsClient(this);

    resetGame();
*/


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
                result=data.getIntExtra("result", 0);

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

    //commited at 8:51 by Aydan



}