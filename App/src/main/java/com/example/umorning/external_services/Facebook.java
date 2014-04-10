package com.example.umorning.external_services;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.example.umorning.model.Event;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Facebook {

    private List<Event> eventsList = new ArrayList<Event>();

    public Session getSession() {
        return session;
    }

    private Session session;
    private Context cxt;
    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
    private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
    private boolean pendingPublishReauthorization = false;

    public Facebook(Context cxt) {
        this.cxt = cxt;
        session = Session.getActiveSession();

        if (session == null) {
            System.out.println("restore");
            session = Session.openActiveSessionFromCache(cxt);
        }

    }
//
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
                        List<String> idList = getEventIdList(response);
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

            GraphObject go = response.getGraphObject();
            JSONObject json = go.getInnerJSONObject();
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
                        GraphObject go = response.getGraphObject();
                        JSONObject json = go.getInnerJSONObject();

                        try {

                            String name = json.getString("name");
                            String address = json.getString("location");
                            String organizer = json.getJSONObject("owner").getString("name");
                            String startTime = json.getString("start_time");
                            JSONObject jsonVenue = json.getJSONObject("venue");
                            String city = null;
                            String country = null;
                            double latitude;
                            double longitude;

                            try {
                                city = jsonVenue.getString("city");
                                country = jsonVenue.getString("country");
                                latitude = jsonVenue.getDouble("latitude");
                                longitude = jsonVenue.getDouble("longitude");
                            } catch (JSONException e) {
                                GoogleGeocode gg = new GoogleGeocode(address);
                                latitude = gg.getLatitude();
                                longitude = gg.getLongitude();
                            }

                            String[] start = startTime.split("T");
                            String dateStart = start[0];
                            String rest = start[1];

                            String[] yearMonthDay = dateStart.split("-");
                            int year = Integer.parseInt(yearMonthDay[0]);
                            int month = Integer.parseInt(yearMonthDay[1]);
                            int day = Integer.parseInt(yearMonthDay[2]);

                            String dateTime[] = rest.split("\\+");
                            String hourStart = dateTime[0];
                            int hour = Integer.parseInt(hourStart.split(":")[0]);
                            int minute = Integer.parseInt(hourStart.split(":")[1]);

                            Calendar date = new GregorianCalendar(year, month-1, day, hour, minute);

                            long currentTime = System.currentTimeMillis();

                            if (date.getTimeInMillis() > currentTime) {

                                Event event = new Event(name, organizer, address, city, country, latitude, longitude, null, null, null, date, null);
                                eventsList.add(event);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAndWait();
    }

    public void publishStory(Activity activity, String name, String place, String url, String time) {
        Session session = Session.getActiveSession();

        if (session != null) {

            List<String> permissions = session.getPermissions();
            if (!isSubsetOf(PERMISSIONS, permissions)) {
                pendingPublishReauthorization = true;
                Session.NewPermissionsRequest newPermissionsRequest = new Session
                        .NewPermissionsRequest(activity, PERMISSIONS);
                session.requestNewPublishPermissions(newPermissionsRequest);
                return;
            }

            Bundle postParams = new Bundle();
            postParams.putString("name", "uMorning");
            postParams.putString("caption", "Don't arrive late to your appointments, be smart!");
            postParams.putString("description", "Activated an alarm for " + name + " at " + place + " on " + time + " using uMorning.");
            postParams.putString("link", url);
            postParams.putString("picture", "https://cdn1.iconfinder.com/data/icons/devine_icons/512/PNG/System%20and%20Internet/Times%20and%20Dates.png");

            Request.Callback callback = new Request.Callback() {
                public void onCompleted(Response response) {

                    FacebookRequestError error = response.getError();
                    if (error != null) {
                        Toast.makeText(cxt.getApplicationContext(), "Error during posting", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(cxt.getApplicationContext(), "Post completed", Toast.LENGTH_LONG).show();
                    }
                }
            };

            Request request = new Request(session, "me/feed", postParams,
                    HttpMethod.POST, callback);

            RequestAsyncTask task = new RequestAsyncTask(request);
            task.execute();
        }

    }

    private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
        for (String string : subset) {
            if (!superset.contains(string)) {
                return false;
            }
        }
        return true;
    }

}
