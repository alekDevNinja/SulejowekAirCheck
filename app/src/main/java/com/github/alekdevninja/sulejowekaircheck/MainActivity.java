package com.github.alekdevninja.sulejowekaircheck;

import android.os.AsyncTask;
import android.os.Bundle;

import com.github.alekdevninja.sulejowekaircheck.Looko2Tools.Look2Scraper;
import com.github.alekdevninja.sulejowekaircheck.Looko2Tools.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerViewAdapter adapter;
    Look2Scraper looko2Sul1;
    Look2Scraper looko2Reymonta;
    Look2Scraper looko2Pogodna7;
    Look2Scraper looko2SikorskiegoWolaGrzybowska;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // data to populate the RecyclerView with
        ArrayList<String> sensorList = new ArrayList<>();
        sensorList.add("sensor #1");
        sensorList.add("sensor #2");
        sensorList.add("sensor #3");
        sensorList.add("sensor #4");
        sensorList.add("sensor #5");

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvSensors);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, sensorList);
//        adapter.setClickListener((RecyclerViewAdapter.ItemClickListener) this);
        recyclerView.setAdapter(adapter);


        fetchAirData("Sul1", "http://looko2.com/tracker.php?lan=&search=5CCF7F1A546F");

//        fetchAirData("Reymonta", "http://looko2.com/tracker.php?lan=&search=2C3AE833FFD3");
//        fetchAirData("Pogodna7", "http://looko2.com/tracker.php?lan=&search=6001944BCDEB");
//        fetchAirData("UMS1", "http://looko2.com/tracker.php?lan=&search=2C3AE834F051");
//        fetchAirData("SikorskiegoWolaGrzybowska", "http://looko2.com/tracker.php?lan=&search=807D3A1F6F4F");


        // button on the bottom // UPDATING UI
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ////broken
                updateSensorInfo(looko2Sul1);


////                works!!
//                updateTextView(looko2Sul1.getSensorName(), R.id.textView_sensorName);

//              Snackbar.make(view, "Info updated", Snackbar.LENGTH_LONG)
                Snackbar.make(view, "Info updated", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });
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


    private void fetchAirData(final String sensorName, final String subUrl) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                looko2Sul1 = new Look2Scraper(sensorName, subUrl);
            }
        });
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
