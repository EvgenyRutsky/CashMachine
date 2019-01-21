package com.example.dbryuzgin.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class LowCountProductsActivity extends AppCompatActivity {

    TextView name1, name2, name3, name4, name5, name6, name7;
    TextView count1, count2, count3, count4, count5, count6, count7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_low_count_products);

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
                name5 = (TextView) findViewById(R.id.name5),
                name6 = (TextView) findViewById(R.id.name6),
                name7 = (TextView) findViewById(R.id.name7)
        };

        TextView[] counts = new TextView[]{
                count1 = (TextView) findViewById(R.id.count1),
                count2 = (TextView) findViewById(R.id.count2),
                count3 = (TextView) findViewById(R.id.count3),
                count4 = (TextView) findViewById(R.id.count4),
                count5 = (TextView) findViewById(R.id.count5),
                count6 = (TextView) findViewById(R.id.count6),
                count7 = (TextView) findViewById(R.id.count7)
        };

    }
}
