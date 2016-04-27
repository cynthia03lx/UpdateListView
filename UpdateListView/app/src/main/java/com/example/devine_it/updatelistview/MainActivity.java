package com.example.devine_it.updatelistview;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends FragmentActivity implements EndlessListView.EndlessListener {


    EndlessListView lv;
    EndlessAdapter adp;
     double lati=0.0;
     double longi=0.0;
    double latitude;
    double longitude;
    int q=0;

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    public static final int NETWORK_STATUS_NOT_CONNECTED=0,NETWORK_STAUS_WIFI=1,NETWORK_STATUS_MOBILE=2;

     ProgressDialog dialog;

    private final String TAG = "tic";

    private String places = "Mosque";
    LocationManager locationManager;
    private Location loc;

    ArrayList<Place> findPlaces;
    JSONObject getjson;
    String pre_token = "";
    String next_token = "";
    ListAdapter adapter;
    JSONArray array;



    String distance = null;
    String duration = null;
    String name;
    String adress;
    // = getResources().getStringArray(R.array.places);


    //comit();

    static int i = 0;
    ListView listView;
    String[] values_names, values_adress,values_new_names,values_new_adress;
    Double[] values_lat, values_long,values_new_lat,values_new_long,values_dist,values_new_dist,values_new_distance;





    public boolean gps_enabled = false;
    public boolean network_enabled = false;

    public LocationManager locManager;
    public LocationListener locationListener;
    public Context context;
    public double latitude1, longitude1;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    ArrayList<Place> items ;
    boolean flag;
    int count=0;
    int p=0;
    ProgressBar b;
    public static EndlessListView myActivity;
    private GoogleMap  googleMap;
    ViewFlipper MyViewFlipper;
    TextView show_duration;
    TextView header;
    LatLng sourcePosition;
    LatLng destPosition;
    ConnectionChangeReceiver conn= new ConnectionChangeReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if( getIntent().getBooleanExtra("Exit me", false)){
            finish();
            return; // add this to prevent from doing unnecessary stuffs
        }
        getConnectivityStatus(this);
        getConnectivityStatusString(this);

//        conn.onReceive(context,getIntent());

        items = new ArrayList<Place>();
         header= (TextView) findViewById(R.id.lb);
        b = (ProgressBar)findViewById(R.id.progressBar1);
          MyViewFlipper = (ViewFlipper)findViewById(R.id.flipper1);
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        googleMap =  fm.getMap();

        lv = (EndlessListView) findViewById(R.id.el);

        adp = new EndlessAdapter(MainActivity.this, items);
        lv.setLoadingView(R.layout.loading_layout);
        lv.setAdapter(adp);
        lv.setListener(MainActivity.this);
      //  LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
      LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("dfds");
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent( Settings.ACTION_SETTINGS);
                    startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton("no", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
        }





       flag=isInternetOn();
        if(flag==false)
        {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Your network connection is off");
            builder.setMessage("Are you want to connect Wifi?");

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
                    // Do nothing
                    dialog.dismiss();

                    System.exit(0);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();







        } else
        {
             dialog = new ProgressDialog(this);
             dialog.setCancelable(false);
              dialog.setMessage("Loading..");
              dialog.isIndeterminate();
              dialog.show();

            if (!startService()) {
                CreateAlert("Error!", "Service Cannot be started");
            } else {
                Toast.makeText(MainActivity.this, "Service Started",
                        Toast.LENGTH_LONG).show();
            }
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MyViewFlipper.setDisplayedChild(1);

                final Place mnotes = (Place) parent
                        .getItemAtPosition(position);
                mnotes.getName();

                TextView header= (TextView) findViewById(R.id.lb);
                googleMap.clear();


                //((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                //  .getMap();

                // Receiving latitude from MainActivity screen
                 latitude =  mnotes.getLatitude();

                // Receiving longitude from MainActivity screen
                 longitude =  mnotes.getLongitude();

                 name = mnotes.getName();
                 adress = mnotes.getVicinity();
                 sourcePosition = new LatLng(lati, longi);

                destPosition = new LatLng(latitude, longitude);

                String url = getDirectionsUrl(sourcePosition, destPosition);

                DownloadTask downloadTask = new DownloadTask();

// Start downloading json data from Google Directions API
                downloadTask.execute(url);

                System.out.println("--------------Distance:" + distance + ", Duration:" + duration);



                Toast.makeText(MainActivity.this,   mnotes.getName()+mnotes.getLatitude(),
                       Toast.LENGTH_LONG).show();
            }
        });

    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

// Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

// Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

// Sensor enabled
        String sensor = "sensor=false";

// Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

// Output format
        String output = "json";

// Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

// Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

// Connecting to url
            urlConnection.connect();

// Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
           // Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String>{

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

// For storing data from web service
            String data = "";

            try{
// Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
// doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

// Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

// Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();


            if(result.size()<1){
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

// Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList();
                lineOptions = new PolylineOptions();

// Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

// Fetching all the points in i-th route
                for(int j=0;j <path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    if(j==0){ // Get distance from the list
                        distance = (String)point.get("distance");
                        continue;
                    }else if(j==1){ // Get duration from the list
                        duration = (String)point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }


                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                googleMap.addMarker(new MarkerOptions().position(destPosition)
                        .title(name).snippet(" Travel  Distance:" + distance + " ,Travel Duration:" + duration)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))).showInfoWindow();
                try {
                    googleMap.setMyLocationEnabled(true);
                }
                catch (SecurityException e) {

                    Log.d("key", e.toString());
                }

                googleMap.moveCamera(CameraUpdateFactory.newLatLng(destPosition));

                // Zoom in the Google Map
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));


                if(distance!=null&&duration!=null)

                    header.setText(name + " Map Location");



// Adding all the points in the route to LineOptions


                    lineOptions.addAll(points);
                    lineOptions.width(5);
                    lineOptions.color(Color.BLUE);


            }
            System.out.println("--------------Distance:" + distance + ", Duration:" + duration);
          //  show_duration.setText("Distance:" + distance + ", Duration:" + duration);

