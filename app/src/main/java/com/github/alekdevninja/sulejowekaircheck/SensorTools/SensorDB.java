package com.github.alekdevninja.sulejowekaircheck.SensorTools;

import java.util.ArrayList;

public class SensorDB {
    private ArrayList<Sensor> sensorDB;

    public SensorDB() {
        sensorDB = new ArrayList<>();
        addSensor(new Sensor("", 0, 0)); // temp sensor only for initial display
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

    public void removeAllSensorsInDB() {
        sensorDB.removeAll(sensorDB);
        AbstractSensor.sensorsCreated = 1;
    }
}
