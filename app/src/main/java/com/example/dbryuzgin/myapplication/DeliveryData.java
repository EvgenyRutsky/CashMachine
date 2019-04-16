package com.example.dbryuzgin.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;

import static com.example.dbryuzgin.myapplication.successOrder.billNumber;
// import com.google.android.gms.maps.GoogleMap;


public class DeliveryData extends AppCompatActivity /*implements OnMapReadyCallback*/ {

    TextView orderNumber, clientName, address, tel;
    Button proceed;
    Bill bill = successOrder.tempBill;
    private DatabaseReference myRef;
//    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_data);

        orderNumber = findViewById(R.id.orderNumber);
        clientName = findViewById(R.id.clientName);
        address = findViewById(R.id.address);
        tel = findViewById(R.id.tel);
        proceed = findViewById(R.id.proceed);

        orderNumber.setText(billNumber);

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


//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.mapView);
//        if (mapFragment != null) {
//            mapFragment.getMapAsync(this);
//        }
    }



//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        this.googleMap = googleMap;
//    }
}
