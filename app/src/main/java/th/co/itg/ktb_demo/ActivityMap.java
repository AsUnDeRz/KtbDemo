package th.co.itg.ktb_demo;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.thekhaeng.pushdownanim.PushDownAnim;

/**
 * Created by ToCHe on 24/8/2018 AD.
 */
public class ActivityMap extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {

    private GoogleMap mMap;
    private Location mLocation;
    private LocationRequest mLocationRequest;
    private LocationCallback locationCallback;
    private Button btnGetLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        btnGetLocation = findViewById(R.id.btnGetLocation);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        setupGoogleClient();

        PushDownAniamtion(btnGetLocation);

    }

    private void PushDownAniamtion(View view){
        PushDownAnim.setOnTouchPushDownAnim(view)
                .setOnClickListener(view1 -> {
                    switch (view.getId()) {
                        case R.id.btnGetLocation:
                            getCurrentLocation();
                            break;
                    }
                });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            mMap = googleMap;
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);
        }

    }

    @Override
    public boolean onMyLocationButtonClick() {
        getCurrentLocation();
       // LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest,locationCallback,null);
        return true;
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation(){
        Task<Location> task = LocationServices.getFusedLocationProviderClient(this).getLastLocation();
        task.addOnCompleteListener(it -> {
            if (it.isComplete()){
                mLocation = it.getResult();
                handleNewLocation(it.getResult());
            }
        });
    }

    private void setupGoogleClient(){
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000);

        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null){
                    for (Location location:locationResult.getLocations()){
                        mLocation = location;
                        handleNewLocation(location);
                    }
                }
            }
        };
    }


    private void handleNewLocation(Location location) {
        mLocation = location;
        Float zoomLevel = 14f;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.clear();
        MarkerOptions current = new MarkerOptions()
                .position(latLng);
        mMap.addMarker(current);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
    }

}
