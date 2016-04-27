package com.example.devine_it.updatelistview;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

/**
 * Created by Divine_it on 4/26/2016.
 */


    public class AlertDialogActivity extends Activity
    {

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);

            System.out.println("-----------tttttt-------------");
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);

            builder.setTitle(" network connection is off");
            builder.setMessage(" want to connect Wifi?");

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing but close the dialog
                    startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 0);
                    dialog.dismiss();
                }

            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                    Intent intent = new Intent( AlertDialogActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("Exit me", true);
                    startActivity(intent);
                    finish();

                }
            });

            android.support.v7.app.AlertDialog alert = builder.create();
            alert.show();






        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent intent)
        {
            if(requestCode==0)
            {
                WifiManager wifiManager = (WifiManager)
                        getSystemService(Context.WIFI_SERVICE);

               // getParent().startActivityForResult(0);
                if(!wifiManager.isWifiEnabled());

               // setResult(RESULT_OK);
                finish();

                // new GetPlaces(MainActivity.this, places.toLowerCase().replace(
                // "-", "_")).execute();

                //restart Application here
            }
        }

    }

