package com.github.alekdevninja.sulejowekaircheck;
//firebase integration and testing
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.alekdevninja.sulejowekaircheck.SensorTools.Sensor;
import com.github.alekdevninja.sulejowekaircheck.SensorTools.SensorDB;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    RecyclerViewAdapter adapter;
    SupportMapFragment mapFragment;
    public static GoogleMap googleMap;
    private static Context mainActivityContext;
    private View viewPlaceholder;
    SensorDB sensorDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setup - main layout view + toolbar + support action bar);
        super.onCreate(savedInstanceState);
        setupMainView();

        // startup the map
        initializeMapFragment();

        //startup the DB for sensors
        sensorDB = new SensorDB();
        sensorDB.bootstrapSensorDB();

        //setup the RecyclerView
        recyclerViewSetup(sensorDB.getSensorDB());

        // setup the refresh bottom // UPDATING UI
        buttonRefresh();

        //populate the DB with data
//        bootstrapSensorDB();

        //update data in the recycler view
        displayUpdatedData(viewPlaceholder);
    }

    private void buttonRefresh() {
        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //clear map from old markers
                googleMap.clear(); //clearing map for a new fresh markers&circles

                //clear DB from existing sensors
                sensorDB.removeAllSensorsInDB();

                //add fresh sensors to DB
                sensorDB.bootstrapSensorDB();
//                bootstrapSensorDB(); //todo - fix this when bootstrap moved to DB

                //add mapMarkers based on sensors & update recycle view
                displayUpdatedData(view);
            }

        });
    }

    private void addSensorMarkersToMap() {
        for (int i = 0; i < sensorDB.getSensorDBSize(); i++) {
            googleMap.addMarker(
                    sensorDB.getSensor(i)
                            .getSensorMarker()
                            .getMarker()
            );
            googleMap.addCircle(
                    sensorDB.getSensor(i)
                            .getSensorMarker()
                            .getCircle()
            );
        }
    }

    private void bootstrapSensorDB() {
        sensorDB.removeAllSensorsInDB();
        sensorDB.addSensor(new Sensor("http://looko2.com/tracker.php?lan=&search=5CCF7F1A546F", 52.2483, 21.2767)); //Sul1
        sensorDB.addSensor(new Sensor("http://looko2.com/tracker.php?lan=&search=2C3AE833FFD3", 52.2492, 21.2807)); //Reymonta
        sensorDB.addSensor(new Sensor("http://looko2.com/tracker.php?lan=&search=6001944BCDEB", 52.2539, 21.296)); //Pogodna7
        sensorDB.addSensor(new Sensor("http://looko2.com/tracker.php?lan=&search=2C3AE834F051", 52.2450, 21.3032)); //UMS1
        sensorDB.addSensor(new Sensor("http://looko2.com/tracker.php?lan=&search=807D3A1F6F4F", 52.2521, 21.2456)); //Wola Grzybowska
    }

    private void initializeMapFragment() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    public void displayUpdatedData(final View view) {
        viewPlaceholder = view;

        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                adapter.notifyDataSetChanged(); //@ToDo need to set a delay here
                addSensorMarkersToMap();
                Log.i("MainActivity", "sensor data updated");
            }
        };
        handler.postDelayed(r, 1500);

    }

    private void setupMainView() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mainActivityContext = getApplicationContext(); //setting main activity context for other fragments
    }

    private void recyclerViewSetup(ArrayList<Sensor> sensors) {
        RecyclerView recyclerView = findViewById(R.id.rvSensors);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, sensors);
//        adapter.setClickListener((RecyclerViewAdapter.ItemClickListener) this);
//        ToDo - create the onClick forwarding user to the web page of curent sensor
        recyclerView.setAdapter(adapter);
    }

    public static Context getMainActivityContext() {
        return mainActivityContext;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        //setting map type
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //ToDo TEST - do everything as async tasks to reduce animation lag
        switch (id) {
            case (R.id.author): {

                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}