package com.github.alekdevninja.sulejowekaircheck;

import android.content.Context;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;

import com.github.alekdevninja.sulejowekaircheck.Looko2Tools.Look2Scraper;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;

import java.util.ArrayList;

public class MapService extends AppCompatActivity implements OnMapReadyCallback {
    private SupportMapFragment mapFragment;
    private ArrayList<Look2Scraper> sensorList;
    private MapMarker sensorsMapRepresentation;

    private Context context;
    private MapView mapView;

    private GoogleMap googleMap;
    CircleOptions testCircle;

    public MapService(int mapRiD, ArrayList<Look2Scraper> sensorList) {
        this.context = MainActivity.getMainActivityContext(); //getting the MainActivity context
        this.sensorList = sensorList;

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(mapRiD);
        mapFragment.getMapAsync(this);


    }

//    private MapMarker generateMapMarkers() {
//        MapMarker output = new MapMarker(sensorList);
//
//        return output;
//    }

    @Override
    public void onMapReady(GoogleMap map) {
        //setting map type
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        MapMarker mapMarker = new MapMarker(sensorList, map);

        //only for testing
        //markers setup parameters
        int radiusDiameter = 400; // in meters
        float strokeWidth = 5; // only important  for visuals
        int strokeColor = Color.parseColor("#6d6d6d"); //dark gray | red -> ff0000
        int fillColor = 0x336d6d6d; // .fillColor(Color.argb(20, 50, 0, 255)) //this works for tweaking //gray
        float anchor = 0.5f; // amount of offset for the icon to be in the middle of the marker

        testCircle = new CircleOptions()
                .center(sensorList.get(5).getSensorCoordinates())
                .radius(radiusDiameter)
                .strokeWidth(strokeWidth)
                .strokeColor(strokeColor)
                .fillColor(fillColor);

        map.addCircle(testCircle);

        //create markers for all sensors
//        createMapMarkers(sensorList, map);
        //experiment
//        MapMarker mapMarker = new MapMarker(sensorList, map);
//        new MapMarker(sensorList, map, mainActivityContext);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}
