package com.example.dbryuzgin.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;

import static com.example.dbryuzgin.myapplication.successOrder.billNumber;


public class DeliveryData extends AppCompatActivity /*implements OnMapReadyCallback*/ {

    TextView orderNumber, clientName, address, tel;
    Button proceed;
    Bill bill = successOrder.tempBill;
    private DatabaseReference myRef;
    private MapView mapView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        MapKitFactory.setApiKey("6b28ce94-1e9d-47a4-b7b2-6c3bdba65f5a");
        MapKitFactory.initialize(this);

        setContentView(R.layout.activity_delivery_data);

        super.onCreate(savedInstanceState);

        mapView = (MapView)findViewById(R.id.mapview);

        orderNumber = findViewById(R.id.orderNumber);
        clientName = findViewById(R.id.clientName);
        address = findViewById(R.id.address);
        tel = findViewById(R.id.tel);
        proceed = findViewById(R.id.proceed);

        orderNumber.setText(billNumber);

        mapView.getMap().move(
                new CameraPosition(new Point(53.898506, 30.333742), 11.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // добваить сеттеры для полей
                bill.setFio(clientName.getText().toString());
                bill.setAddress(address.getText().toString());
                bill.setTel(tel.getText().toString());
                bill.setDeliveryState("1");
                myRef = FirebaseDatabase.getInstance().getReference().child("Bills");
                myRef.push().setValue(bill);

                BarcodeScan.idsList = "";

                Intent intent = new Intent(DeliveryData.this, EmailSender.class);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onStop() {
        // Вызов onStop нужно передавать инстансам MapView и MapKit.
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        // Вызов onStart нужно передавать инстансам MapView и MapKit.
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }



}
