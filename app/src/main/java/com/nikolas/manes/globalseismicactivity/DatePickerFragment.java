package com.nikolas.manes.globalseismicactivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Nikolas on 12/1/2017.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public static final String LOG_TAG = DatePickerFragment.class.getSimpleName();
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        // Use the current date as the default date in the picker
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        datePicker.updateDate(year, month, day);
        if (MainActivity.DATE_INFO == 0) {
            MainActivity.startDate = String.valueOf(DateFormat.format("MM/dd/yyyy", calendar));
            MainActivity.startDateURL = String.valueOf(DateFormat.format("yyyy-MM-dd", calendar));
        }
        else if (MainActivity.DATE_INFO == 1) {
            MainActivity.endDate = String.valueOf(DateFormat.format("MM/dd/yyyy", calendar));
            MainActivity.endDateURL = String.valueOf(DateFormat.format("yyyy-MM-dd", calendar));
        }
        MainActivity.dateUpdate();
    }


}
