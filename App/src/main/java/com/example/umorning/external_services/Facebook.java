package com.example.umorning.external_services;

import com.example.umorning.model.Event;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Facebook {

    private List<Event> eventsList = new ArrayList<Event>();
    private Session session;

    public Facebook() {
        session = Session.getActiveSession();
    }

    public List<Event> getEventsList() {
        return eventsList;
    }

    public List<Event> getEventList() {

        new Request(
                session,
                "/me/events/attending",
                null,
                HttpMethod.GET,
                new Request.Callback() {
                    public void onCompleted(Response response) {

                        System.out.println("response "+response.toString());
                        //List<String> idList = getEventIdList(response);

                        //for (int i = 0; i < idList.size(); i++) {
                       //     getEventDetails(idList.get(i));

                       // }
                    }
                }
        ).executeAsync();
        return eventsList;
    }

    private List<String> getEventIdList(Response response) {
        List<String> idList = new ArrayList<String>();
        JSONArray eventsArr;
        try {

            eventsArr = response.getGraphObject().getInnerJSONObject().getJSONArray("data");

            for (int i = 0; i < eventsArr.length(); i++) {
                JSONObject item = eventsArr.getJSONObject(i);
                String id = item.getString("id");
                idList.add(id);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return idList;
    }

    private void getEventDetails(String id) {

        new Request(
                session,
                "/" + id,
                null,
                HttpMethod.GET,
                new Request.Callback() {
                    public void onCompleted(Response response) {
                        JSONObject jsonEvent = response.getGraphObject().getInnerJSONObject();
                        try {
                            String name = jsonEvent.getString("name");
                            String location = jsonEvent.getString("location");
                            String organizer = jsonEvent.getJSONObject("owner").getString("name");
                            String startTime = jsonEvent.getString("start_time");
                            JSONObject jsonVenue = jsonEvent.getJSONObject("venue");
                            String city = jsonVenue.getString("city");
                            String country = jsonVenue.getString("country");
                            String latitude = jsonVenue.getString("latitude");
                            String longitude = jsonVenue.getString("longitude");
                            Date date = new Date();
                            //TODO mettere a posto startTime
                            Event event = new Event( name, organizer, location, city, country, latitude,longitude,null,null,null,date,null);

                            System.out.println("name " + name + "org "+organizer + "loc " + location + "lat" + latitude + "long " + longitude);
                            eventsList.add(event);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }
        ).executeAsync();


    }


}
