package com.example.memento.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.TimePicker;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.memento.Utils;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private EditText edTimeRef;

    TimePickerFragment(EditText edTime){
        super();
        edTimeRef = edTime;
    }


    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.set(0, 0, 0, hourOfDay, minute);
        edTimeRef.setText(Utils.getTime(timeCalendar.getTimeInMillis()));
    }
}
