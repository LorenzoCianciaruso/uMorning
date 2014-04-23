package com.example.umorning.external_services;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.umorning.model.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Eventbrite {

    String token;
    Context cxt;

    public Eventbrite(Context cxt) {

        this.cxt = cxt;

        SharedPreferences prefs = this.cxt.getSharedPreferences("uMorning", 0);
        token = prefs.getString("EventbriteToken", "NotEventbriteLogged");

        if (!token.equals("NotEventbriteLogged")) {
            getEventbriteOrders();
        } else {
            //Toast.makeText(this.cxt.getApplicationContext(), "Not logged in Eventbrite", Toast.LENGTH_LONG).show();
        }

    }

    public List<Event> getEventList() {
        return eventList;
    }

    List<Event> eventList = new ArrayList<Event>();

    private void getEventbriteOrders() {

        String url = "https://www.eventbriteapi.com/v3/users/me/orders/?token=" + token;
        String response = new HttpRequest().getRequest(url);

        try {
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
                    } else {
                        break;
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Event getEventbriteEvent(String resource_uri) {

        resource_uri = resource_uri + "?token=" + token;
        String response = new HttpRequest().getRequest(resource_uri);

        try {
            JSONObject jObject = null;
            try {
                jObject = new JSONObject(response);
            } catch (NullPointerException e) {
                //  Toast.makeText(cxt.getApplicationContext(), "Not logged in Eventbrite", Toast.LENGTH_LONG).show();
            }

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
            double latitude = jsonVenue.getDouble("latitude");
            double longitude = jsonVenue.getDouble("longitude");
            String url = jObject.getString("url");
            String startTime = jsonStart.getString("local");
            String status = jObject.getString("status");

            //parsa la data
            String[] start = startTime.split("T");
            String dateStart = start[0];
            String rest = start[1];

            String[] yearMonthDay = dateStart.split("-");
            int year = Integer.parseInt(yearMonthDay[0]);
            int month = Integer.parseInt(yearMonthDay[1]);
            int day = Integer.parseInt(yearMonthDay[2]);

            String[] hourMinuteSec = rest.split(":");
            int hour = Integer.parseInt(hourMinuteSec[0]);
            int minute = Integer.parseInt(hourMinuteSec[1]);

            Calendar date = new GregorianCalendar(year, month - 1, day, hour, minute);

            Event event = new Event(name, organizer, address, city, country, latitude, longitude, url, date, status, 2);

            return event;

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

}
