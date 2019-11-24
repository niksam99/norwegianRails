package com.oslomet.norwegianrails.ui;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.oslomet.norwegianrails.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class SelectDateTime extends Fragment {
    String from, to, date, time;
    Button btnDatePicker, btnTimePicker, btnSearch;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;

    public static SelectDateTime newInstance() {
        SelectDateTime fragment = new SelectDateTime();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_select_date_time, container, false);
        if (this.getArguments() != null) {
            from = this.getArguments().getString("from");
            to = this.getArguments().getString("to");
        }
        TextView tvFromTo = v.findViewById(R.id.fromTo);
        String display = "From " + "<b>" + from + "</b> " + "to " + "<b>" + to + "</b> ";
        tvFromTo.setText(Html.fromHtml(display));
        btnDatePicker = v.findViewById(R.id.btn_date);
        btnTimePicker = v.findViewById(R.id.btn_time);
        txtDate = v.findViewById(R.id.in_date);
        txtTime = v.findViewById(R.id.in_time);
        btnSearch = v.findViewById(R.id.btnSearch);

        Date calender = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(calender);
        txtDate.setText(formattedDate);
        txtTime.setText("Now");
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        time = mHour + ":" + mMinute;
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        date = mDay + "-" + (mMonth + 1) + "-" + mYear;
        btnDatePicker.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                date = mDay + "-" + (mMonth + 1) + "-" + mYear;
                final DatePickerDialog datePickerDialog;
                datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }

        });
        btnTimePicker.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                time = mHour + ":" + mMinute;

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                Calendar datetime = Calendar.getInstance();
                                datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                datetime.set(Calendar.MINUTE, minute);
                                if (datetime.getTimeInMillis() >= c.getTimeInMillis()) {
                                    //it's after current
                                    txtTime.setText(hourOfDay + ":" + minute);
                                    time = hourOfDay + ":" + minute;
                                } else {
                                    txtTime.setText("Now");
                                    time = mHour + ":" + mMinute;
                                }

                            }
                        }, mHour, mMinute, false);

                timePickerDialog.show();
            }


        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment searchResult = SearchResult.newInstance();
                Bundle bundle = new Bundle();
                bundle.putString("to", to);
                bundle.putString("from", from);
                bundle.putString("date", date);
                bundle.putString("time", time);
                searchResult.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null);
                transaction.replace(R.id.frame_layout, searchResult);
                transaction.commit();
            }
        });

        return v;
    }
}