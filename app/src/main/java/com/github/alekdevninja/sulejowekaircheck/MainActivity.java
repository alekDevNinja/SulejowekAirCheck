package com.github.alekdevninja.sulejowekaircheck;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.alekdevninja.sulejowekaircheck.Looko2Tools.Look2Scraper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerViewAdapter adapter;
    ArrayList<Look2Scraper> sensorList;
    MainViewController mainViewController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setup - main layout view + toolbar + support action bar);
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
//        checkIfUpdateIsNeeded();
    }

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
        for (int i = sensorList.size(); i > 0; i--) {
            sensorList.get(0).updateSensorData();
            sensorList.get(1).updateSensorData();
            sensorList.get(2).updateSensorData();
            sensorList.get(3).updateSensorData();
            sensorList.get(4).updateSensorData();
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
