package com.example.ervan.ta2pelacak.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.FloatRange;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.ervan.ta2pelacak.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String Device = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Device = this.getIntent().getExtras().getString("device");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        final String Mypref="mypref";
        SharedPreferences sharedPref = this.getSharedPreferences(Mypref, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        Float lat = Float.parseFloat(sharedPref.getString("Device"+Device+"_lat",""));
        Float longi = Float.parseFloat(sharedPref.getString("Device"+Device+"_long",""));
        String nama = (String) sharedPref.getString("Device" + Device +"-NAMA", "");

        LatLng dev_loc= new LatLng(lat,longi);
        mMap.addMarker(new MarkerOptions().position(dev_loc).title(nama + " berada disini"));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(dev_loc)
                .zoom(15)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
