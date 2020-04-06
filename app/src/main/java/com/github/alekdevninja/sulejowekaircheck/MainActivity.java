package com.github.alekdevninja.sulejowekaircheck;

import android.content.Context;
import android.graphics.Color;
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

import com.github.alekdevninja.sulejowekaircheck.Looko2Tools.Look2Scraper;
import com.github.alekdevninja.sulejowekaircheck.Map.MapMarker;
import com.github.alekdevninja.sulejowekaircheck.Map.MapService;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    RecyclerViewAdapter adapter;
    ArrayList<Look2Scraper> sensorList;
    SupportMapFragment mapFragment;
    boolean updatedAfterStart = false;
    private static Context mainActivityContext;
    private GoogleMap googleMap;
    private MapService mapService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setup - main layout view + toolbar + support action bar);
        super.onCreate(savedInstanceState);
        setupMainView();

        //data to populate the RecyclerView with
        boottrapingSensorList();

        //obtaining data from the web for the initial data set
        scrapDataForAllSensors();

        //set up the RecyclerView
        recyclerViewSetup(sensorList);

        // button on the bottom // UPDATING UI
        buttonOnTheBottomSetup();

        // mini-map setup
        initializeMapFragment();
        mapService = new MapService(sensorList);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;

        //setting map type
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        mapService.populateMapWithMarkers(map);
    }

    private void boottrapingSensorList() {
        sensorList = new ArrayList<>();
//        sensorList.add(new Look2Scraper("1", "http://looko2.com/tracker.php?lan=&search=5CCF7F1A546F", 52.2483, 21.2767)); //Sul1
//        sensorList.add(new Look2Scraper("2", "http://looko2.com/tracker.php?lan=&search=2C3AE833FFD3", 52.2492, 21.2807)); //Reymonta
//        sensorList.add(new Look2Scraper("3", "http://looko2.com/tracker.php?lan=&search=6001944BCDEB", 52.2539, 21.296));  //Pogodna7
//        sensorList.add(new Look2Scraper("4", "http://looko2.com/tracker.php?lan=&search=2C3AE834F051", 52.2450, 21.3032)); //UMS1
//        sensorList.add(new Look2Scraper("5", "http://looko2.com/tracker.php?lan=&search=807D3A1F6F4F", 52.2521, 21.2456)); //Wola Grzybowska

        sensorList.add(new Look2Scraper("test", "http://looko2.com/tracker.php?lan=&search=807D3A1F6F4F", 52.2400, 21.2456)); //TEST
    }

    private void buttonOnTheBottomSetup() {
        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (updatedAfterStart) {
                    updatedAfterStart = true;
                    displayAllSensorsLogs();
                    displayUpdatedData(view);
                } else {
                    displayAllSensorsLogs();
                    scrapDataForAllSensors();
                    displayUpdatedData(view);
                }
            }
        });
    }

    private void initializeMapFragment() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void displayUpdatedData(final View view) {
        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                adapter.notifyDataSetChanged(); // need to set a delay here
                Log.i("MainActivity", "sensor data updated");
                Snackbar.make(view, "Info updated", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        };
        handler.postDelayed(r, 2000);

    }

    private void scrapDataForAllSensors() {
        for (int i = 0; i < sensorList.size(); i++) {
            sensorList.get(i).updateSensorData();
        }
    }

    private void setupMainView() {

        updatedAfterStart = false;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mainActivityContext = getApplicationContext(); //setting main activity context for other fragments
    }

    private void displaySensorLog(int i) {
        Log.i("MainActivity", "---------------------------------------");
        Log.i("MainActivity", "sensors preview:");
        Log.i("MainActivity", sensorList.get(i).getSensorName() + " id#" + sensorList.get(i).getSensorId());
        Log.i("MainActivity", "PM2.5 = " + sensorList.get(i).getPm25Value());
        Log.i("MainActivity", sensorList.get(i).getPm25Percentage() + "% of norm");
        Log.i("MainActivity", "was updated yet: " + sensorList.get(i).isWasUpdated());
    }

    private void displayAllSensorsLogs() {
        Log.i("MainActivity", "---------------------------------------");
        for (int i = 0; i < sensorList.size(); i++) {
            displaySensorLog(i);
        }
    }

    private void recyclerViewSetup(ArrayList<Look2Scraper> sensorList) {
        RecyclerView recyclerView = findViewById(R.id.rvSensors);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, sensorList);
