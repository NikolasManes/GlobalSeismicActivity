package com.nikolas.manes.globalseismicactivity;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Nikolas on 12/1/2017.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    private static final String LOG_TAG = EarthquakeAdapter.class.getSimpleName();

    public EarthquakeAdapter(Context context, List<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item, parent, false);
        }
        Earthquake currentEarthquake = getItem(position);

        Date dateObject = new Date(currentEarthquake.getTime());
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        // Magnitude
        TextView magnitudeText = (TextView) listItemView.findViewById(R.id.magnitude);
        magnitudeText.setText(decimalFormat.format(currentEarthquake.getMagnitude()));
        // Location
        TextView locationText = (TextView) listItemView.findViewById(R.id.location_text);
        locationText.setText(currentEarthquake.getLocation()[0]);
        // City
        TextView cityText = (TextView) listItemView.findViewById(R.id.city_text);
        cityText.setText(currentEarthquake.getLocation()[1]);
        // Date
        TextView dateText = (TextView) listItemView.findViewById(R.id.date_text);
        dateText.setText(dateFormat.format(dateObject));
        // Time
        TextView timeText = (TextView) listItemView.findViewById(R.id.time_text);
        timeText.setText(timeFormat.format(dateObject));

        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magCircle = (GradientDrawable) magnitudeText.getBackground();
        // Set the background circle color depending on the size of earthquake
        int magColor = getMagColor(currentEarthquake.getMagnitude());
        magCircle.setColor(magColor);
        return listItemView;
    }

    private int getMagColor(double magnitude) {
        int magColorResID;
        int magFloor = (int) Math.floor(magnitude);
        switch (magFloor){
            case 0:
            case 1:
                magColorResID = R.color.magnitude1;
                break;
            case 2:
                magColorResID = R.color.magnitude2;
                break;
            case 3:
                magColorResID = R.color.magnitude3;
                break;
            case 4:
                magColorResID = R.color.magnitude4;
                break;
            case 5:
                magColorResID = R.color.magnitude5;
                break;
            case 6:
                magColorResID = R.color.magnitude6;
                break;
            case 7:
                magColorResID = R.color.magnitude7;
                break;
            case 8:
                magColorResID = R.color.magnitude8;
                break;
            case 9:
                magColorResID = R.color.magnitude9;
                break;
            default:
                magColorResID = R.color.magnitude10plus;
        }
        return ContextCompat.getColor(getContext(), magColorResID);
    }
}
