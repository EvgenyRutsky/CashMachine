package com.example.dbryuzgin.myapplication;

import android.app.DatePickerDialog;
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
    Date currentDate = new Date();
    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    private int mYearStart, mMonthStart, mDayStart, mYearEnd, mMonthEnd, mDayEnd;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.range_statistics, container, false);

        date = (TextView)rootView.findViewById(R.id.date);
        startDate = (TextView)rootView.findViewById(R.id.startDate);
        endDate = (TextView)rootView.findViewById(R.id.endDate);
        buyersCount = (TextView)rootView.findViewById(R.id.buyersCount);
        productsCount = (TextView)rootView.findViewById(R.id.productsCount);
        totalMoney = (TextView)rootView.findViewById(R.id.totalMoney);
        showResults = (Button)rootView.findViewById(R.id.showResults);

        startDate.setText(dateFormat.format(currentDate));
        endDate.setText(dateFormat.format(currentDate));

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                mYearStart = c.get(Calendar.YEAR);
                mMonthStart = c.get(Calendar.MONTH);
                mDayStart = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(RangeStatistics.super.getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int yearStart, int monthOfYearStart, int dayOfMonthStart) {

                                String startDraft = dayOfMonthStart + "." + (monthOfYearStart + 1) + "." + yearStart;
                                DateFormat draftDateChanger = new SimpleDateFormat("d.M.yyyy");
                                try {
                                    Date newStartDate = draftDateChanger.parse(startDraft);
                                    startDate.setText(dateFormat.format(newStartDate));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, mYearStart, mMonthStart, mDayStart);
                dpd.show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar cal = Calendar.getInstance();
                mYearEnd = cal.get(Calendar.YEAR);
                mMonthEnd = cal.get(Calendar.MONTH);
                mDayEnd = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(RangeStatistics.super.getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {

                                String endDraft = dayOfMonthEnd + "." + (monthOfYearEnd + 1) + "." + yearEnd;
                                DateFormat draftDateChanger = new SimpleDateFormat("d.M.yyyy");
                                try {
                                    Date newEndDate = draftDateChanger.parse(endDraft);
                                    endDate.setText(dateFormat.format(newEndDate));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, mYearEnd, mMonthEnd, mDayEnd);
                dpd.show();
            }
        });



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

