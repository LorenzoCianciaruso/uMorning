package com.example.umorning.external_services;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GoogleGeocode {

    private double latitude;
    private double longitude;
    private String city;
    private String address;
    private String number;
    private String formattedAddress;

    //TODO se non serve da coordinate a indirizzo, toglierlo

    public GoogleGeocode(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;

        fromCoordinatesToAddress();
    }

    public GoogleGeocode(String address, String city, String country) {

        formattedAddress = address + ",+" + city + ",+" + country;

        formattedAddress = address.replace(" ", "+");

        fromAddressToCoordinates();
    }

    public GoogleGeocode(String address) {
        formattedAddress = address.replace(" ", "+");

        fromAddressToCoordinates();
    }


    private void fromAddressToCoordinates() {

        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + formattedAddress + "&sensor=true&key=AIzaSyDj6lm3eLSuOhG4rLXL66WUBg7C7XEDYcA";

        String result = new HttpRequest().getRequest(url);

        try {
            JSONObject jObject;
            try {
                jObject = new JSONObject(result);
                JSONObject jLocation = jObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                latitude = jLocation.getDouble("lat");
                longitude = jLocation.getDouble("lng");
            } catch (NullPointerException e) {
                latitude = 45.2751;
                longitude = 9.1129;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void fromCoordinatesToAddress() {

        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&sensor=true&key=AIzaSyDj6lm3eLSuOhG4rLXL66WUBg7C7XEDYcA";

        String result = new HttpRequest().getRequest(url);

        try {
            JSONObject jObject = new JSONObject(result);
            JSONArray jArr = jObject.getJSONArray("results");

            JSONObject jRes = jArr.getJSONObject(0);
            formattedAddress = jRes.getString("formatted_address");
            JSONArray jComponents = jRes.getJSONArray("address_components");

            number = jComponents.getJSONObject(0).getString("long_name");
            address = jComponents.getJSONObject(1).getString("long_name");
            city = jComponents.getJSONObject(2).getString("long_name");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getNumber() {
        return number;
    }


}
