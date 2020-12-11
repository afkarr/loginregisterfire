package com.example.loginregisterfire;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.app.DatePickerDialog;
import java.text.DateFormat;
import java.util.Calendar;

public class Calendar_r extends Fragment implements DatePickerDialog.OnDateSetListener{

    TextView calendar_textView;
    Button button_calendar;

    public Calendar_r() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendar_textView = (TextView) view.findViewById(R.id.calendar_textView);
        button_calendar = (Button) view.findViewById(R.id.calendar_button);

        button_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.setTargetFragment(Calendar_r.this, 0);
                datePicker.show(getFragmentManager(), "date picker");
            }
        });
        return view;
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDate = DateFormat.getDateInstance().format(c.getTime());

        calendar_textView.setText(currentDate);
    }
}