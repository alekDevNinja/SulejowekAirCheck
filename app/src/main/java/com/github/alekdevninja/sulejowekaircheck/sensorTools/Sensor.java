package com.github.alekdevninja.sulejowekaircheck.sensorTools;

import android.os.AsyncTask;
import android.util.Log;

import com.github.alekdevninja.sulejowekaircheck.map.MapColorTools;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class Sensor extends SupportMapFragment implements OnMapReadyCallback {

    private int sensorId;
    private String sensorHttpLink;
    private LatLng sensorCoordinates;
    private String pm25Value; // scraped & formatted PM2.5 value //@todo reformat this to integer
    private String pm25Percentage; // scraped & formatted % of healthy norm value //@todo reformat this to integer
    private SensorMarker sensorMarker;
    private Scraper scraper;

    public Sensor(String sensorHttpLink, double locationLatitude, double locationLongitude) {
        setSensorId();
        this.sensorHttpLink = sensorHttpLink;
        sensorCoordinates = new LatLng(locationLatitude, locationLongitude);

        scraper = new Scraper(getSensorHttpLink());
        scraper.updateSensorData();

        //extract pm2.5 and % values
        obtainValues();

        Log.i("Sensor", "Sensor object: " + getSensorId() + " created");
    }

    private void obtainValues() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                pm25Value = scraper.getPm25Value();
                pm25Percentage = scraper.getPm25Percentage();
                Log.i("Sensor", " Sensor #" + getSensorId() + " was updated " + pm25Value);

                //determine color for the map circles
                determineColors();

            }
        });
    }

    private void determineColors() {
        MapColorTools mapColorTools;
        try {
            //when sensor is online
            mapColorTools = new MapColorTools(Integer.parseInt(getPm25Value()));
            createSensorMarker(mapColorTools.determineColorsBasedOnPmValues());
        } catch (Exception e) {
            //sensor is offline
            createSensorMarker(0x33373737); //gray
        }
    }

    public void createSensorMarker(int fillColor) {
        sensorMarker = new SensorMarker(
                getSensorId(), //sensor #id
                sensorCoordinates,
                fillColor //grey default color
        );
        Log.i("Sensor", "SensorMarker created");
    }

    public void removeSensorMarker() {
        sensorMarker = null;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
//        googleMap.addMarker(sensorMarker.getMarker());
//        googleMap.addCircle(sensorMarker.getCircle());

//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                googleMap.addMarker(sensorMarker.getMarker());
//                googleMap.addCircle(sensorMarker.getCircle());
//            }
//        });

//        addMapMarkersToGoogleMap();

    }

    public String getPm25Value() {
        return pm25Value;
    }

    public String getPm25Percentage() {
        return pm25Percentage;
    }

    public int getSensorId() {
        return sensorId;
    }

    private void setSensorId() {
//        sensorId = sensorId + SensorInterface.sensorsCreated;
//        SensorInterface.sensorsCreated++;
        sensorId = sensorId + AbstractSensor.sensorsCreated;
        AbstractSensor.sensorsCreated++;
    }

    public SensorMarker getSensorMarker() {
        return sensorMarker;
    }

    public void setColor(int fillColor) {
        sensorMarker.setFillColor(fillColor);
//        this.fillColor = fillColor;
    }

    public String getSensorHttpLink() {
        return sensorHttpLink;
    }
}
