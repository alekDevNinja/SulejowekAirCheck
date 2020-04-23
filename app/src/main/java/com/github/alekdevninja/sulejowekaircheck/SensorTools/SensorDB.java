package com.github.alekdevninja.sulejowekaircheck.SensorTools;

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

    public void removeAllSensorsInDB() {
        sensorDB.removeAll(sensorDB);
        AbstractSensor.sensorsCreated = 1;
    }

    public void bootstrapSensorDB() {
        sensorDB.removeAll(sensorDB);
        sensorDB.add(new Sensor("http://looko2.com/tracker.php?lan=&search=5CCF7F1A546F", 52.2483, 21.2767)); //Sul1
        sensorDB.add(new Sensor("http://looko2.com/tracker.php?lan=&search=2C3AE833FFD3", 52.2492, 21.2807)); //Reymonta
        sensorDB.add(new Sensor("http://looko2.com/tracker.php?lan=&search=6001944BCDEB", 52.2539, 21.296)); //Pogodna7
        sensorDB.add(new Sensor("http://looko2.com/tracker.php?lan=&search=2C3AE834F051", 52.2450, 21.3032)); //UMS1
        sensorDB.add(new Sensor("http://looko2.com/tracker.php?lan=&search=807D3A1F6F4F", 52.2521, 21.2456)); //Wola Grzybowska
    }
}
