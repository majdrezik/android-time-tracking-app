package com.example.hourstracker.BroadcastReceivers;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import com.example.hourstracker.MainActivity;
import com.example.hourstracker.ui.Dialogs.ExitAppDialogFragment;

public class GPSReceiver extends BroadcastReceiver {
    private final static String TAG = "LocationProviderChanged";

    boolean isGpsEnabled;
    boolean isNetworkEnabled;



    public GPSReceiver() {
        // EMPTY

        // MyReceiver Close Bracket
    }



    // START OF onReceive
    @Override
    public void onReceive(Context context, Intent intent) {


        // PRIMARY RECEIVER
        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {

            Log.i(TAG, "Location Providers Changed");

            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            Toast.makeText(context, "GPS Enabled: " + isGpsEnabled + " Network Location Enabled: " + isNetworkEnabled, Toast.LENGTH_LONG).show();

            // START DIALOG ACTIVITY
            if (isGpsEnabled || isNetworkEnabled) {
                (new AlertDialog.Builder(context)
                        .setTitle("System warning!")
                        .setMessage("Detected GPS turned off! The location will not be ")
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                        .create()).show();

            }

        }



        // BOOT COMPLETED (REPLICA OF PRIMARY RECEIVER CODE FOR WHEN BOOT_COMPLETED)
        if (intent.getAction().matches("android.intent.action.BOOT_COMPLETED")) {

            Log.i(TAG, "Location Providers Changed Boot");

            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            Toast.makeText(context, "GPS Enabled Boot: " + isGpsEnabled + " Network Location Enabled Boot: " + isNetworkEnabled, Toast.LENGTH_LONG).show();

            // START DIALOG ACTIVITY
            if (isGpsEnabled || isNetworkEnabled) {
                (new AlertDialog.Builder(context)
                        .setTitle("System warning!")
                        .setMessage("Detected GPS turned off! The location will not be ")
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                        .create()).show();

            }

        }



        // onReceive CLOSE BRACKET
    }


}
