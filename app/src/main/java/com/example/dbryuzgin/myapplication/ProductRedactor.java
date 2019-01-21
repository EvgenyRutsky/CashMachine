package com.example.dbryuzgin.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wonderkiln.camerakit.CameraView;

public class ProductRedactor extends AppCompatActivity {

    Button scan, goToMenu, goToEditor;
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
        setContentView(R.layout.activity_product_redactor);

        scan = (Button) findViewById(R.id.scan);
        goToMenu = (Button) findViewById(R.id.goToMenu);
        goToEditor = (Button) findViewById(R.id.goToEditor);
        cameraView = (CameraView) findViewById(R.id.cameraView);


        BarcodeScan barcodeScan = new BarcodeScan(cameraView, scan, ProductRedactor.this, "ProductRedactor");
        barcodeScan.scan();

        goToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductRedactor.this, MainActivity.class));
            }

        });

        goToEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BarcodeScan.exportProduct != null) {
                    startActivity(new Intent(ProductRedactor.this, Editor.class));
                } else {
                    Toast.makeText(ProductRedactor.this, "Нет продуктов к редактированию", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

}
