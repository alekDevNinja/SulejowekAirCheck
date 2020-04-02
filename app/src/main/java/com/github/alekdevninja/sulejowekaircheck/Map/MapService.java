package com.github.alekdevninja.sulejowekaircheck.Map;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.github.alekdevninja.sulejowekaircheck.Looko2Tools.Look2Scraper;
import com.github.alekdevninja.sulejowekaircheck.MainActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;

import java.util.ArrayList;

public class MapService extends SupportMapFragment implements OnMapReadyCallback {
    //    private SupportMapFragment mapFragment;
    private ArrayList<Look2Scraper> sensorList;
    private ArrayList<MapMarker> mapMarkers;
    private Context context;
    private GoogleMap googleMap;
    CircleOptions testCircle;

    public MapService(ArrayList<Look2Scraper> sensorList) {
        this.context = MainActivity.getMainActivityContext(); //getting the MainActivity context
        this.sensorList = sensorList;
        mapMarkers = new ArrayList<MapMarker>();

        generateAllMarkers();

    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        //setting map type
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

//        populateMapWithMarkers(googleMap);
    }

    public void generateAllMarkers() {
        Log.i("MapService", "generateAllMarkers() - starting to create all markers");
        for (int i = 0; i < sensorList.size(); i++) {

            Log.i("MapService", "generateAllMarkers() - marker #" + i + " created");
            mapMarkers.add(
                    new MapMarker(
                            sensorList.get(i).getSensorCoordinates(),
                            context, //main activity context passed for icon number finding
                            i, //sensor #id
                            sensorList.get(i).getSensorName() //sensor name for the onClick display
                    )
            );
            Log.i("MapService", "marker #" + i + " sensorList.get(i).getSensorCoordinates(): " + sensorList.get(i).getSensorCoordinates());
            Log.i("MapService", "marker #" + i + " sensorList.get(i).getSensorName(): " + sensorList.get(i).getSensorName());


            Log.i("MapService", "generateAllMarkers() - marker #" + i + " added to the mapMarkers list");
        }
        Log.i("MapService", "generateAllMarkers() - all markers created");
    }

    public void populateMapWithMarkers(GoogleMap map) {
        //add markers to the map for every sensor in the sensor list
        for (int i = 0; i < mapMarkers.size(); i++) {

            map.addCircle(mapMarkers.get(i).getCircle());
            map.addMarker(mapMarkers.get(i).getMarker());

//            map.addCircle(marker.getCircle());
//            map.addMarker(marker.getMarker());
        }
//        for (int i = 0; i < sensorList.size(); i++) {
//
//            MapMarker marker = new MapMarker(
//                    sensorList.get(i).getSensorCoordinates(),
//                    context, //main activity context passed for icon number finding
//                    i, //sensor #id
//                    sensorList.get(i).getSensorName() //sensor name for the onClick display
//            );
//
//            map.addCircle(marker.getCircle());
//            map.addMarker(marker.getMarker());
//        }
    }

    public void addTestCircle() {
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

        googleMap.addCircle(testCircle);
    }

//    @Override
//    public void onPointerCaptureChanged(boolean hasCapture) {
//    }
}
