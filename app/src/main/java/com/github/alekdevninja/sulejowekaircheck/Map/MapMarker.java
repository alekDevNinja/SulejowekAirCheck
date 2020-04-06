package com.github.alekdevninja.sulejowekaircheck.Map;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapMarker {
    private static int id;
    private int radiusDiameter;
    private float strokeWidth;
    private int strokeColor;
    private int fillColor;
    private float anchor;
    private LatLng sensorCoordinates;
    private CircleOptions circle;
    private MarkerOptions marker;


    //ToDo put fill color in the constructor ?
    public MapMarker(LatLng sensorCoordinates, Context context, int sensorId, String sensorName, int fillColor) {
        //marker setup parameters
        this.sensorCoordinates = sensorCoordinates;
        setupParameters();

        //generating correct icon for map display
        int icon_id = context.getResources().getIdentifier("com.github.alekdevninja.sulejowekaircheck:drawable/ic_" + getId(),
                "Drawable",
                "com.github.alekdevninja.sulejowekaircheck"); // finding #id for the correct R.drawable. icon

        circle = new CircleOptions()
                .center(sensorCoordinates)
                .radius(radiusDiameter)
                .strokeWidth(strokeWidth)
                .strokeColor(strokeColor)
                .fillColor(fillColor);

        marker = new MarkerOptions()
                .position(sensorCoordinates)
                .title(sensorName) // @ToDo needs a fix
                //.snippet(sensorList.get(i).getPm25Value() + (" ug/m3 PM2.5")) // @ToDo fix display after data refresh
                .icon(BitmapDescriptorFactory
                        .fromResource(icon_id))
                .anchor(anchor, anchor);
    }

    private void setupParameters() {
        setId(); // track count of created objects so far - id++

        radiusDiameter = 400; // in meters
        strokeWidth = 5; // only important  for visuals
        strokeColor = Color.parseColor("#6d6d6d"); //dark gray | red -> ff0000
        fillColor = 0x33373737; //  fillColor(Color.argb(20, 50, 0, 255)) //this works for tweaking //gray
                                //  fillColor = Color.argb(128, 255, 0, 0);
        anchor = 0.5f; // amount of offset for the icon to be in the middle of the marker
    }


    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    public CircleOptions getCircle() {
        return circle;
    }

    public MarkerOptions getMarker() {
        return marker;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public int getFillColor() {
        return fillColor;
    }


    public static int getId() {
        return id;
    }

    public static void setId() {
        MapMarker.id++;
    }
    public static void resetIdCounter() {
        MapMarker.id = 0;
    }
}
