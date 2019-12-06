package com.drunk.mode.Activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.StaticLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

//import com.drunk.mode.Services.GeofenceTransitionService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.drunk.mode.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class LiveMapActivity extends AppCompatActivity implements OnMapReadyCallback {


    @BindView(R.id.toolbarLiveMap) Toolbar toolbar;

    GoogleMap mMap;
//    GoogleApiClient client;
    LatLng friendLatLng;
    String latitude,longitude,name,userid,prevdate,prevImage,myImage,myName,myLat,myLng,myDate;
    Marker marker;
    DatabaseReference reference;
    ArrayList<String> mKeys;
    MarkerOptions myOptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_map);
        ButterKnife.bind(this);


        mKeys = new ArrayList<>();
        Intent intent = getIntent();

        if(intent!=null)
        {
            latitude=intent.getStringExtra("latitude");
            longitude = intent.getStringExtra("longitude");
            name = intent.getStringExtra("name");
            userid = intent.getStringExtra("userid");
            prevdate = intent.getStringExtra("date");
            prevImage = intent.getStringExtra("image");

            toolbar.setTitle(name +"'s" + "Location");
            setSupportActionBar(toolbar);
            if(getSupportActionBar()!=null)
            {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }

        }

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);


        SupportMapFragment mapFragment =  (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        myName = dataSnapshot.child("name").getValue(String.class);
                        myLat = dataSnapshot.child("lat").getValue(String.class);
                        myLng = dataSnapshot.child("lng").getValue(String.class);
                        myDate = dataSnapshot.child("date").getValue(String.class);
                        myImage = dataSnapshot.child("profile_image").getValue(String.class);


                        friendLatLng = new LatLng(Double.parseDouble(myLat),Double.parseDouble(myLng));

                        myOptions.position(friendLatLng);
                        myOptions.snippet("Last seen: "+myDate);
                        myOptions.title(myName);

                        if(marker == null)
                        {
                            marker = mMap.addMarker(myOptions);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(friendLatLng,15));
                        }
                        else
                        {
                            marker.setPosition(friendLatLng);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        mMap.setOnMarkerClickListener(this);
//        mMap.setOnMapClickListener(this);
//
//        client = new GoogleApiClient.Builder(this)
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();

//        client.connect();

//        LatLng dangerous_area = new LatLng(30.448107, -84.306236);
//        mMap.addCircle(new CircleOptions()
//        .center(dangerous_area)
//        .radius(500)
//        .strokeColor(Color.BLUE)
//        .fillColor(0x220000FF)
//        .strokeWidth(5.0f));
//
//        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation)


        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View row = getLayoutInflater().inflate(R.layout.custom_snippet,null);
                TextView nameTxt = row.findViewById(R.id.snippetName);
                TextView dateTxt = row.findViewById(R.id.snippetDate);
                CircleImageView imageTxt = row.findViewById(R.id.snippetImage);
                if(myName == null && myDate == null)
                {
                    nameTxt.setText(name);
                    dateTxt.setText(dateTxt.getText().toString() + prevdate);
                    Picasso.get().load(prevImage).placeholder(R.drawable.defaultprofile).into(imageTxt);
                }
                else
                {
                    nameTxt.setText(myName);
                    dateTxt.setText(dateTxt.getText().toString() + myDate);
                    Picasso.get().load(myImage).placeholder(R.drawable.defaultprofile).into(imageTxt);
                }

                return row;
            }
        });

        friendLatLng = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
        MarkerOptions optionsnew = new MarkerOptions();

        optionsnew.position(friendLatLng);
        optionsnew.title(name);
        optionsnew.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        if(marker == null)
        {
            marker = mMap.addMarker(optionsnew);
        }
        else
        {
            marker.setPosition(friendLatLng);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(friendLatLng,15));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
//            if(item.getItemId() == R.id.start_geofence){
//                startGeofence();
//            }
        }

        return super.onOptionsItemSelected(item);
    }

//    GeofencingRequest geofencingRequest;
//    private void startGeofence() {
//        if(geofenceMarker != null){
//            Geofence geofence = createGeofence(geofenceMarker.getPosition(), 400f);
//            geofencingRequest = createGeoRequest(geofence);
//            addGeoFence(geofence);
//        }
//    }

//    private void addGeoFence(Geofence geofence) {
//        LocationServices.GeofencingApi.addGeofences(client, geofencingRequest, createGeofencingPendingIntent())
//                .setResultCallback(this);
//    }

//    PendingIntent geoFencePendingIntent;
//    private PendingIntent createGeofencingPendingIntent() {
//        if(geoFencePendingIntent != null){
//            return geoFencePendingIntent;
//        }
//
//        Intent i = new Intent(this, GeofenceTransitionService.class);
//
//        return PendingIntent.getService(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
//    }

//    private GeofencingRequest createGeoRequest(Geofence geofence) {
//        return new GeofencingRequest.Builder()
//                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
//                .addGeofence(geofence)
//                .build();
//    }

//    private Geofence createGeofence(LatLng position, float v) {
//        return new Geofence.Builder()
//                .setRequestId("My Geofence")
//                .setCircularRegion(position.latitude, position.longitude, v)
//                .setExpirationDuration(60 * 60 * 1000)
//                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
//                .build();
//    }


//    @Override
//    public void onLocationChanged(Location location) {
//
//    }
//
//    @Override
//    public void onStatusChanged(String s, int i, Bundle bundle) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String s) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String s) {
//
//    }
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    @Override
//    public void onMapClick(LatLng latLng) {
//        markerForGeofence(latLng);
//    }

//    Marker geofenceMarker;
//    private void markerForGeofence(LatLng latLng) {
//        MarkerOptions optionsMarker = new MarkerOptions()
//                .position(latLng)
//                .title("Geofence Marker");
//
//        if(mMap != null){
//            if(geofenceMarker != null){
//                geofenceMarker.remove();
//            }
//
//            geofenceMarker = mMap.addMarker(optionsMarker);
//
//        }
//    }
//
//    @Override
//    public boolean onMarkerClick(Marker marker) {
//        return false;
//    }
//
//    @Override
//    public void onResult(@NonNull Status status) {
//        drawGeofence();
//    }
//
//    Circle geoFenceLimits;
//    private void drawGeofence() {
//        if(geoFenceLimits != null){
//            geoFenceLimits.remove();
//        }
//
//        CircleOptions circleOptions = new CircleOptions()
//                .center(geofenceMarker.getPosition())
//                .strokeColor(Color.argb(50, 70, 70, 70))
//                .fillColor(Color.argb(100, 150, 150, 150))
//                .radius(400f);
//
//        geoFenceLimits = mMap.addCircle(circleOptions);
//    }
}
