package com.dev.ashish.bluehome;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    BluetoothAdapter mBluetoothAdapter;
    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action!=null&&action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                switch(state){
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "onReceive-STATE_ON: Bluetooth is turned ON");
                        Snackbar.make(findViewById(R.id.main_layout),"Turned ON",Snackbar.LENGTH_SHORT).show();
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive-STATE_OFF: Bluetooth is turned OFF");
                        Snackbar.make(findViewById(R.id.main_layout),"Turned OFF",Snackbar.LENGTH_SHORT).show();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "onReceive-STATE_TURNING_ON: Bluetooth is turning ON");
                        Snackbar.make(findViewById(R.id.main_layout),"Turning ON",Snackbar.LENGTH_SHORT).show();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "onReceive-STATE_TURNING_OFF: Bluetooth is turning OFF");
                        Snackbar.make(findViewById(R.id.main_layout),"Turning OFF",Snackbar.LENGTH_SHORT).show();
                        break;
                    default:
                        break;

                }
            }
        }
    };
    private BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if(action!=null&&action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)){
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch(state){
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "onReceive-CONNECTIBLE: Bluetooth is Connectible");
                        Snackbar.make(findViewById(R.id.main_layout),"Connectible",Snackbar.LENGTH_SHORT).show();
                        break;
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "onReceive-SCAN_MODE_: Bluetooth is Discoverable and Connectible");
                        Snackbar.make(findViewById(R.id.main_layout),"Bluetooth is Discoverable and Connectible",Snackbar.LENGTH_SHORT).show();
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "onReceive-NONE: Bluetooth is invisible");
                        Snackbar.make(findViewById(R.id.main_layout),"Invisible",Snackbar.LENGTH_SHORT).show();
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "onReceive-STATE_CONNECTING: Connecting....");
                        Snackbar.make(findViewById(R.id.main_layout),"Connecting....",Snackbar.LENGTH_SHORT).show();
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "onReceive-STATE_CONNECTED: Connected");
                        Snackbar.make(findViewById(R.id.main_layout),"Connected",Snackbar.LENGTH_SHORT).show();
                        break;
                    default:
                        break;

                }
            }


        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        enableDisableBluetooth();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.disable();
            }
        unregisterReceiver(mBroadcastReceiver1);
        unregisterReceiver(mBroadcastReceiver2);
    }

    public void enableDisableBluetooth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkBTPermissions();
        }
        if(mBluetoothAdapter == null) {
            Log.d(TAG, "enableDisableBluetooth: Device doesn't support Bluetooth");
            Toast.makeText(this, "Device doesn't support Bluetooth", Toast.LENGTH_SHORT).show();
        }
            if (!mBluetoothAdapter.isEnabled()){
                Log.d(TAG,"enableDisableBT:EnablingBT");
                Intent enableBTIntent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivity(enableBTIntent);
                IntentFilter BTIntent=new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                registerReceiver(mBroadcastReceiver1,BTIntent);
                Snackbar.make(findViewById(R.id.main_layout),"Enabled Bluetooth",Snackbar.LENGTH_SHORT).show();
                enableBluetoothDiscovery();

                }


        }

    public void enableBluetoothDiscovery() {
        Log.d(TAG," btnEnableDisable_Discoverable:Making device discoverable for 300 seconds");
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);


        IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReceiver2,intentFilter);
        Snackbar.make(findViewById(R.id.main_layout),"Dicovery enabled for 300 secs",Snackbar.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            }
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }
}

