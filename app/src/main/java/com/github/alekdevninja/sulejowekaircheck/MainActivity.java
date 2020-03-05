package com.github.alekdevninja.sulejowekaircheck;

import android.os.Bundle;

import com.github.alekdevninja.sulejowekaircheck.Looko2Tools.Look2Scraper;
import com.github.alekdevninja.sulejowekaircheck.Looko2Tools.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerViewAdapter adapter;
    ArrayList<Look2Scraper> sensorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setup - main layout view + toolbar + support action bar);
        setupMainView();

        // data to populate the RecyclerView with
        boottrapingSensorList();

        // set up the RecyclerView
        recyclerViewSetup(sensorList);

        // button on the bottom // UPDATING UI
        buttonOnTheBottomSetup();
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

    private void setupMainView() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void buttonOnTheBottomSetup() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
////                ////broken
//                updateSensorInfo(looko2Sul1);
//
//////                works!!
////                updateTextView(looko2Sul1.getSensorName(), R.id.textView_sensorName);
//
////              Snackbar.make(view, "Info updated", Snackbar.LENGTH_LONG)
//                Snackbar.make(view, "Info updated", Snackbar.LENGTH_SHORT)
//                        .setAction("Action", null).show();

                sensorList.get(0).updateSensorData();
                sensorList.get(1).updateSensorData();
                sensorList.get(2).updateSensorData();
                sensorList.get(3).updateSensorData();
                sensorList.get(4).updateSensorData();

                Log.i("MainActivity", "---------------------------------------");
                displaySensorLog(0);
                displaySensorLog(1);
                displaySensorLog(2);
                displaySensorLog(3);
                displaySensorLog(4);

//                updateTextView(looko2Sul1.getSensorName(), R.id.textView_sensorName);
//                updateTextView(sensorList.get(1).getSensorName(), 1);
            }
        });
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
        sensorList.add(new Look2Scraper("Sul1", "http://looko2.com/tracker.php?lan=&search=5CCF7F1A546F"));
        sensorList.add(new Look2Scraper("Reymonta", "http://looko2.com/tracker.php?lan=&search=2C3AE833FFD3"));
        sensorList.add(new Look2Scraper("Pogodna7", "http://looko2.com/tracker.php?lan=&search=6001944BCDEB"));
        sensorList.add(new Look2Scraper("UMS1", "http://looko2.com/tracker.php?lan=&search=2C3AE834F051"));
        sensorList.add(new Look2Scraper("Sikorskiego (Wola Grz.)", "http://looko2.com/tracker.php?lan=&search=807D3A1F6F4F"));
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
