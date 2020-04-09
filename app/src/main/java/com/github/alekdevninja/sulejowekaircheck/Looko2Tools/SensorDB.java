package com.github.alekdevninja.sulejowekaircheck.Looko2Tools;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;

public class SensorDB {
    private ArrayList<Sensor> sensorDB;

    public SensorDB() {
        sensorDB = new ArrayList<>();
    }

    public Sensor getSensor(int sensorId) {
        return sensorDB.get(sensorId);
    }

    public int getSensorDBSize() {
        return sensorDB.size();
    }

    public void addSensor(Sensor sensor) {
        sensorDB.add(sensor);
    }

    public ArrayList<Sensor> getSensorDB() {
        return sensorDB;
    }

    //    public void printInLogAllSensors() {
//
//        Log.i("SensorDB", "---------------------------------------");
//        for (int i = 0; i < getSensorDBSize(); i++) {
//            Log.i("SensorDB", "sensor preview:");
//
//            Log.i("SensorDB", "sensor id#" + sensorDB.get(i).getSensorId());
//            Log.i("SensorDB", "PM2.5 = " + sensorList.get(i).getPm25Value());
//            Log.i("SensorDB", sensorList.get(i).getPm25Percentage() + "% of norm");
//            Log.i("SensorDB", "was updated yet: " + sensorList.get(i).isWasUpdated());
//
//        }
//    }
}
