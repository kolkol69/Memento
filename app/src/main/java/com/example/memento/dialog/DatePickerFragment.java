package com.example.memento.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.example.memento.Utils;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment{

    private EditText etDateRef;

    DatePickerFragment(EditText etDate){
        super();
        etDateRef = etDate;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    Calendar dateCalendar = Calendar.getInstance();
                    dateCalendar.set(year, month, day);
                    etDateRef.setText(Utils.getDate(dateCalendar.getTimeInMillis()));
                }
            };
}