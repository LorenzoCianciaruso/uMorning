package com.example.umorning.fragments;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.umorning.R;
import com.example.umorning.model.Event;

import java.text.SimpleDateFormat;
import java.util.List;

public class EventsAdapter extends BaseAdapter {

    private Activity activity;
    private List<Event> events;
    private static LayoutInflater inflater = null;

    EventsAdapter(Activity activity, List<Event> events) {
        this.activity = activity;
        this.events = events;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (convertView == null) {
            row = inflater.inflate(R.layout.list_item_events, null);
        }

        TextView name = (TextView) row.findViewById(R.id.eventName);
        TextView date = (TextView) row.findViewById(R.id.eventDate);
        ImageView icon = (ImageView) row.findViewById(R.id.eventIcon);

        Event e;
        e = events.get(position);

        //Titolo evento
        name.setText(e.getName());
        //Ora e data dell'evento
        SimpleDateFormat df = new SimpleDateFormat("c d LLLL yyyy HH:mm");
        String formattedDate = df.format(e.getDate().getTime());
        date.setText(formattedDate);
        //Evento di google/facebook/eventbrite
        Integer[] mThumbIds = {R.drawable.calendar_icon, R.drawable.facebook_icon, R.drawable.eventb_icon};
        icon.setBackgroundResource(mThumbIds[e.getType()]);

        return row;
    }
}
