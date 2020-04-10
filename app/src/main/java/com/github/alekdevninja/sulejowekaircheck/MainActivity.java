package com.github.alekdevninja.sulejowekaircheck;

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
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    RecyclerViewAdapter adapter;
    SupportMapFragment mapFragment;
    public static GoogleMap googleMap;
    private static Context mainActivityContext;
    SensorDB sensorDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setup - main layout view + toolbar + support action bar);
        super.onCreate(savedInstanceState);
        setupMainView();

        // mini-map setup
        initializeMapFragment();

        //refactored version DB 2.0
        sensorDB = new SensorDB();

        //data to populate the RecyclerView with
        boottrapSensorDB();

        //set up the RecyclerView
        recyclerViewSetup(sensorDB.getSensorDB());

        // button on the bottom // UPDATING UI
        buttonOnTheBottomSetup();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        //setting map type
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }

    private void buttonOnTheBottomSetup() {
        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                displayUpdatedData(view); //update recycle view
                googleMap.clear(); //clearing map for a new fresh markers&circles

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
                displayAllSensorsLogs();
            }

//                                                    private void setupParameters() {
////        setId(); // track count of created objects so far - id++
//
//                                                        radiusDiameter = 400; // in meters
//                                                        strokeWidth = 5; // only important  for visuals
//                                                        strokeColor = Color.parseColor("#6d6d6d"); //dark gray | red -> ff0000
//                                                        fillColor = 0x33373737; //  fillColor(Color.argb(20, 50, 0, 255)) //this works for tweaking //gray
//                                                        //  fillColor = Color.argb(128, 255, 0, 0);
////                                                        anchor = ; // amount of offset for the icon to be in the middle of the marker
//                                                    }

//                for (int i = 0; i < sensorDB.getSensorDBSize(); i++) {
////                    sensorDB.getSensor(i).addMapMarkersToGoogleMap();
////                }
//
//            }
////            }
        });
    }

    private void boottrapSensorDB() {
        sensorDB.addSensor(new Sensor("http://looko2.com/tracker.php?lan=&search=5CCF7F1A546F", 52.2483, 21.2767)); //Sul1
        sensorDB.addSensor(new Sensor("http://looko2.com/tracker.php?lan=&search=2C3AE833FFD3", 52.2492, 21.2807)); //Reymonta
        sensorDB.addSensor(new Sensor("http://looko2.com/tracker.php?lan=&search=6001944BCDEB", 52.2539, 21.296)); //Pogodna7
        sensorDB.addSensor(new Sensor("http://looko2.com/tracker.php?lan=&search=2C3AE834F051", 52.2450, 21.3032)); //UMS1
        sensorDB.addSensor(new Sensor("http://looko2.com/tracker.php?lan=&search=807D3A1F6F4F", 52.2521, 21.2456)); //Wola Grzybowska
    }

    private void initializeMapFragment() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void displayUpdatedData(final View view) {
        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                adapter.notifyDataSetChanged(); //@ToDo need to set a delay here

                Log.i("MainActivity", "sensor data updated");

                Snackbar.make(view, "Info updated", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
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

    //    private void recyclerViewSetup(ArrayList<Look2Scraper> sensorList) {
    private void recyclerViewSetup(ArrayList<Sensor> sensors) {
        RecyclerView recyclerView = findViewById(R.id.rvSensors);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, sensors);
//        adapter.setClickListener((RecyclerViewAdapter.ItemClickListener) this);
        recyclerView.setAdapter(adapter);
    }

    public static Context getMainActivityContext() {
        return mainActivityContext;
    }

    public static GoogleMap getGoogleMapContext() {
        return googleMap;
    }

    private void displaySensorLog(int i) {
        Log.i("MainActivity", "---------------------------------------");
        Log.i("MainActivity", "sensors id#" + sensorDB.getSensor(i).getSensorId());
        Log.i("MainActivity", "PM2.5 = " + sensorDB.getSensor(i).getPm25Value());
        Log.i("MainActivity", sensorDB.getSensor(i).getPm25Percentage() + "% of norm");
//        Log.i("MainActivity", "was updated yet: " + sensorDB.getSensor(i).isWasUpdated());
    }

    private void displayAllSensorsLogs() {
        Log.i("MainActivity", "---------------------------------------");
        for (int i = 0; i < sensorDB.getSensorDBSize(); i++) {
            displaySensorLog(i);
        }
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

            case (R.id.action_clear_all_markers):{
                Log.i("MapService", "clearing map");
                googleMap.clear();
                Log.i("MapService", "map cleared");
                break;}

            case (R.id.action_set_color): {
                Toast.makeText(this, "action_set_color clicked!", Toast.LENGTH_LONG).show();

                int idToChange = 0;

//                mapService.mapMarkers.get(idToChange).setFillColor(0x33ff0000);
//                mapService.mapMarkers.get(idToChange).setStrokeColor(Color.argb(128, 255, 0, 0));
                sensorDB.getSensor(idToChange).removeSensorMarker();
                sensorDB.getSensor(idToChange).createSensorMarker(0x33ff0000);


                break;
            }



        }




        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}
