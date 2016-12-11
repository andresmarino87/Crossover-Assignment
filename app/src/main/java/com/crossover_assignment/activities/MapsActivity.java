package com.crossover_assignment.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.crossover_assignment.R;
import com.crossover_assignment.models.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap map;
    private ArrayList<Place> places;
    private SupportMapFragment mapFragment;
    private String authToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        authToken = getIntent().getStringExtra("authToken");
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
        map = googleMap;
        places = getIntent().getParcelableArrayListExtra("places");
        LatLng saitama_prefecture = new LatLng(35.8569991,139.6488487);
        for (Place place :places){
            LatLng location = new LatLng(place.getLocation().getLatitude(), place.getLocation().getLongitude());
            map.addMarker(new MarkerOptions().position(location).title(place.getName()));
        }
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){
            @Override
            public boolean onMarkerClick(Marker marker) {
                Snackbar snackbar = Snackbar.make(mapFragment.getView(), getString(R.string.str_rent_bike), Snackbar.LENGTH_LONG);
                snackbar.setAction(getString(R.string.action_rent),new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getBaseContext(), CreditCardInfoActivity.class);
                        intent.putExtra("authToken",authToken);
                        startActivity(intent);
                        finish();
                    }
                });
                snackbar.show();
                return false;
            }
        });
        map.moveCamera(CameraUpdateFactory.newLatLng(saitama_prefecture));
        map.animateCamera( CameraUpdateFactory.zoomTo( 11.0f ) );
    }
}
