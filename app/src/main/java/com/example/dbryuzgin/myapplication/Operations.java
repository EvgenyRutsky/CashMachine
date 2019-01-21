package com.example.dbryuzgin.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Operations extends AppCompatActivity {

    Button goToMenu, addProduct, editProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operations);

        goToMenu = (Button) findViewById(R.id.goToMenu);
        addProduct = (Button) findViewById(R.id.addProduct);
        editProduct = (Button) findViewById(R.id.editProduct);

        goToMenu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Operations.this, MainActivity.class));
            }

        });

        addProduct.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Operations.this, ProductAdding.class));
            }

        });

        editProduct.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Operations.this, ProductRedactor.class));
            }

        });
    }
}
