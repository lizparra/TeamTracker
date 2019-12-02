package edu.fsu.cs.drunkmode_new;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//most code from https://stackoverflow.com/questions/8828639/get-gps-location-via-a-service-in-android

public class LocationService extends Service {


    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 900000;
    private static final float LOCATION_DISTANCE = 0;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            String Uid = mAuth.getCurrentUser().getUid();
            if (Uid != null)
            {
                DatabaseReference ref = database.getReference("users/"+Uid);
                ref.child("latitude").setValue(location.getLatitude());
                ref.child("longitude").setValue(location.getLongitude());
            }

            mLastLocation.set(location);
        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);
        mAuth = FirebaseAuth.getInstance();
        return START_STICKY;
    }

    @Override
    public void onCreate() {

        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {

        } catch (IllegalArgumentException ex) {

        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {

        } catch (IllegalArgumentException ex) {

        }
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        Log.println( Log.ERROR, "ON DESTROY", "ON DESTROY");
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {

                }
            }
        }
    }

    private void initializeLocationManager() {

        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}