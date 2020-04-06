package com.github.alekdevninja.sulejowekaircheck.Looko2Tools;

import java.util.ArrayList;

public class SensorDB {
    private ArrayList<Sensor> sensorDB;

    public SensorDB() {
        sensorDB = new ArrayList<>();
    }

    public Sensor getSensor(int sensorId) {
        return sensorDB.get(sensorId);
    }

    public void addSensor(Sensor sensor) {
        sensorDB.add(sensor);
    }
}
