package com.example.dbryuzgin.myapplication;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class TopSaleActivity extends AppCompatActivity {

    TextView name1, name2, name3, name4, name5;
    TextView rate1, rate2, rate3, rate4, rate5;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_sale);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.85), (int)(height*.7));

        TextView[] names = new TextView[]{
                name1 = (TextView) findViewById(R.id.name1),
                name2 = (TextView) findViewById(R.id.name2),
                name3 = (TextView) findViewById(R.id.name3),
                name4 = (TextView) findViewById(R.id.name4),
                name5 = (TextView) findViewById(R.id.name5)
        };
        TextView[] rates = new TextView[]{
                rate1 = (TextView) findViewById(R.id.rate1),
                rate2 = (TextView) findViewById(R.id.rate2),
                rate3 = (TextView) findViewById(R.id.rate3),
                rate4 = (TextView) findViewById(R.id.rate4),
                rate5 = (TextView) findViewById(R.id.rate5)
        };

        builder = new AlertDialog.Builder(TopSaleActivity.this);

        try {
            StatisticsMaker.getTopSaleProducts(names, rates, builder);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
