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
    Button mBluetoothEnable;
    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothEnable = findViewById(R.id.bluetooth_enable_main_switch);
        mBluetoothEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableDisableBluetooth();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver1);
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

                }
        if(mBluetoothAdapter.isEnabled()){
            Log.d(TAG,"enableDisableBT:DisablingBT");
            mBluetoothAdapter.disable();
            IntentFilter BTIntent=new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1,BTIntent);
            Snackbar.make(findViewById(R.id.main_layout),"Disabled Bluetooth",Snackbar.LENGTH_SHORT).show();

        }
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

