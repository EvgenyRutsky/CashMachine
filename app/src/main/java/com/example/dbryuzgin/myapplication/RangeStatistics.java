package com.example.dbryuzgin.myapplication;

import android.app.DatePickerDialog;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RangeStatistics extends Fragment {

    TextView date, buyersCount, productsCount, totalMoney, startDate, endDate;
    Button showResults;
    Calendar calendar = Calendar.getInstance();
    String startDateStr, endDateStr;
    DatePickerDialog datePickerDialog;
    Date currentDate = new Date();
    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    DateFormat dateFormatDay = new SimpleDateFormat("dd");
    DateFormat dateFormatMonth = new SimpleDateFormat("MM");
    DateFormat dateFormatYear = new SimpleDateFormat("yyyy");

    private DatePickerDialog.OnDateSetListener mStartDateSetListener;
    private DatePickerDialog.OnDateSetListener mEndDateSetListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.range_statistics, container, false);

        date = (TextView)rootView.findViewById(R.id.date);
        startDate = (TextView)rootView.findViewById(R.id.startDate);
        endDate = (TextView)rootView.findViewById(R.id.endDate);
        buyersCount = (TextView)rootView.findViewById(R.id.buyersCount);
        productsCount = (TextView)rootView.findViewById(R.id.productsCount);
        totalMoney = (TextView)rootView.findViewById(R.id.totalMoney);
        showResults = (Button)rootView.findViewById(R.id.showResults);

        startDate.setText(dateFormat.format(currentDate));
        endDate.setText(dateFormat.format(currentDate));

        calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        final int day = Integer.parseInt(dateFormatDay.format(calendar.getTime()));
        final int month = Integer.parseInt(dateFormatMonth.format(calendar.getTime())) - 1;
        final int year = Integer.parseInt(dateFormatYear.format(calendar.getTime()));

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    datePickerDialog = new DatePickerDialog(RangeStatistics.super.getActivity(), mStartDateSetListener, year, month, day);
                    datePickerDialog.show();
                }
            }
        });

        mStartDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int startYear, int startMonth, int startDayOfMonth) {
                startDateStr = startDayOfMonth + "." + dateFormatMonth.format(startMonth + 1) + "." + startYear;
                startDate.setText(startDateStr);
            }
        };

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    datePickerDialog = new DatePickerDialog(RangeStatistics.super.getActivity(), mEndDateSetListener, year, month, day);
                    datePickerDialog.show();
                }
            }
        });

        mEndDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int endYear, int endMonth, int endDayOfMonth) {
                endDateStr = endDayOfMonth + "." + dateFormatMonth.format(endMonth + 1) + "." + endYear;
                endDate.setText(endDateStr);
            }
        };

        showResults.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    if (dateFormat.parse(startDate.getText().toString()).after(dateFormat.parse(endDate.getText().toString()))){
                        Toast.makeText(RangeStatistics.super.getActivity(), "Введен неверный диапазон", Toast.LENGTH_SHORT).show();
                    } else {
                        StatisticsMaker.statsMaker(dateFormat.parse(startDate.getText().toString()), dateFormat.parse(endDate.getText().toString()), buyersCount, productsCount, totalMoney);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        });

        return rootView;
    }
}

