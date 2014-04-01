package com.example.umorning.external_services;

import com.example.umorning.model.Event;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

        System.out.println("FFFFFFFFFFFFF dentro fb");

        new Request(
                session,
                "/me/events/attending",
                null,
                HttpMethod.GET,
                new Request.Callback() {
                    public void onCompleted(Response response) {


                        List<String> idList = getEventIdList(response);

                        System.out.println("FFFFFFFFFFFFFFFFFFF completed"+response.toString());

                        for (int i = 0; i < idList.size(); i++) {
                            getEventDetails(idList.get(i));
                        }
                    }
                }
        ).executeAndWait();
        return eventsList;
    }

    private List<String> getEventIdList(Response response) {
        List<String> idList = new ArrayList<String>();

        try {

            GraphObject go  = response.getGraphObject();
            JSONObject  json = go.getInnerJSONObject();
            JSONArray eventsArr = json.getJSONArray("data");

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
                        GraphObject go  = response.getGraphObject();
                        JSONObject  json = go.getInnerJSONObject();

                        try {

                            String name = json.getString("name");
                            String location = json.getString("location");
                            String organizer = json.getJSONObject("owner").getString("name");
                            String startTime = json.getString("start_time");
                            JSONObject jsonVenue = json.getJSONObject("venue");
                            String city = jsonVenue.getString("city");
                            String country = jsonVenue.getString("country");
                            String latitude = jsonVenue.getString("latitude");
                            String longitude = jsonVenue.getString("longitude");

                            String[] start = startTime.split("T");
                            String dateStart= start[0];
                            String rest= start[1];

                            String[] yearMonthDay = dateStart.split("-");
                            int year = Integer.parseInt(yearMonthDay[0]);
                            int month = Integer.parseInt(yearMonthDay[1]);
                            int day = Integer.parseInt(yearMonthDay[2]);

                            String dateTime[] = rest.split("\\+");
                            String hourStart = dateTime[0];
                            int hour = Integer.parseInt(hourStart.split(":")[0]);
                            int minute = Integer.parseInt(hourStart.split(":")[1]);

                            Calendar date = new GregorianCalendar(year,month,day,hour,minute);
                            Event event = new Event( name, organizer, location, city, country, latitude,longitude,null,null,null,date,null);

                            eventsList.add(event);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAndWait();
    }


}
