package com.nikolas.manes.globalseismicactivity;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikolas on 12/1/2017.
 */

public class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    public static int responseCode = 200;

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    public static List<Earthquake> fetchEarthquakeData(String requestUrl) {
        Log.i(LOG_TAG, "Fetching Data from Internet...");
        URL url = createURL(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
            return null;
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        List<Earthquake> earthquakes = extractEarthquakes(jsonResponse);

        // Return the {@link Event}
        return earthquakes;
    }
    // Create a URL object
    private static URL createURL(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error creating the URL...");
            return null;
        }
        return url;
    }
    // Make HTTP request and get JSON response code
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null...
        if (url == null){
            return jsonResponse;
        }
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else {
                responseCode = httpURLConnection.getResponseCode();
                Log.e(LOG_TAG, "Error with URL connection. Response code: " + responseCode);
            }
        } catch (ProtocolException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results.");
        } catch (SocketTimeoutException e){
            responseCode = 0;
            Log.e(LOG_TAG, "Process stopped... It takes too long");
        }

        return jsonResponse;
    }
    // Convert the input stream to a String
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Earthquake> extractEarthquakes(String jsonResponse){
        // First create an empty Array
        ArrayList<Earthquake> earthquakes = new ArrayList<>();
        /**
         * Trying to parse JSON response if there is a problem with the way
         * it is formatted it throws an exception that we catch
         * so the Application doesn't crash.
         */
        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray featuresArray = root.getJSONArray("features");

            // Pass through the JSON Array to get the parameters we need
            for (int i = 0; i < featuresArray.length(); i++) {
                JSONObject current = featuresArray.getJSONObject(i);
                JSONObject properties = current.getJSONObject("properties");
                String location = properties.getString("place");
                double magnitude = properties.getDouble("mag");
                long time = properties.getLong("time");
                String url = properties.getString("url");
                earthquakes.add(new Earthquake(location, magnitude, time, url));
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing JSON response...", e);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        } catch (Exception e) {
            Log.e(LOG_TAG, "Unknown exception");
        }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        return earthquakes;
    }
}