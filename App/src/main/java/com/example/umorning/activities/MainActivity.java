package com.example.umorning.activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;

import com.example.umorning.R;
import com.example.umorning.internal_services.UpdateAlarmService;
import com.example.umorning.model.Badge;
import com.example.umorning.model.DatabaseHelper;
import com.example.umorning.tabswipeadapter.TabsPagerAdapter;

public class MainActivity extends FragmentActivity implements
        ActionBar.TabListener {

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    private String[] tabs = {"Home", "Alarms", "Events"};

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);

        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(2);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // aggiunge i tab
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                //evidenzia la pagina selezionata
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        DatabaseHelper db = new DatabaseHelper(this);
        if(db.aquireBadge(Badge.FIRST_USAGE)){
            Badge badge = db.getBadge(Badge.FIRST_USAGE);
            Intent myIntent = new Intent(this, BadgeAcquisitionActivity.class);
            myIntent.putExtra("badgeAquired", badge);
            startActivity(myIntent);
        }

        //chiama un nuovo update al tempo impostato dall'utente
        Intent updateIntent = new Intent(getApplicationContext(), UpdateAlarmService.class);
        SharedPreferences prefs = getSharedPreferences("uMorning", 0);
        long refreshRate = prefs.getLong("REFRESH", 60);
        PendingIntent intent = PendingIntent.getService(this, 0, updateIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, (System.currentTimeMillis() + (refreshRate * 60 * 1000)), intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // mostra i fragment
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    public void openAddNewActivity(View v) {
        Intent myIntent = new Intent(MainActivity.this, AlarmEditActivity.class);
        startActivity(myIntent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}