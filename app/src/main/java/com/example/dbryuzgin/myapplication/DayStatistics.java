package com.example.dbryuzgin.myapplication;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DayStatistics extends Fragment {

    TextView date, buyersCount, productsCount, totalMoney;
    Calendar calendar = Calendar.getInstance();
    String startDateStr, endDateStr;
    Date currentDate = new Date();
    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.day_statistics, container, false);

        calendar.setTime(currentDate);


        startDateStr = dateFormat.format(calendar.getTime());
        Date startDate = null;
        try {
            startDate = dateFormat.parse(startDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendar.setTime(startDate);

        calendar.add(Calendar.DATE, 1);

        endDateStr = dateFormat.format(calendar.getTime());
        Date endDate = null;
        try {
            endDate = dateFormat.parse(endDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        date = (TextView)rootView.findViewById(R.id.date);
        buyersCount = (TextView)rootView.findViewById(R.id.buyersCount);
        productsCount = (TextView)rootView.findViewById(R.id.productsCount);
        totalMoney = (TextView)rootView.findViewById(R.id.totalMoney);

        date.setText(dateFormat.format(currentDate));

        StatisticsMaker.statsMaker(startDate, endDate, buyersCount, productsCount, totalMoney);

        calendar.clear();

        return rootView;
    }
}
