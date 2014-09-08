package com.example.umorning.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.umorning.R;
import com.example.umorning.model.Badge;

public class BadgeAcquisitionActivity extends Activity {

    private ImageView badgeIcon;
    private TextView badgeName;
    private TextView badgeDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badge_acquisition);

        badgeIcon = (ImageView) findViewById(R.id.imageView);
        badgeName = (TextView) findViewById(R.id.nameText);
        badgeDescription = (TextView) findViewById(R.id.descriptionText);

        Intent i = getIntent();
        Badge badge = (Badge)i.getSerializableExtra("badgeAquired");

        badgeName.setText(badge.getName());
        badgeDescription.setText(badge.getDescription());
        badgeIcon.setImageResource(badge.getIconAcquired());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.badge_acquisition, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void closeActivity(View v){
        finish();
    }
}
