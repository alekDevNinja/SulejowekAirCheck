package com.github.alekdevninja.sulejowekaircheck;

import android.content.Context;
import android.graphics.Color;

import com.github.alekdevninja.sulejowekaircheck.Looko2Tools.Look2Scraper;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;

import java.util.ArrayList;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapMarker extends AppCompatActivity {
    private int radiusDiameter;
    private float strokeWidth;
    private int strokeColor;
    private int fillColor;
    private float anchor;
    private Context context;

    public MapMarker(ArrayList<Look2Scraper> sensorList, GoogleMap map) {
        this.context = MainActivity.getMainActivityContext(); //getting the MainActivity context

        //markers setup parameters
        setupParameters();

        //for testing context connection
        Toast.makeText(context, "MainActivity context connection - works!" , Toast.LENGTH_LONG).show();

        for (int i = 0; i < sensorList.size(); i++) {

            map.addCircle(new CircleOptions()
                    .center(sensorList.get(i).getSensorCoordinates())
                    .radius(radiusDiameter)
                    .strokeWidth(strokeWidth)
                    .strokeColor(strokeColor)
                    .fillColor(fillColor)
            );

            int icon_id = context.getResources().getIdentifier("com.github.alekdevninja.sulejowekaircheck:drawable/ic_" + (i + 1),
                    "Drawable",
                    "com.github.alekdevninja.sulejowekaircheck"); // finding #id for the correct R.drawable. icon


            map.addMarker(new MarkerOptions()
                            .position(sensorList.get(i).getSensorCoordinates())
                            .title(sensorList.get(i).getSensorName())
//                            .snippet(sensorList.get(i).getPm25Value() + (" ug/m3 PM2.5")) // @Todo fix display after data refresh
                            .icon(BitmapDescriptorFactory
                                    .fromResource(icon_id))
                            .anchor(anchor, anchor)
            );
        }

    }

    private void setupParameters() {
        radiusDiameter = 400; // in meters
        strokeWidth = 5; // only important  for visuals
        strokeColor = Color.parseColor("#6d6d6d"); //dark gray | red -> ff0000
        fillColor = 0x33373737; // .fillColor(Color.argb(20, 50, 0, 255)) //this works for tweaking //gray
        anchor = 0.5f; // amount of offset for the icon to be in the middle of the marker
    }
}
