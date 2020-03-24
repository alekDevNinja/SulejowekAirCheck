package com.github.alekdevninja.sulejowekaircheck;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.alekdevninja.sulejowekaircheck.Looko2Tools.Look2Scraper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
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
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import static android.graphics.Typeface.BOLD;
import static android.graphics.Typeface.ITALIC;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    RecyclerViewAdapter adapter;
    ArrayList<Look2Scraper> sensorList;
    MainViewController mainViewController;

    private WebView webView;

    LatLngBounds SULEJOWEK = new LatLngBounds(
            new LatLng(-44, 113), new LatLng(-10, 154));
//            new LatLng(-52.24446, 21.278449), new LatLng(-10, 154));

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

        //checking if there is a need to update the sensors view
        //checkIfUpdateIsNeeded();


        //open mini map view

//        this works ok
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

    }

    /*
    this works-ish
     */
    @Override
    public void onMapReady(GoogleMap map) {

//        //template marker - this works!
//        map.addMarker(new MarkerOptions()
//                        .position(new LatLng(52.248292, 21.276708))
//                        .title("2")
//                        .snippet("Population: 4,627,300")
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_1))
////                      .icon(BitmapDescriptorFactory.fromResource(iconGen.makeIcon("Text")))
//        );


//        //add markers on map
//        map.addMarker(new MarkerOptions()
//                        .position(new LatLng(52.249172, 21.280839))
//                        .title("1")
////                .snippet("1") //comment below marker name
//                        .icon(BitmapDescriptorFactory.fromResource(android.R.drawable.arrow_down_float)) //R.drawable.common_full_open_on_phone for local res
//        ).showInfoWindow(); //showing title on map
//
//        map.addMarker(new MarkerOptions()
//                        .position(new LatLng(52.248292, 21.276708))
//                        .title("2")
////                .snippet("2") //comment below marker name
//                        .icon(BitmapDescriptorFactory.fromResource(android.R.drawable.arrow_down_float))
//        ).showInfoWindow(); //showing title on map
///////////////////////////////////
//
//        IconGenerator iconGen = new IconGenerator(this);
//        MarkerOptions markerOptions = new MarkerOptions().
//                icon(BitmapDescriptorFactory.fromBitmap(iconGen.makeIcon("Text"))).
//                position(new LatLng(52.253982, 21.295988)).
//                anchor(iconGen.getAnchorU(), iconGen.getAnchorV());


        //setting map type
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        //Adding sensor range
        int radiusDiameter = 400;
        float strokeWidth = 5;
        int strokeColor = Color.parseColor("#ff0000"); //red
        int fillColor = 0x33FF0000; // .fillColor(Color.argb(20, 50, 0, 255)) //this works for tweaking
        float anchorv0 = 0.5f;
        float anchorv1 = 0.5f;

        //Reymonta
        Circle circle1 = map.addCircle(new CircleOptions()
                .center(new LatLng(52.249172, 21.280839))
                .radius(radiusDiameter)
                .strokeWidth(strokeWidth)
                .strokeColor(strokeColor)
                .fillColor(fillColor)
        );
        map.addMarker(new MarkerOptions()
                .position(new LatLng(52.249172, 21.280839))
                .title("Reymonta")
                .snippet("... PM2.5")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_1))
                .anchor(anchorv0,anchorv1)
        ).showInfoWindow();


        //Sul1
        Circle circle2 = map.addCircle(new CircleOptions()
                .center(new LatLng(52.248292, 21.276708))
                .radius(radiusDiameter)
                .strokeWidth(strokeWidth)
                .strokeColor(strokeColor)
                .fillColor(fillColor)
        );
        map.addMarker(new MarkerOptions()
                .position(new LatLng(52.248292, 21.276708))
                .title("Sul1")
                .snippet("... PM2.5")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_2))
                .anchor(anchorv0,anchorv1)
        );


        //Pogodna
        Circle circle3 = map.addCircle(new CircleOptions()
                .center(new LatLng(52.253982, 21.295988))
                .radius(radiusDiameter)
                .strokeWidth(strokeWidth)
                .strokeColor(strokeColor)
                .fillColor(fillColor)
        );
        map.addMarker(new MarkerOptions()
                .position(new LatLng(52.253982, 21.295988))
                .title("Pogodna")
                .snippet("... PM2.5")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_3))
                .anchor(anchorv0,anchorv1)
        );

        // UMS_1
        Circle circle4 = map.addCircle(new CircleOptions()
                .center(new LatLng(52.245063, 21.303239))
                .radius(radiusDiameter)
                .strokeWidth(strokeWidth)
                .strokeColor(strokeColor)
                .fillColor(fillColor)

        );
        map.addMarker(new MarkerOptions()
                .position(new LatLng(52.245063, 21.303239))
                .title("UMS_1")
                .snippet("... PM2.5")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_4))
                .anchor(anchorv0,anchorv1)
        );


        // Wola Grzyb.
        Circle circle5 = map.addCircle(new CircleOptions()
                .center(new LatLng(52.252022, 21.245689))
                .radius(radiusDiameter)
                .strokeWidth(strokeWidth)
                .strokeColor(strokeColor)
                .fillColor(fillColor)
        );
        map.addMarker(new MarkerOptions()
                .position(new LatLng(52.252022, 21.245689))
                .title("Wola Grzybowska")
                .snippet("... PM2.5")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_5))
                .anchor(anchorv0,anchorv1)
        );

    }


