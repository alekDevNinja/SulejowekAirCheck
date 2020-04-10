package com.github.alekdevninja.sulejowekaircheck.SensorTools;

import android.content.Context;
import android.graphics.Color;

import com.github.alekdevninja.sulejowekaircheck.MainActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SensorMarker {
    private int sensorId;
    private int radiusDiameter;
    private float strokeWidth;
    private int strokeColor;
    private int fillColor;
    private float anchor;
    private LatLng sensorCoordinates;
    private CircleOptions circle;
    private MarkerOptions marker;
    private Context mainActivityContext; //main activity mapContext passed for icon number finding
private GoogleMap googleMapContext; //main activity mapContext passed for icon number finding

    //    public SensorMarker(LatLng sensorCoordinates, Context context, int sensorId, String sensorName, int fillColor) {
    public SensorMarker(int sensorId, LatLng sensorCoordinates, int fillColor) {
        //marker setup parameters
        this.sensorCoordinates = sensorCoordinates;
        this.mainActivityContext = MainActivity.getMainActivityContext();
        this.sensorId = sensorId;
        setupParameters();

        //generating correct icon for map display
        int icon_id = mainActivityContext.getResources().getIdentifier("com.github.alekdevninja.sulejowekaircheck:drawable/ic_" + sensorId,
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
//                .title(sensorName) // @ToDo needs a fix
                //.snippet(sensorList.get(i).getPm25Value() + (" ug/m3 PM2.5")) // @ToDo fix display after data refresh
                .icon(BitmapDescriptorFactory
                        .fromResource(icon_id))
                .anchor(anchor, anchor);
    }

    private void setupParameters() {
//        setId(); // track count of created objects so far - id++

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


//    public static int getId() {
//        return id;
//    }

//    public static void setId() {
//        SensorMarker.id++;
//    }

//    public static void resetIdCounter() {
//        SensorMarker.id = 0;
//    }
}
