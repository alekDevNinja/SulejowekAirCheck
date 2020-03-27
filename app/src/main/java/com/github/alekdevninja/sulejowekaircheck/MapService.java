package com.github.alekdevninja.sulejowekaircheck;

import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;

import com.github.alekdevninja.sulejowekaircheck.Looko2Tools.Look2Scraper;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapService extends AppCompatActivity implements OnMapReadyCallback {
    SupportMapFragment mapFragment;
    ArrayList<Look2Scraper> sensorList;

    public MapService(int mapRiD, ArrayList<Look2Scraper> sensorList) {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(mapRiD);
        mapFragment.getMapAsync(this);
        this.sensorList = sensorList;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        //setting map type
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        //Adding sensor range
        int radiusDiameter = 400;
        float strokeWidth = 5;
//        int strokeColor = Color.parseColor("#ff0000"); //red
        int strokeColor = Color.parseColor("#6d6d6d"); //dark gray
//        int fillColor = 0x33FF0000; // .fillColor(Color.argb(20, 50, 0, 255)) //RED
        int fillColor = 0x33373737; // .fillColor(Color.argb(20, 50, 0, 255)) //this works for tweaking //gray
        float anchorV0 = 0.5f;
        float anchorV1 = 0.5f;


        for (int i = 0; i < sensorList.size(); i++) {
            //Reymonta
            Circle circle = map.addCircle(new CircleOptions()
                    .center(sensorList.get(i).getSensorCoordinates())
                    .radius(radiusDiameter)
                    .strokeWidth(strokeWidth)
                    .strokeColor(strokeColor)
                    .fillColor(fillColor)
            );

            int id = getResources().getIdentifier("com.github.alekdevninja.sulejowekaircheck:drawable/ic_" + (i + 1),
                    "Drawable",
                    "com.github.alekdevninja.sulejowekaircheck");

            map.addMarker(new MarkerOptions()
                            .position(sensorList.get(i).getSensorCoordinates())
                            .title(sensorList.get(i).getSensorName())
//                            .snippet(sensorList.get(i).getPm25Value() + (" ug/m3 PM2.5")) // @Todo fix display after data refresh
                            .icon(BitmapDescriptorFactory
                                    .fromResource(id))
                            .anchor(anchorV0, anchorV1)
            );
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