//    @Override
//    public void onMapReady(GoogleMap map) {
//
//        map.addMarker(new MarkerOptions()
//                .position(new LatLng(52.248292, 21.276708))
//                .title("Marker"));
//    }

    private void buttonOnTheBottomSetup() {
        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                updateScrapedData();

                Log.i("MainActivity", "---------------------------------------");
                displaySensorLog(0);
                displaySensorLog(1);
                displaySensorLog(2);
                displaySensorLog(3);
                displaySensorLog(4);

                adapter.notifyDataSetChanged();

                Snackbar.make(view, "Info updated", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

//                checkIfUpdateIsNeeded();
            }
        });
    }

    private void scrapDataForAllSensors() {
        for (int i = 0; i < sensorList.size(); i++) {
            sensorList.get(i).updateSensorData();
        }
    }

    private void checkIfUpdateIsNeeded() {
        sensorList.get(0).updateSensorData();
        sensorList.get(1).updateSensorData();
        sensorList.get(2).updateSensorData();
        sensorList.get(3).updateSensorData();
        sensorList.get(4).updateSensorData();

//        (new Handler()).postDelayed(this::checkIfUpdateIsNeeded, 5000);
        System.out.println("test");

//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                int counter = 0;
//                while (!wasDataEverUpdated) {
//                    for (int i = 0; i < sensorList.size(); i++) {
//                        counter++;
//                        Log.i("MainActivity", "counter++: " + counter);
//                        if (sensorList.get(i).isWasUpdated()) {
//                            Log.i("MainActivity", "checkIfUpdateIsNeeded used | counter: " + counter);
//                            buttonOnTheBottomSetup();
//
//                            wasDataEverUpdated = true;
//
//                            break;
//                        }
//                    }
//
////                    sensorList.get(0).updateSensorData();
////                    sensorList.get(1).updateSensorData();
////                    sensorList.get(2).updateSensorData();
////                    sensorList.get(3).updateSensorData();
////                    sensorList.get(4).updateSensorData();
//
//                }
//                adapter.notifyDataSetChanged();
//            }
//        });


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

    private void boottrapingSensorList() {
        sensorList = new ArrayList<>();
        sensorList.add(new Look2Scraper("1", "http://looko2.com/tracker.php?lan=&search=5CCF7F1A546F")); //Sul1
        sensorList.add(new Look2Scraper("2", "http://looko2.com/tracker.php?lan=&search=2C3AE833FFD3")); //Reymonta
        sensorList.add(new Look2Scraper("3", "http://looko2.com/tracker.php?lan=&search=6001944BCDEB")); //Pogodna7
        sensorList.add(new Look2Scraper("4", "http://looko2.com/tracker.php?lan=&search=2C3AE834F051")); //UMS1
        sensorList.add(new Look2Scraper("5", "http://looko2.com/tracker.php?lan=&search=807D3A1F6F4F")); //Wola Grzybowska
    }

    private void recyclerViewSetup(ArrayList<Look2Scraper> sensorList) {
        RecyclerView recyclerView = findViewById(R.id.rvSensors);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, sensorList);
//        adapter.setClickListener((RecyclerViewAdapter.ItemClickListener) this);
        recyclerView.setAdapter(adapter);
    }

    void updateSensorInfo(Look2Scraper look2ScraperObj) {
        updateTextView(look2ScraperObj.getSensorName(), R.id.textView_sensorName);
        updateTextView(look2ScraperObj.getPm25ValueString(), R.id.textView_pm25value);
        updateTextView(look2ScraperObj.getPm25Percentage(), R.id.textView_percentageValue);

        // set name
        //set PM2.5
        //set %

//        TextView textView = (TextView) findViewById(textViewId);
//        textView.setText(sensorName);
    }

    void updateTextView(String dataSource, int textViewId) {
        TextView textView = (TextView) findViewById(textViewId);
        textView.setText(dataSource);
    }

}
