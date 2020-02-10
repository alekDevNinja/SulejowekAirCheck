package com.github.alekdevninja.sulejowekaircheck.Looko2Tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Look2Scraper implements Runnable {
    private String sensorName;
    private String pm25Value;
    private String pm25Percentage;
    private String scannerOutputLine;
    private String subPageLink;

    /**
     * 1. input the link on the constructor
     * 2. download sub-page to a buffer
     * 3. scan for "PM2.5" line
     * 3.1 - if found, extract only the PM.25 value
     * 4. return PM2.5 the value
     * 4.1 get the % value while scanning for PM2.5
     */

    @Override
    public void run() {
        pm25Value = getPM2Value();
        this.toString();
    }

    public Look2Scraper(String sensorName, String subPageLink) {
        this.sensorName = sensorName;
        this.subPageLink = subPageLink;
        run();
    }

    public String getPM2Value() {
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
                    pm25Value = "offline";
                    pm25Percentage = "offline";
                    scrapedOutput = "offline";
                } else {
                    scrapedOutput = valueExtractor(scannerOutputLine);
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("error with the URL");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("undefined error");
        }
        return scrapedOutput;
    }

    //scraping the PM2.5 value from the subPage
    String valueExtractor(String scannerOutputLine) {
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

        //cut&slash to get PM2.5 only
        pm25StringBuilder.replace(0, 75, "");
        pm25StringBuilder.replace(pm25StringBuilder.indexOf(" "), pm25StringBuilder.length(), "");

        //cut&shash to get the percentage value only
        percentageStringBuilder.replace(0, percentageStringBuilder.indexOf("(") + 1, "");
        percentageStringBuilder.replace(percentageStringBuilder.indexOf("%"), percentageStringBuilder.length(), "");


        pm25Percentage = percentageStringBuilder.toString();
        outputString = pm25StringBuilder.toString();
        return outputString;
    }

    @Override
    public String toString() {
        if (pm25Value.equals("offline")) {
            System.out.println(
                    sensorName + ":\t" + pm25Value

            );
        } else {
            System.out.println(
                    sensorName + ":\t" + pm25Value + " PM 2.5 | " +
                            pm25Percentage + "% of healthy"
            );
        }
        return "Look2Scraper{" +
                "pm25Value='" + pm25Value + '\'' +
                '}';
    }

    public String getPm25Percentage() {
        return pm25Percentage;
    }

}
