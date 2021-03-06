package com.example.umorning.external_services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class GoogleTraffic {

    private double startLatitude;
    private double startLongitude;
    private double endLatitude;
    private double endLongitude;
    private String arrivalLocation;
    private int tripDuration;
    private int tripDistance;


    public GoogleTraffic(double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.arrivalLocation = null;

        this.askForTraffic();
    }

    private void askForTraffic() {
        String url;
        if (arrivalLocation == null) {
            url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + startLatitude + "," + startLongitude + "&destinations=" + endLatitude + "," + endLongitude + "&mode=driving&language=en-US&sensor=false&key=AIzaSyDj6lm3eLSuOhG4rLXL66WUBg7C7XEDYcA";
        } else {
            url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + startLatitude + "," + startLongitude + "&destinations=" + arrivalLocation + "&mode=driving&language=en-US&sensor=false&key=AIzaSyDj6lm3eLSuOhG4rLXL66WUBg7C7XEDYcA";
        }
        String result = new HttpRequests().getRequest(url);

        try {
            JSONObject jObject = new JSONObject(result);
            JSONArray jsonRows = jObject.getJSONArray("rows");
            JSONObject jsonElement = (JSONObject) jsonRows.get(0);
            JSONArray jsonElem = jsonElement.getJSONArray("elements");
            JSONObject jsonE = (JSONObject) jsonElem.get(0);
            JSONObject jsonDuration = jsonE.getJSONObject("duration");

            // valore in secondi della durata del viaggio
            tripDuration = jsonDuration.getInt("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getTripDurationInMillis() {
        return tripDuration * 1000;
    }

    public int getTripDistance() {
        return tripDistance;
    }

}
