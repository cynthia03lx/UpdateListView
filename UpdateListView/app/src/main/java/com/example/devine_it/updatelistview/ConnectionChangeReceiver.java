package com.example.devine_it.updatelistview;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import static android.support.v4.app.ActivityCompat.startActivityForResult;


public class ConnectionChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        int p=0;

        String status = MainActivity.getConnectivityStatusString(context);

        System.out.println("...........status------" + status);
        Toast.makeText(context, status, Toast.LENGTH_LONG).show();


        int i = MainActivity.getConnectivityStatus(context);
        System.out.println("............network state------  "+i);
        if(i==0&&p==0)
        {
            p++;

            System.out.println("-----------preeee-------------");


            Intent j = new Intent(context, AlertDialogActivity.class);
            j.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(j);

        }


    }


}