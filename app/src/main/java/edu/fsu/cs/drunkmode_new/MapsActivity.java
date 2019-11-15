package edu.fsu.cs.drunkmode_new;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//Most code from https://www.simplifiedcoding.net/android-google-maps-tutorial-google-maps-android-api/
//and https://developer.android.com/training/location

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener,
        View.OnClickListener{

    private GoogleMap mMap;
    private double longitude;
    private double latitude;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private Marker previousLocation;

    private FusedLocationProviderClient fusedLocationClient;
    private GoogleApiClient googleApiClient;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference ref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mAuth = FirebaseAuth.getInstance();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Initializing googleapi client
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    getCurrentLocation();
                    moveMap();
                }
            }
        };

        //Initializing views and adding onclick listeners
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();

        ref = database.getReference("users").child(mAuth.getCurrentUser().getUid());
        ref.addChildEventListener(childEventListener);
    }

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
            Toast.makeText(getApplicationContext(), "onChildAdded: " + dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();

            // A new comment has been added, add it to the displayed list
            Toast.makeText(getApplicationContext(), "Child: " + dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();

            // ...
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            Toast.makeText(getApplicationContext(), "onChildChanged: " + dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();

            // A new comment has been added, add it to the displayed list
            Toast.makeText(getApplicationContext(), "Child: " + dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();

            // ...
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            Toast.makeText(getApplicationContext(), "onChildRemoved: " + dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();

            // A new comment has been added, add it to the displayed list
            Toast.makeText(getApplicationContext(), "Child: " + dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();

            // ...
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            Toast.makeText(getApplicationContext(), "onChildMoved: " + dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();

            // A new comment has been added, add it to the displayed list
            Toast.makeText(getApplicationContext(), "Child: " + dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();

            // ...
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Toast.makeText(getApplicationContext(), "onCancelled: ", Toast.LENGTH_SHORT).show();
        }
    };

    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Initializing our map
        mMap = googleMap;
        //Creating a random coordinate
        LatLng latLng = new LatLng(-34, 151);
        //Adding marker to that coordinate
        mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //Setting onMarkerDragListener to track the marker drag
        mMap.setOnMarkerDragListener(this);
        //Adding a long click listener to the map
        mMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnected(Bundle bundle) {
        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        //Clearing all the markers
        mMap.clear();

        //Adding a new marker to the current pressed position we are also making the draggable true
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true));
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        //Getting the coordinates
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;

        //Moving the map
        moveMap();
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    private void getCurrentLocation() {
        //Creating a location object
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    //Getting longitude and latitude
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();


                    ref.child("longitude").setValue(longitude);
                    ref.child("latitude").setValue(latitude);






                    //moving the map to location
                    moveMap();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    private void moveMap() {
        //String to display current latitude and longitude
        String msg = latitude + ", "+longitude;

        //Creating a LatLng Object to store Coordinates
        LatLng latLng = new LatLng(latitude, longitude);

        if (previousLocation != null)
        {
            previousLocation.remove();
        }

        //Adding marker to map
        previousLocation = mMap.addMarker(new MarkerOptions()
                .position(latLng) //setting position
                .draggable(true) //Making the marker draggable
                .title("Current Location")); //Adding a title

        //Moving the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        //Animating the camera
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

}
