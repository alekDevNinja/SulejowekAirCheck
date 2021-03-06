package com.github.alekdevninja.sulejowekaircheck.sensorTools;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Scraper implements Runnable {
    private String pm25Value; // scraped & formatted PM2.5 value
    private String pm25Percentage; // scraped & formatted % of healthy norm value
    private String scannerOutputLine; // working area for scraping
    private String subPageLink; // link for the designated sensor web page


    public Scraper(String subPageLink) {
        this.subPageLink = subPageLink;
    }

    void updateSensorData() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                pm25Value = extractPM2Value();
                Log.i("Scraper", " Sensor was updated " + pm25Value);
            }
        });
    }

    public String extractPM2Value() {
        String scrapedOutput = "no output scraped";
        try {
            // Make a URL to the web page
            URL url = new URL(subPageLink);

            // Get the input stream through URL Connection
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            // read each line and scan for PM2.5 value
            while ((scannerOutputLine = bufferedReader.readLine()) != null) {
                if (scannerOutputLine.startsWith("Czujnik nie przesyłał danych")) {
                    pm25Value = "offline ";
                    pm25Percentage = "";
                    scrapedOutput = "offline ";
                } else {
                    scrapedOutput = valueExtractor(scannerOutputLine);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("Scraper", "error with the URL");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Scraper", "undefined error");
        }

        return scrapedOutput;
    }

    //scraping the PM2.5 value from the subPage
    private String valueExtractor(String scannerOutputLine) {
        this.scannerOutputLine = scannerOutputLine;
        String extractorRawOutput = "something went wrong (getPM2Value() method)";

        //does the page contain a PM2.5 value check
        if (scannerOutputLine.startsWith("<div  class=\"col-sm-4\" style=\"background-color:#eeeeee;\"><H4>PM2.5</H4><BR>")) {
            // saving the PM2.5 value
            pm25Value = scannerOutputLine;
            pm25Value = makeScrapedDataReadable(pm25Value);
        }

        extractorRawOutput = pm25Value;
        return extractorRawOutput;
    }

    private String makeScrapedDataReadable(String pm25Value) {
        String outputString = "something went wrong in the \"makeScrapedDataReadable()\"";

        //import the raw data
        StringBuilder pm25StringBuilder = new StringBuilder();
        pm25StringBuilder.append(pm25Value);
        StringBuilder percentageStringBuilder = new StringBuilder();
        percentageStringBuilder.append(pm25Value);

        pm25StringBuilder.replace(0, 75, "");
        pm25StringBuilder.replace(pm25StringBuilder.indexOf(" "), pm25StringBuilder.length(), "");

        //cut&slash to get the percentage value only
        percentageStringBuilder.replace(0, percentageStringBuilder.indexOf("(") + 1, "");
        percentageStringBuilder.replace(percentageStringBuilder.indexOf("%"), percentageStringBuilder.length(), "");
        percentageStringBuilder.append("% ");

        pm25Percentage = percentageStringBuilder.toString();
        outputString = pm25StringBuilder.toString();
        return outputString;
    }

    String getPm25Value() {
        return pm25Value;
    }

    String getPm25Percentage() {
        return pm25Percentage;
    }

    @Override
    public void run() {

    }
}