// Drawing polyline in the Google Map for the i-th route
            googleMap.addPolyline(lineOptions);

            //route(sourcePosition, destPosition,"walking");

        }
    }






    @Override
    public void onBackPressed() {

        if(MyViewFlipper.getDisplayedChild()==1)
        {

            distance=null;
            duration=null;
            latitude=0.0;
            longitude=0.0;
            MyViewFlipper.setDisplayedChild(0);
        }else
        {
            finish();
        }

    }

        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if(requestCode==0)
        {
            WifiManager wifiManager = (WifiManager)
                    getSystemService(Context.WIFI_SERVICE);
            if(!wifiManager.isWifiEnabled());

            dialog = new ProgressDialog(this);
            dialog.setCancelable(false);
            dialog.setMessage("Loading..");
            dialog.isIndeterminate();
            dialog.show();
                //currentLocation();
            if (!startService()) {
                CreateAlert("Error!", "Service Cannot be started");
            } else {
                Toast.makeText(MainActivity.this, "Service Started",
                        Toast.LENGTH_LONG).show();
            }

           // new GetPlaces(MainActivity.this, places.toLowerCase().replace(
                      // "-", "_")).execute();

            //restart Application here
        }
    }


    protected void route(LatLng sourcePosition, LatLng destPosition, String mode) {
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                try {
                    Document doc = (Document) msg.obj;
                    GMapV2Direction md = new GMapV2Direction();
                    ArrayList<LatLng> directionPoint = md.getDirection(doc);
                    PolylineOptions rectLine = new PolylineOptions().width(5).color(Color.MAGENTA);

                    for (int i = 0; i < directionPoint.size(); i++) {
                        rectLine.add(directionPoint.get(i));
                    }
                    Polyline polylin = googleMap.addPolyline(rectLine);
                    md.getDurationText(doc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ;
        };

        new GMapV2DirectionAsyncTask(handler, sourcePosition, destPosition, GMapV2Direction.MODE_WALKING).execute();
    }







    @Override
    public void loadData() {


          count += 20;
        p++;

        if (next_token != null) {

          //  b.setVisibility(View.VISIBLE);

           // lv.addNewData(items);

            Toast.makeText(MainActivity.this, "Service Started",
                    Toast.LENGTH_LONG).show();

            new GetPlaces(MainActivity.this, places.toLowerCase().replace(
                    "-", "_")).execute();
        }
        else
        {
            lv.onLoadMoreComplete();
           // lv.setLoadingView(0);
            //b.setVisibility(View.GONE);

        }




    }


    private class GetPlaces extends AsyncTask<Void, Void, ArrayList<Place>> {


      //  private ProgressDialog dialog;
        private Context context;
        private String places;
        private EndlessAdapter adapter;

        ArrayList<Place> arrayList = new ArrayList<Place>();

        public GetPlaces(Context context, String places) {
            this.context = context;
            this.places = places;
        }

        @Override
        protected ArrayList<Place> doInBackground(Void... params) {

            flag=isInternetOn();
            PlacesService service = new PlacesService("AIzaSyCWsMB1jewKDDwPdfQnVvmmEM_9N1HTxRo");
            Log.d("t", service.toString());
          //  getjson = service.findPlaces(loc.getLatitude(),
                   // loc.getLongitude(), places, next_token);


                getjson = service.findPlaces(lati,
                        longi, places, pre_token);



            try {
                if(  getjson!=null) {
                    if (getjson.has("next_page_token")) {
                        next_token = getjson.getString("next_page_token");
                    } else {
                        next_token = null;
                    }
                    array = getjson.getJSONArray("results");


                    pre_token = next_token;


                    System.out.println("-----------preeee-------------" + pre_token);
                    System.out.println("-----------tttttt-------------" + next_token);


                    for (int i = 0; i < array.length(); i++) {
                        try {
                            Place place = Place
                                    .jsonToPontoReferencia((JSONObject) array.get(i));
                            Log.v("Places Services ", "" + place);
                            arrayList.add(place);
                        } catch (Exception e) {
                        }
                    }
                }else
                {
                    System.out.println("---------------");
                }

            } catch (JSONException ex) {
                Logger.getLogger(PlacesService.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
            for (int i = 0; i < arrayList.size(); i++) {

                Place placeDetail = arrayList.get(i);
                Log.e(TAG, "places : " + placeDetail.getName());
            }
            //  }
            //  else
            // {
            Log.d(TAG, arrayList.toString());
            //
            // }
            System.out.println("-----------find-------------" + arrayList);


            return arrayList;


        }

        @Override
        protected void onPostExecute(ArrayList<Place> result) {

            super.onPostExecute(result);


                if (p != 0) {
                    // lv.addNewData(items);
                    adp.clearData();
                    adp.notifyDataSetChanged();
                }


                //  if (dialog.isShowing()) {
                //  dialog.dismiss();
                // }


                //     System.out.println("-----------POST-------------" + result.get(0).getName());

                //    System.out.println("-----------POST11-------------" + result.size());

                values_lat = new Double[result.size()];
                values_long = new Double[result.size()];
                values_dist = new Double[result.size()];
                values_names = new String[result.size()];
                values_adress = new String[result.size()];

                // final ArrayList<String> list = new ArrayList<String>();
                for (int i = 0; i < result.size(); i++) {
                    values_names[i] = result.get(i).getName();
                    values_adress[i] = result.get(i).getVicinity();
                    values_lat[i] = result.get(i).getLatitude();
                    values_long[i] = result.get(i).getLongitude();
                    values_dist[i] = distance(lati, longi, values_lat[i], values_long[i]);


                }
                if (p == 0) {
                    values_new_dist = new Double[values_dist.length];
                    values_new_names = new String[values_dist.length];
                    values_new_adress = new String[values_dist.length];
                    values_new_distance = new Double[values_dist.length];
                    values_new_lat = new Double[values_dist.length];
                    values_new_long = new Double[values_dist.length];

                } else {
                    values_new_dist = Arrays.copyOf(values_new_dist, count + 20);
                    values_new_names = Arrays.copyOf(values_new_names, count + 20);
                    values_new_adress = Arrays.copyOf(values_new_adress, count + 20);
                    values_new_distance = Arrays.copyOf(values_new_distance, count + 20);
                    values_new_lat = Arrays.copyOf(values_new_lat, count + 20);
                    values_new_long = Arrays.copyOf(values_new_long, count + 20);
                    System.out.println("-----------pooiuyy-------------" + values_new_lat.length);
                }
                // values_new_dist=new Double[values_dist.length+count];
                int j = +count;
                int k = 0;
                System.out.println("--jjjj------" + j);
                System.out.println("------gggg----   " + values_new_dist.length);
                for (int i = j; i < values_new_dist.length; i++) {
                    values_new_dist[i] = values_dist[k];
                    values_new_names[i] = values_names[k];
                    values_new_adress[i] = values_adress[k];
                    values_new_distance[i] = values_dist[k];
                    values_new_lat[i] = values_lat[k];
                    values_new_long[i] = values_long[k];


                    System.out.println("-----------POST2222-------------" + values_new_names[k]);
                    k++;
                }

                System.out.println("-------sort index-------" + Arrays.toString(values_new_dist));

                int[] output = new int[values_new_dist.length];

                System.out.println("--length------  " + output.length);
                Map<Double, Integer> map = new TreeMap<Double, Integer>();

                for (int n = 0; n < values_new_dist.length; n++) {
                    map.put(values_new_dist[n] * values_new_dist.length + n, n);
                }

                int n = 0;

                for (Integer index : map.values()) {
                    output[n++] = index;
                }

                System.out.println("-------sort index-------" + Arrays.toString(output));

                System.out.println("-------adresss-------" + Arrays.toString(values_new_adress));
                System.out.println("-------length-------" + values_new_adress.length);
                System.out.println("-------length-------" + output.length);


                for (int i = 0; i < output.length; i++) {
                    items.add(new Place(values_new_names[output[i]], values_new_adress[output[i]], values_new_distance[output[i]], values_new_lat[output[i]], values_new_long[output[i]]));

                }

                System.out.println("-------sort index-------" + items.toString());


                if (next_token != null) {


                    try {
                        flag=isInternetOn();
                        if(flag) {

                            Thread.sleep(1500);
                            new GetPlaces(MainActivity.this, places.toLowerCase().replace(
                                    "-", "_")).execute();
                        }
                        else
                        {
                            dialog.dismiss();
                            startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 0);
                        }


                        // Thread.sleep(1500);
                        // new GetPlaces(MainActivity.this, places.toLowerCase().replace(
                        //   "-", "_")).execute();
                    } catch (Exception E) {

                    }


                    /// Toast.makeText(MainActivity.this, "Service Started",
                    //   Toast.LENGTH_LONG).show();

                    System.out.println("-------cleardata-------" + output.length);


                    count += 20;
                    p++;
                } else {
                    int m = 0;
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                        m++;
                    }
                    if (m > 0) {
                        adp = new EndlessAdapter(MainActivity.this, items);

                        lv.setAdapter(adp);
                        lv.setListener(MainActivity.this);
                    }

                }


                //  adp = new EndlessAdapter(MainActivity.this, items);
                // lv.setLoadingView(R.layout.loading_layout);
                //   lv.setAdapter(adp);
                //  lv.setListener(MainActivity.this);





        }


       @Override
        protected void onPreExecute() {
            super.onPreExecute();

         //  dialog = new ProgressDialog(context);
          // dialog.setCancelable(false);
         //   dialog.setMessage("Loading..");
          //  dialog.isIndeterminate();
         //   dialog.show();



        }
    }











    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            // if connected with internet
           // currentLocation();
          //  new GetPlaces(MainActivity.this, places.toLowerCase().replace(
                //    "-", "_")).execute();



//            Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();

            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {



         //   Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();


            return false;
        }
        return false;
    }


  private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
       dist = dist * 1.609344;
        return (dist);
    }

   /* public static double distance( double lat1, double lon1,  double lat2,  double lon2)
    {     final int earthRadius = 6371;
        double dLat = (float) Math.toRadians(lat2 - lat1);
        double dLon = (float) Math.toRadians(lon2 - lon1);
        double a =
                (float) (Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
                        * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2));
        double c = (float) (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
        double d = earthRadius * c;
        return (d* 1.5001) ;
    }
*/

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    public boolean startService() {
        try {
            // this.locatorService= new
            // Intent(FastMainActivity.this,LocatorService.class);
            // startService(this.locatorService);

            FetchCordinates fetchCordinates = new FetchCordinates();
            fetchCordinates.execute();
            return true;
        } catch (Exception error) {
            return false;
        }

    }


    public AlertDialog CreateAlert(String title, String message) {
        AlertDialog alert = new AlertDialog.Builder(this).create();

        alert.setTitle(title);

        alert.setMessage(message);

        return alert;

    }



    public class FetchCordinates extends AsyncTask<String, Integer, String> {
       // ProgressDialog progDailog;



        public LocationManager mLocationManager;
        public VeggsterLocationListener mVeggsterLocationListener;

        @Override
        protected void onPreExecute() {
            mVeggsterLocationListener = new VeggsterLocationListener();
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, 0, 0,
                        mVeggsterLocationListener);
            } catch (SecurityException e) {

                Log.d("key", e.toString());
            }

        //    progDailog = new ProgressDialog(MainActivity.this);
        /*   progDailog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    FetchCordinates.this.cancel(true);
                }
            });*/
          //  progDailog.setMessage("Loading...");
          //  progDailog.setIndeterminate(true);
          //  progDailog.setCancelable(true);
           // progDailog.show();

        }

        @Override
        protected void onCancelled(){
            System.out.println("Cancelled by user!");
           // progDailog.dismiss();
            try
            {
                mLocationManager.removeUpdates(mVeggsterLocationListener);
            } catch (SecurityException e) {

                Log.d("key", e.toString());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //progDailog.dismiss();

            System.out.println("-----------lat-------------" + lati);
            System.out.println("-----------long-------------" + longi);

            Toast.makeText(MainActivity.this,
                    "LATITUDE :" + lati + " LONGITUDE :" + longi,
                    Toast.LENGTH_LONG).show();

            new GetPlaces(MainActivity.this, places.toLowerCase().replace(
                        "-", "_")).execute();

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            while (lati == 0.0) {

            }
            return null;
        }


    public class VeggsterLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

            int lat = (int) location.getLatitude(); // * 1E6);
            int log = (int) location.getLongitude(); // * 1E6);
            int acc = (int) (location.getAccuracy());

            String info = location.getProvider();
            try {

                // LocatorService.myLatitude=location.getLatitude();

                // LocatorService.myLongitude=location.getLongitude();

                lati = location.getLatitude();
                longi = location.getLongitude();

            } catch (Exception e) {
                // progDailog.dismiss();
                // Toast.makeText(getApplicationContext(),"Unable to get Location"
                // , Toast.LENGTH_LONG).show();
            }

        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i("OnProviderDisabled", "OnProviderDisabled");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.i("onProviderEnabled", "onProviderEnabled");
        }

        @Override
        public void onStatusChanged(String provider, int status,
                                    Bundle extras) {
            Log.i("onStatusChanged", "onStatusChanged");

        }

    }

}






    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        System.out.println("--1111---");
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                System.out.println("--2222---");
                return TYPE_WIFI;
            }
            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = MainActivity.getConnectivityStatus(context);
        String status = null;
        if (conn == MainActivity.TYPE_WIFI) {

            status = "Wifi enabled";
            System.out.println("-----"+status);
        } else if (conn == MainActivity.TYPE_MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == MainActivity.TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        System.out.println("--3333---");
        System.out.println("--hhhhhhh---"+status);
        return status;
    }



}





