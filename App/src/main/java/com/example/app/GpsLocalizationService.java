package com.example.app;

import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_LONG;

/**
 * Created by Lorenzo on 04/01/14.
 */
public class GpsLocalizationService extends Service {




    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){

        LocationManager mgr;

        mgr=(LocationManager)getSystemService(LOCATION_SERVICE);
        LocationListener locationListener = new myLocationListener();
        mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
}



    @Override
    public void onDestroy() {
        super.onDestroy();

        //client.getConnectionManager().shutdown();
    }


    public class myLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            if(location != null){
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();


                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, latitude+" "+longitude, Toast.LENGTH_LONG);
                toast.show();

            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }

}
