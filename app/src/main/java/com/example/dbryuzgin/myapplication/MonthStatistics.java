package com.example.dbryuzgin.myapplication;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MonthStatistics extends Fragment {

    TextView date, buyersCount, productsCount, totalMoney;
    Calendar calendar = Calendar.getInstance();
    Date startDate, endDate;
    DateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.month_statistics, container, false);
        date = (TextView)rootView.findViewById(R.id.date);
        buyersCount = (TextView)rootView.findViewById(R.id.buyersCount);
        productsCount = (TextView)rootView.findViewById(R.id.productsCount);
        totalMoney = (TextView)rootView.findViewById(R.id.totalMoney);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        calendar.clear(Calendar.MONTH);

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        startDate = calendar.getTime();

        calendar.add(Calendar.MONTH, 1);
        endDate = calendar.getTime();

        date.setText(dateFormat.format(startDate));

        StatisticsMaker.statsMaker(startDate, endDate, buyersCount, productsCount, totalMoney);

        return rootView;
    }

}
