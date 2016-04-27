package com.example.devine_it.updatelistview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapDemoActivity extends FragmentActivity {
    private GoogleMap  googleMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        TextView header= (TextView) findViewById(R.id.lb);
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        googleMap =  fm.getMap();


                //((MapFragment) getFragmentManager().findFragmentById(R.id.map))
              //  .getMap();

        // Receiving latitude from MainActivity screen
        double latitude = getIntent().getDoubleExtra("lat", 0);

        // Receiving longitude from MainActivity screen
        double longitude = getIntent().getDoubleExtra("lng", 0);

         String name = getIntent().getExtras().getString("name", null);
        String adress = getIntent().getExtras().getString("adress", null);


header.setText(name+"Map Location");
        LatLng latLng = new LatLng(latitude,
                longitude);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions().position(latLng)
                .title("My Spot").snippet("This is my spot!")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));
try {
    googleMap.setMyLocationEnabled(true);
}
        catch (SecurityException e) {

            Log.d("key", e.toString());
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));



       /* mMap.addMarker(new MarkerOptions()
                .title(name)
                .position(
                        new LatLng(latitude, longitude))
                .icon(BitmapDescriptorFactory
                        .defaultMarker())
                .snippet(adress));
        CameraPosition cameraPosition = new CameraPosition.Builder()
            .target(new LatLng(latitude, longitude
                    )) // Sets the center of the map to
                    // Mountain View
            .zoom(15) // Sets the zoom
            .tilt(60) // Sets the tilt of the camera to 30 degrees
            .build(); // Creates a CameraPosition from the builder
    mMap.animateCamera(CameraUpdateFactory
            .newCameraPosition(cameraPosition));

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                Context mContext = getApplicationContext();
                LinearLayout info = new LinearLayout(mContext);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(mContext);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(mContext);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

       /* LatLng position = new LatLng(latitude, longitude);

        // Instantiating MarkerOptions class
        MarkerOptions options = new MarkerOptions();

        // Setting position for the MarkerOptions
        options.position(position);

        // Setting title for the MarkerOptions
        options.title("Position");

        // Setting snippet for the MarkerOptions
        options.snippet("Latitude:" + latitude + ",Longitude:" + longitude);

        // Getting Reference to SupportMapFragment of activity_map.xml
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        // Getting reference to google map
        GoogleMap googleMap = fm.getMap();

        // Adding Marker on the Google Map
        googleMap.addMarker(options);

        // Creating CameraUpdate object for position
        CameraUpdate updatePosition = CameraUpdateFactory.newLatLng(position);

        // Creating CameraUpdate object for zoom
        CameraUpdate updateZoom = CameraUpdateFactory.zoomBy(4);

        // Updating the camera position to the user input latitude and longitude
        googleMap.moveCamera(updatePosition);

        // Applying zoom to the marker position
        googleMap.animateCamera(updateZoom);
    }*/
    }
}