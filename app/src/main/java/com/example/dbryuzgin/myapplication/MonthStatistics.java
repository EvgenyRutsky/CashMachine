package com.example.dbryuzgin.myapplication;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MonthStatistics extends Fragment {

    TextView date, buyersCount, productsCount, totalMoney;
    Calendar calendar = Calendar.getInstance();
    Date startDate, endDate;
    LineChart chart;
    DateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.month_statistics, container, false);
        date = (TextView)rootView.findViewById(R.id.date);
        buyersCount = (TextView)rootView.findViewById(R.id.buyersCount);
        productsCount = (TextView)rootView.findViewById(R.id.productsCount);
        totalMoney = (TextView)rootView.findViewById(R.id.totalMoney);
        chart = (LineChart)rootView.findViewById(R.id.weekChart);

        calendar.set(Calendar.HOUR_OF_DAY, 0);

        calendar.set(Calendar.DAY_OF_MONTH, 2);
        startDate = calendar.getTime();

        calendar.add(Calendar.MONTH, 1);
        endDate = calendar.getTime();

        date.setText(dateFormat.format(startDate));

        final ArrayList<GraphHandler> valuesList = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        DateTimeFormatter localDateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String startDateStr = dateFormat.format(startDate);
        String endDateStr = dateFormat.format(endDate);

        LocalDate localStartDate = LocalDate.parse(startDateStr, localDateFormat);
        LocalDate localEndDate = LocalDate.parse(endDateStr, localDateFormat);

        int daysRange = (int) ChronoUnit.DAYS.between(localStartDate, localEndDate);

        for (int i = 0; i<daysRange; i++){
            LocalDate loopDate = localStartDate.plusDays(i);
            GraphHandler graphHandler = new GraphHandler(localDateFormat.format(loopDate), 0);
            valuesList.add(graphHandler);
        }

        StatisticsMaker.statsMaker(startDate, endDate, buyersCount, productsCount, totalMoney);

        date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ChartResultsMaker.chartDataPicker(valuesList, chart);
            }

        });

        return rootView;
    }

}