//        adapter.setClickListener((RecyclerViewAdapter.ItemClickListener) this);
        recyclerView.setAdapter(adapter);
    }

    public static Context getMainActivityContext() {
        return mainActivityContext;
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

//            case (R.id.action_clear_all_markers):{
//                Log.i("MapService", "clearing map");
//                googleMap.clear();
//                Log.i("MapService", "map cleared");
////                Log.i("MapService", "populating map with markers");
////                mapService.populateMapWithMarkers(googleMap);
////                Log.i("MapService", "map populated with markers");
//                break;}

//            case (R.id.action_populateMapWithMarkers):{
//                mapService.populateMapWithMarkers(googleMap);
//                break;}

//            case (R.id.action_set_color): {
//                Toast.makeText(this, "action_set_color clicked!", Toast.LENGTH_LONG).show();
//                //test features here:
//
//
//                mapService.removeAllExistingMarkersInDB();
//                mapService.generateAllMarkers();
//
//                int idToChange = 0;
//
////                mapService.mapMarkers.get(1).setFillColor(Color.parseColor("#ff0000"));
////                mapService.mapMarkers.get(1).setStrokeColor(Color.parseColor("#ff0000"));
//                Log.i("MapService", "old colors:");
//                Log.i("MapService", "fill color: " + mapService.mapMarkers.get(idToChange).getFillColor());
//                Log.i("MapService", "stroke color: " + mapService.mapMarkers.get(idToChange).getStrokeColor());
//
//                mapService.mapMarkers.get(idToChange).setFillColor(0x33ff0000);
////                mapService.mapMarkers.get(idToChange).setStrokeColor(Color.argb(128, 255, 0, 0));
//
//                Log.i("MapService", "new colors: ");
//                Log.i("MapService", "fill color: " + mapService.mapMarkers.get(idToChange).getFillColor());
//                Log.i("MapService", "stroke color: " + mapService.mapMarkers.get(idToChange).getStrokeColor());
//
////                googleMap.addMarker(mapService.mapMarkers.get(1).getMarker());
////                googleMap.addCircle(mapService.mapMarkers.get(1).getCircle());
//
//
//                mapService.populateMapWithMarkers(googleMap);
//                break;
//            }

//            case (R.id.action_delete_all_markers):{
////                Toast.makeText(this, "clear clicked!", Toast.LENGTH_LONG).show();
//                googleMap.clear();
//                mapService.mapMarkers = new ArrayList<MapMarker>();
//                sensorList = new ArrayList<Look2Scraper>();
//                MapMarker.resetIdCounter();
//                break;}

//            case (R.id.action_add_all_markers):{
//                Toast.makeText(this, "add all marker clicked!", Toast.LENGTH_LONG).show();
//                mapService.generateAllMarkers();
//                mapService.populateMapWithMarkers(googleMap);
//                break;}

            case (R.id.action_add_new_sensor):{
                sensorList.add(new Look2Scraper("test", "http://looko2.com/tracker.php?lan=&search=807D3A1F6F4F", 52.2400, 21.2456)); //TEST

                int newSensorId = sensorList.size() - 1;
//                int newSensorId = MapMarker.getId();
//                int newSensorId = sensorList.size();

                mapService.mapMarkers.add(
                        new MapMarker(
                                sensorList.get(newSensorId).getSensorCoordinates(),
                                mainActivityContext,
                                newSensorId,
                                sensorList.get(newSensorId).getSensorName(),
                                0x33ff0000
                        )
                );

                googleMap.addCircle(mapService.mapMarkers.get(newSensorId).getCircle());
                googleMap.addMarker(mapService.mapMarkers.get(newSensorId).getMarker());
                break;}

            case (R.id.action_refresh_map):{
                googleMap.clear();
//                mapService.removeAllExistingMarkersInDB();
//                mapService.generateAllMarkers();
//                mapService.populateMapWithMarkers(googleMap);

                break;}


            case (R.id.action_print_log_in_console):{
                Toast.makeText(this, "Logs printed to console", Toast.LENGTH_LONG).show();
                Log.i("Backlog", "---------------------------------------");

                //print sensorList
                Log.i("Backlog", "sensorList content:");
                for (int i = 0; i < sensorList.size(); i++) {
                    Log.i("Backlog", "sensor " + sensorList.get(i).getSensorId());
                    System.out.println(sensorList.get(i).getSensorId());
                }

                Log.i("Backlog", "all markers existing:" + MapMarker.getId());
//                Log.i("Backlog", "markerList content: ");
//                for (int i = 0; i < mapService.mapMarkers.size(); i++) {
////                    Log.i("Backlog", "marker " + MapService.markersCreated
//                            );
//                }
                break;}


        }


//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            Toast.makeText(this, "TestButton clicked!", Toast.LENGTH_LONG).show();
//            //test features here:
//
//            mapService.mapMarkers.get(1).setFillColor(Color.parseColor("#ff0000"));
//            mapService.mapMarkers.get(1).setStrokeColor(Color.parseColor("#ff0000"));
//            Log.i("MapService", "fill color: " + mapService.mapMarkers.get(1).getFillColor());
//            Log.i("MapService", "stroke color: " + mapService.mapMarkers.get(1).getStrokeColor());
//            googleMap.clear();
//            googleMap.addMarker(mapService.mapMarkers.get(1).getMarker());
//            googleMap.addCircle(mapService.mapMarkers.get(1).getCircle());
//
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}
