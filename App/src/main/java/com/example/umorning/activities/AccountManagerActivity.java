package com.example.umorning.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.umorning.R;
import com.example.umorning.model.Badge;
import com.example.umorning.model.DatabaseHelper;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import java.util.Arrays;
import java.util.List;


public class AccountManagerActivity extends Activity {

    private static final String TAG = "AccountManagerActivity";
    private UiLifecycleHelper uiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_manager);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setTitle("Accounts");

        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        List<String> writePermission = Arrays.asList("publish_actions","user_events");
        LoginButton facebookLoginButton = (LoginButton) findViewById(R.id.authButton);
        facebookLoginButton.setPublishPermissions(writePermission);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void eventbriteAuth(View view) {
        Intent intent = new Intent(this, WebViewActivity.class);
        Bundle b = new Bundle();
        b.putString("url", "https://www.eventbrite.com/oauth/authorize?response_type=token&client_id=AWF7I3D2E3CAVX6QNW");
        intent.putExtras(b);
        startActivity(intent);
        finish();

        //controllo se è il primo login in eventbrite
        DatabaseHelper db = new DatabaseHelper(this);
        if(db.aquireBadge(Badge.LOGIN_EB)){
            Badge badge = db.getBadge(Badge.LOGIN_EB);
            Intent myIntent = new Intent(this, BadgeAcquisitionActivity.class);
            myIntent.putExtra("badgeAquired", badge);
            startActivity(myIntent);
        }
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");

            //controllo se è il primo login in facebook
            DatabaseHelper db = new DatabaseHelper(this);
            if(db.aquireBadge(Badge.LOGIN_FB)){
                Badge badge = db.getBadge(Badge.LOGIN_FB);
                Intent myIntent = new Intent(this, BadgeAcquisitionActivity.class);
                myIntent.putExtra("badgeAquired", badge);
                startActivity(myIntent);
            }

        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
        }
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed())) {
            onSessionStateChange(session, session.getState(), null);
        }
        uiHelper.onResume();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

}
