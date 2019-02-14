package com.example.dbryuzgin.myapplication;

import android.graphics.Color;
import android.support.annotation.NonNull;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class ChartResultsMaker {

    public static DatabaseReference billsRef;

    public static void chartDataPicker (final ArrayList<GraphHandler> valuesList, LineChart chart){

        billsRef = FirebaseDatabase.getInstance().getReference().child("Bills");

        for (int i = 0; i < valuesList.size(); i++){

            final int finalI = i;
            billsRef.orderByChild("date").equalTo(valuesList.get(i).getDate()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    double total = 0;

                    if (dataSnapshot.exists()) {

                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                            Bill bill = new Bill(childDataSnapshot.child("date").getValue().toString(), "", "",
                                    "", "", "", childDataSnapshot.child("total").getValue().toString(), "");
                            total = total + Double.parseDouble(bill.getTotal());
                        }
                        handler(valuesList, finalI, total);
                    } else {
                        handler(valuesList, finalI, 0);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            chartGenarator(valuesList, chart);
        }
    }

    public static void handler(ArrayList<GraphHandler> valuesList, int index, double total){
        valuesList.get(index).setTotal(total);
    }

    public static void chartGenarator (ArrayList<GraphHandler> valuesList, LineChart chart){

        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        ArrayList<Entry> yValues = new ArrayList<>();

        for (int i = 0; i < valuesList.size(); i++){

            yValues.add(new Entry(4 + i,(float)valuesList.get(i).getTotal()));
        }

        LineDataSet set1 = new LineDataSet(yValues, "Data set 1");

        set1.setFillAlpha(110);

        set1.setColor(Color.rgb(0, 133, 119));
        set1.setLineWidth(2f);
        set1.setValueTextSize(8f);
        set1.setCircleColor(Color.rgb(216, 27, 96));

        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);

        chart.setData(data);

    }
}