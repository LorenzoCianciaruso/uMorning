package com.example.umorning.external_services;

import com.example.umorning.oldmodel.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Eventbrite {

    String token;


    public Eventbrite(String token) {
        this.token = token;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    List<Event> eventList = new ArrayList<Event>();

    public void getEventbriteOrders() {

                String url = "https://www.eventbriteapi.com/v3/users/me/orders/?token=" + token;
                String response = new HttpRequest().getRequest(url);

                try {
                    JSONObject jObject = new JSONObject(response);
                    JSONArray jsonOrders = jObject.getJSONArray("orders");

                    for (int i = 0; i < jsonOrders.length(); i++) {
                        JSONObject jsonOrder = jsonOrders.getJSONObject(i);
                        JSONObject jsonEvent = jsonOrder.getJSONObject("event");
                        String resource_uri = jsonEvent.getString("resource_uri");
                        Event event = getEventbriteEvent(resource_uri);

                        if (event.getStatus().equals("live")) {
                            eventList.add(event);
                        }else{
                            break;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
    }

    private Event getEventbriteEvent(String resource_uri){

        resource_uri = resource_uri + "?token=" + token;
        String response =new HttpRequest().getRequest(resource_uri);
        try {
            JSONObject jObject = new JSONObject(response);

            JSONObject jsonName = jObject.getJSONObject("name");

            JSONObject jsonOrganizer = jObject.getJSONObject("organizer");

            JSONObject jsonVenue = jObject.getJSONObject("venue");

            JSONObject jsonAddress = jsonVenue.getJSONObject("address");

            JSONObject jsonStart = jObject.getJSONObject("start");


            String name = jsonName.getString("text");
            String organizer = jsonOrganizer.getString("name");
            String country = jsonAddress.getString("country_name");
            String city = jsonAddress.getString("city");
            String address = jsonAddress.getString("address_1");
            String latitude = jsonVenue.getString("latitude");
            String longitude = jsonVenue.getString("longitude");
            String locationName = jsonVenue.getString("name");
            String id = jObject.getString("id");
            String url = jObject.getString("url");
            String start = jsonStart.getString("local");
            String status = jObject.getString("status");

            String[] startTime = start.split("T");
            String date= startTime[0];
            String hour= startTime[1];

            Event event = new Event( name, id, organizer, address, city, country, latitude, longitude, locationName, url, resource_uri,date, hour, status);

            return event;

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

}
