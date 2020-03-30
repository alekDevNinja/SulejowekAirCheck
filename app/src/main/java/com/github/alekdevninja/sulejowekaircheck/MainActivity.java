package com.github.alekdevninja.sulejowekaircheck;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.alekdevninja.sulejowekaircheck.Looko2Tools.Look2Scraper;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    RecyclerViewAdapter adapter;
    ArrayList<Look2Scraper> sensorList;
    SupportMapFragment mapFragment;
    boolean updatedAfterStart;
    private static Context mainActivityContext;
    CircleOptions testCircle;

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

        //map setup
//        MapService mapService = new MapService(R.id.map, sensorList);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap map) {
        MapMarker mapMarker = new MapMarker(sensorList, map);

        //setting map type
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);


//        //only for testing
//        //markers setup parameters
//        int radiusDiameter = 400; // in meters
//        float strokeWidth = 5; // only important  for visuals
//        int strokeColor = Color.parseColor("#6d6d6d"); //dark gray | red -> ff0000
//        int fillColor = 0x336d6d6d; // .fillColor(Color.argb(20, 50, 0, 255)) //this works for tweaking //gray
//        float anchor = 0.5f; // amount of offset for the icon to be in the middle of the marker
//
//        testCircle = new CircleOptions()
//                .center(sensorList.get(5).getSensorCoordinates())
//                .radius(radiusDiameter)
//                .strokeWidth(strokeWidth)
//                .strokeColor(strokeColor)
//                .fillColor(fillColor);
//
//        map.addCircle(testCircle);

    }

//    private void createMapMarkers(ArrayList<Look2Scraper> sensorList, GoogleMap map) {
//        //Adding sensor display parameters
//        int radiusDiameter = 400; // in meters
//        float strokeWidth = 5; // only important  for visuals
//        int strokeColor = Color.parseColor("#6d6d6d"); //dark gray | red -> ff0000
//        int fillColor = 0x33373737; // .fillColor(Color.argb(20, 50, 0, 255)) //this works for tweaking //gray
//        float anchor = 0.5f; // amount of offset for the icon to be in the middle of the marker
//
//        for (int i = 0; i < sensorList.size(); i++) {
//
//            map.addCircle(new CircleOptions()
//                    .center(sensorList.get(i).getSensorCoordinates())
//                    .radius(radiusDiameter)
//                    .strokeWidth(strokeWidth)
//                    .strokeColor(strokeColor)
//                    .fillColor(fillColor)
//            );
//
//            int icon_id = getResources().getIdentifier("com.github.alekdevninja.sulejowekaircheck:drawable/ic_" + (i + 1),
//                    "Drawable",
//                    "com.github.alekdevninja.sulejowekaircheck"); // finding #id for the correct R.drawable. icon
//
//            map.addMarker(new MarkerOptions()
//                            .position(sensorList.get(i).getSensorCoordinates())
//                            .title(sensorList.get(i).getSensorName())
////                            .snippet(sensorList.get(i).getPm25Value() + (" ug/m3 PM2.5")) // @Todo fix display after data refresh
//                            .icon(BitmapDescriptorFactory
//                                    .fromResource(icon_id))
//                            .anchor(anchor, anchor)
//            );
//        }
//    }

    private void boottrapingSensorList() {
        sensorList = new ArrayList<>();
        sensorList.add(new Look2Scraper("1", "http://looko2.com/tracker.php?lan=&search=5CCF7F1A546F", 52.2483, 21.2767)); //Sul1
        sensorList.add(new Look2Scraper("2", "http://looko2.com/tracker.php?lan=&search=2C3AE833FFD3", 52.2492, 21.2807)); //Reymonta
        sensorList.add(new Look2Scraper("3", "http://looko2.com/tracker.php?lan=&search=6001944BCDEB", 52.2539, 21.296));  //Pogodna7
        sensorList.add(new Look2Scraper("4", "http://looko2.com/tracker.php?lan=&search=2C3AE834F051", 52.2450, 21.3032)); //UMS1
        sensorList.add(new Look2Scraper("5", "http://looko2.com/tracker.php?lan=&search=807D3A1F6F4F", 52.2521, 21.2456)); //Wola Grzybowska
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

                    //for testing only
//                    testCircle.fillColor(Color.parseColor("#ff0000"));
//                    mapFragment = null;
//
                }
            }
        });


    }

//    private void initializeMapFragment() {
//        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//    }

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
        handler.postDelayed(r, 2500);

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
//        viewController = new MainViewController();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

//    void updateSensorInfo(Look2Scraper look2ScraperObj) {
//        updateTextView(look2ScraperObj.getSensorName(), R.id.textView_sensorName);
//        updateTextView(look2ScraperObj.getPm25ValueString(), R.id.textView_pm25value);
//        updateTextView(look2ScraperObj.getPm25Percentage(), R.id.textView_percentageValue);
//
//        // set name
//        //set PM2.5
//        //set %
//
////        TextView textView = (TextView) findViewById(textViewId);
////        textView.setText(sensorName);
//    }
//
//    void updateTextView(String dataSource, int textViewId) {
//        TextView textView = (TextView) findViewById(textViewId);
//        textView.setText(dataSource);
//    }

}
