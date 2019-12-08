package com.example.memento.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.memento.Utils;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private EditText etTimeRef;

    TimePickerFragment(EditText etTime){
        super();
        etTimeRef = etTime;
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
        etTimeRef.setText(Utils.getTime(timeCalendar.getTimeInMillis()));
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        etTimeRef.setText(null);
    }
}
