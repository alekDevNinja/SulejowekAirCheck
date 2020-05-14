package com.github.alekdevninja.sulejowekaircheck;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import com.github.alekdevninja.sulejowekaircheck.sensorTools.Sensor;
import com.github.alekdevninja.sulejowekaircheck.sensorTools.SensorDB;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    RecyclerViewAdapter adapter;
    SupportMapFragment mapFragment;
    public static GoogleMap googleMap;
    private static Context mainActivityContext;
    private View viewPlaceholder;
    public static SensorDB sensorDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setup - main layout view + toolbar + support action bar);
        super.onCreate(savedInstanceState);
        setupMainView();

        //startup the map
        initializeMapFragment();

        //startup the DB for sensors
        sensorDB = new SensorDB();
        sensorDB.bootstrapSensorDB();

        //setup the RecyclerView
        recyclerViewSetup(sensorDB.getSensorDB());

        // setup the refresh bottom // UPDATING UI
        buttonRefresh();

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
                adapter.notifyDataSetChanged();
                try {
                    addSensorMarkersToMap();
                    Log.i("MainActivity", "sensor data updated");
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    TastyToast.makeText(
                            getApplicationContext(),
                            "Please check internet connection and restart the app ;(",
                            TastyToast.LENGTH_LONG,
                            TastyToast.ERROR);
                }

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

        switch (id) {
            case (R.id.about_index): {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.air_index_scale_source)));
                startActivity(browserIntent);
                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}