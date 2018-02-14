package com.nikolas.manes.globalseismicactivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static String BASE_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    public static String USGS_REQUEST_URL;
    public static int DATE_INFO = 0;
    int minMagnitude = 0;
    static String startDate;
    static String endDate;
    static String startDateURL;
    static String endDateURL;
    TextView minMagnitudeText;
    static TextView startDateText;
    static TextView endDateText;
    TextView endDateTittle;
    CheckBox resultsTillNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startDateText = (TextView) findViewById(R.id.start_date);
        endDateText = (TextView) findViewById(R.id.end_date);
        endDateTittle = (TextView) findViewById(R.id.end_date_tittle);
        minMagnitudeText = (TextView) findViewById(R.id.min_magnitude);
        resultsTillNow = (CheckBox) findViewById(R.id.till_now);
        dateUpdate();
    }

    public void setStartDate(View view) {
        DATE_INFO = 0;
        DialogFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "start_date");
        dateUpdate();
    }

    public void setEndDate(View view) {
        DATE_INFO = 1;
        DialogFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "datePicker");
        dateUpdate();
    }

    public static void dateUpdate() {
        startDateText.setText(getStartDate());
        endDateText.setText(getEndDate());
    }

    public static String getStartDate() {
        if (startDate == null) {
            return "01/01/2000";
        } else {
            return startDate;
        }
    }

    public static String getEndDate() {
        if (endDate == null) {
            return "01/01/2000";
        } else {
            return endDate;
        }
    }

    public void decreaseMagnitude(View view) {
        if (minMagnitude > 0) {
            minMagnitude -= 1;
        }
        setMinMagnitude(minMagnitude);
    }

    public void increaseMagnitude(View view) {
        minMagnitude += 1;
        setMinMagnitude(minMagnitude);
    }

    public void showResults(View view) {
        setUsgsRequestUrl();
        Toast.makeText(getApplicationContext(), getUsgsRequestUrl(), Toast.LENGTH_LONG).show();
        Intent i = new Intent(MainActivity.this, EarthquakeActivity.class);
        startActivity(i);
    }

    public void setMinMagnitude(int i) {
        minMagnitudeText.setText("" + i);
    }

    public int getMinMagnitude() {
        return minMagnitude;
    }

    public void setUsgsRequestUrl() {
        Uri baseUri = Uri.parse(BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("starttime", startDateURL);
        if (!resultsTillNow.isChecked()) {
            uriBuilder.appendQueryParameter("endtime", endDateURL);
        }
        uriBuilder.appendQueryParameter("minmagnitude", String.valueOf(getMinMagnitude()));
        USGS_REQUEST_URL = uriBuilder.toString();
        Log.i(LOG_TAG, USGS_REQUEST_URL);
    }

    public static String getUsgsRequestUrl() {
        return USGS_REQUEST_URL;
    }

    public void checkState(View view) {
        if (resultsTillNow.isChecked()) {
            endDateTittle.setVisibility(View.GONE);
            endDateText.setVisibility(View.GONE);
        }
        else {
            endDateTittle.setVisibility(View.VISIBLE);
            endDateText.setVisibility(View.VISIBLE);
        }
    }
}
