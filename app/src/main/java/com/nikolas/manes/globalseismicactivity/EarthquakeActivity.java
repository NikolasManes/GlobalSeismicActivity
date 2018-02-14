package com.nikolas.manes.globalseismicactivity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;

    private EarthquakeAdapter earthquakeAdapter;

    private static final String LOG_TAG = EarthquakeActivity.class.getSimpleName();

    // Getting the URL from MainActivity
    private String USGS_URL = MainActivity.USGS_REQUEST_URL;

    private TextView mEmptyStateTextView;
    private ProgressBar mLoaderSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake);

        earthquakeAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        mLoaderSpinner = (ProgressBar) findViewById(R.id.loading_spinner);
        ListView earthquakeListView = (ListView)findViewById(R.id.list);
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        earthquakeListView.setAdapter(earthquakeAdapter);

        earthquakeListView.setEmptyView(mEmptyStateTextView);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Earthquake currentEarthquake = earthquakeAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getLoaderManager();

        // Checking the internet connection...
        if (networkInfo != null && networkInfo.isConnected()){
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            Log.i(LOG_TAG, "Loader initialising...");
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        }
        else {
            mLoaderSpinner.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet);
        }
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "Creating new Loader...");
        return new EarthquakeLoader(this, USGS_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        Log.i(LOG_TAG, "Loading finished.");
        // Clear the adapter of previous earthquake data
        mLoaderSpinner.setVisibility(View.GONE);
        earthquakeAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (earthquakes != null && !earthquakes.isEmpty()) {
            earthquakeAdapter.addAll(earthquakes);
        }
        else if (QueryUtils.responseCode != 200 && QueryUtils.responseCode !=0){
            mEmptyStateTextView.setText("Error: " + QueryUtils.responseCode + "\nBad Request...");
        }
        else if (QueryUtils.responseCode == 0) {
            mEmptyStateTextView.setText("Process stopped... Connection timeout.");
        }
        else {
            // Set empty state text to display "No earthquakes found."
            mEmptyStateTextView.setText(R.string.no_earthquakes);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        Log.i(LOG_TAG, "Loader Deleted.");
        // Loader reset, so we can clear out our existing data.
        earthquakeAdapter.clear();
    }
}