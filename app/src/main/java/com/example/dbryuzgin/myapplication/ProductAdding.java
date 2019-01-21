package com.example.dbryuzgin.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wonderkiln.camerakit.CameraView;

public class ProductAdding extends AppCompatActivity {

    Button scan, goToMenu, goToAdder;
    CameraView cameraView;

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.stop();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_adding);

        scan = (Button) findViewById(R.id.scan);
        goToMenu = (Button) findViewById(R.id.goToMenu);
        goToAdder = (Button) findViewById(R.id.goToAdder);
        cameraView = (CameraView) findViewById(R.id.cameraView);

        BarcodeScan barcodeScan = new BarcodeScan(cameraView, scan, ProductAdding.this, "ProductAdding");
        barcodeScan.scan();

        goToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductAdding.this, MainActivity.class));
            }

        });

        goToAdder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BarcodeScan.exportProduct != null) {
                    startActivity(new Intent(ProductAdding.this, ProductAdder.class));
                } else {
                    Toast.makeText(ProductAdding.this, "Нет продуктов к добавлению", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
}
