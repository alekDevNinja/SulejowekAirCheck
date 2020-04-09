package com.github.alekdevninja.sulejowekaircheck.Looko2Tools;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.github.alekdevninja.sulejowekaircheck.MainActivity;
import com.github.alekdevninja.sulejowekaircheck.Map.SensorMarker;
import com.github.alekdevninja.sulejowekaircheck.R;
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
    GoogleMap googleMap;
    Context mainActivityContext;

    public Sensor(String sensorHttpLink, double locationLatitude, double locationLongitude) {
        setSensorId();
        this.googleMap = googleMap;
        this.sensorHttpLink = sensorHttpLink;
        sensorCoordinates = new LatLng(locationLatitude, locationLongitude);
        mainActivityContext = MainActivity.getMainActivityContext();
        googleMap = MainActivity.getGoogleMapContext();

        scraper = new Scraper(sensorHttpLink);
        scraper.updateSensorData();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                pm25Value = scraper.getPm25Value();
                pm25Percentage = scraper.getPm25Percentage();
                Log.i("Sensor", " Sensor #" + getSensorId() + " was updated " + pm25Value);
            }
        });

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                sensorMarker = new SensorMarker(
                        getSensorId(), //sensor #id
                        sensorCoordinates,
                        0x33373737 //grey default color
                );
                Log.i("Sensor", "SensorMarker created");
            }
        });


        //todo fix this - proboly wrong context related
//        googleMap.addMarker(sensorMarker.getMarker());
//        googleMap.addCircle(sensorMarker.getCircle());

//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                googleMap.addMarker(sensorMarker.getMarker());
//                googleMap.addCircle(sensorMarker.getCircle());
//            }
//        });

//        googleMap.addMarker(sensorMarker.getMarker());
//        googleMap.addCircle(sensorMarker.getCircle());

        Log.i("Sensor", "Sensor object: " + getSensorId() + " created");
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

    public void addMapMarkersToGoogleMap() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                googleMap.addMarker(sensorMarker.getMarker());
                googleMap.addCircle(sensorMarker.getCircle());
            }
        });
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


}
