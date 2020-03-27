package com.github.alekdevninja.sulejowekaircheck;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    RecyclerViewAdapter adapter;
    ArrayList<Look2Scraper> sensorList;
    MainViewController mainViewController;
    SupportMapFragment mapFragment;
    boolean updatedAfterStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setup - main layout view + toolbar + support action bar);
        super.onCreate(savedInstanceState);
        setupMainView(mainViewController);

        //data to populate the RecyclerView with
        boottrapingSensorList();

        //obtaining data from the web for the initial data set
        scrapDataForAllSensors();

        //set up the RecyclerView
        recyclerViewSetup(sensorList);

        // button on the bottom // UPDATING UI
        buttonOnTheBottomSetup();

        //map setup
//        MapService mapService = new MapService(R.id.map, sensorList); //refactoring as a seperate class

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap map) {
        //setting map type
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        //Adding sensor range
        int radiusDiameter = 400;
        float strokeWidth = 5;
//        int strokeColor = Color.parseColor("#ff0000"); //red
        int strokeColor = Color.parseColor("#6d6d6d"); //dark gray
//        int fillColor = 0x33FF0000; // .fillColor(Color.argb(20, 50, 0, 255)) //RED
        int fillColor = 0x33373737; // .fillColor(Color.argb(20, 50, 0, 255)) //this works for tweaking //gray
        float anchorV0 = 0.5f;
        float anchorV1 = 0.5f;


        for (int i = 0; i < sensorList.size(); i++) {
            //Reymonta
            Circle circle = map.addCircle(new CircleOptions()
                    .center(sensorList.get(i).getSensorCoordinates())
                    .radius(radiusDiameter)
                    .strokeWidth(strokeWidth)
                    .strokeColor(strokeColor)
                    .fillColor(fillColor)
            );

            int id = getResources().getIdentifier("com.github.alekdevninja.sulejowekaircheck:drawable/ic_" + (i + 1),
                    "Drawable",
                    "com.github.alekdevninja.sulejowekaircheck");

            map.addMarker(new MarkerOptions()
                            .position(sensorList.get(i).getSensorCoordinates())
                            .title(sensorList.get(i).getSensorName())
//                            .snippet(sensorList.get(i).getPm25Value() + (" ug/m3 PM2.5")) // @Todo fix display after data refresh
                            .icon(BitmapDescriptorFactory
                                    .fromResource(id))
                            .anchor(anchorV0, anchorV1)
            );
        }
    }

    private void boottrapingSensorList() {
        sensorList = new ArrayList<>();
        sensorList.add(new Look2Scraper("1", "http://looko2.com/tracker.php?lan=&search=5CCF7F1A546F", 52.2483, 21.2767)); //Sul1
        sensorList.add(new Look2Scraper("2", "http://looko2.com/tracker.php?lan=&search=2C3AE833FFD3", 52.2492, 21.2807)); //Reymonta
        sensorList.add(new Look2Scraper("3", "http://looko2.com/tracker.php?lan=&search=6001944BCDEB", 52.2539, 21.296));  //Pogodna7
        sensorList.add(new Look2Scraper("4", "http://looko2.com/tracker.php?lan=&search=2C3AE834F051", 52.2450, 21.3032)); //UMS1
        sensorList.add(new Look2Scraper("5", "http://looko2.com/tracker.php?lan=&search=807D3A1F6F4F", 52.2521, 21.2456)); //Wola Grzybowska
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

    private void setupMainView(MainViewController viewController) {
        updatedAfterStart = false;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewController = new MainViewController();
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
