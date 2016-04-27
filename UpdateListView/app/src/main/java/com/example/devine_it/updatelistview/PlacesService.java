package com.example.devine_it.updatelistview;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Create request for Places API.
 *
 * @author Karn Shah
 * @Date 10/3/2013
 */
public class PlacesService {

    private String API_KEY;

    public PlacesService(String apikey) {
        this.API_KEY = apikey;
    }

    public void setApiKey(String apikey) {
        this.API_KEY = apikey;
    }

    public JSONObject findPlaces(double latitude, double longitude,
                                 String placeSpacification, String Token) {

        String urlString = makeUrl(latitude, longitude, placeSpacification, Token);

        System.out.println("-----url------" + urlString);

        try {
            if (urlString != null) {
                String json = getJSON(urlString);

                if (json != null) {
                 //   System.out.println("-----json-------" + json);

                    JSONObject object;
                    object = new JSONObject(json);
                    System.out.println("-----jsonobject-------" + object);
                    return object;
                }
            }
        } catch (JSONException ex) {
            Logger.getLogger(PlacesService.class.getName()).log(Level.SEVERE,
                    null, ex);

        }


           /* JSONArray array = object.getJSONArray("results");

            ArrayList<Place> arrayList = new ArrayList<Place>();
            for (int i = 0; i < array.length(); i++) {
                try {
                    Place place = Place
                            .jsonToPontoReferencia((JSONObject) array.get(i));
                    Log.v("Places Services ", "" + place);
                    arrayList.add(place);
                } catch (Exception e) {
                }
            }
            return arrayList;
        }
        }*/
        return null;
    }

    // https://maps.googleapis.com/maps/api/place/search/json?location=28.632808,77.218276&radius=500&types=atm&sensor=false&key=AIzaSyAST1X03wgnl05GRc3HBU0W83i53GvIjtw
    private String makeUrl(double latitude, double longitude, String place, String Token) {
        StringBuilder urlString = new StringBuilder(
                "https://maps.googleapis.com/maps/api/place/search/json?");

        if (Token.equals("")) {
            System.out.println("-------token is null-------" + Token);
            urlString.append("&location=");
            urlString.append(Double.toString(latitude));
            urlString.append(",");
            urlString.append(Double.toString(longitude));
            urlString.append("&radius=10000");
            urlString.append("&types=" + place);
            urlString.append("&sensor=false&key=" + API_KEY);

        } else {
            System.out.println("-------token is not null-------" + Token);
            urlString.append("&location=");
            urlString.append(Double.toString(latitude));
            urlString.append(",");
            urlString.append(Double.toString(longitude));
            urlString.append("&radius=10000");
            urlString.append("&types=" + place);
            urlString.append("&sensor=false&key=" + API_KEY);
            urlString.append("&pagetoken=" + Token);


        }


        return urlString.toString();
    }

    protected String getJSON(String url) {
        return getUrlContents(url);
    }

    private String getUrlContents(String theUrl) {
        StringBuilder content = new StringBuilder();

        try {
            URL url = new URL(theUrl);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()), 8);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}
