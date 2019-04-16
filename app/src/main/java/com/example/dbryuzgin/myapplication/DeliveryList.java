package com.example.dbryuzgin.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeliveryList extends AppCompatActivity {

    private DatabaseReference billsRef;
    private List<String> ordersList = new ArrayList<String>();
    ListView orders;
    public static String selectedNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_list);

        orders = findViewById(R.id.list);
         billsRef = FirebaseDatabase.getInstance().getReference().child("Bills");

         billsRef.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if (dataSnapshot.exists()) {
                     for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                         Bill bill = new Bill ("",
                                 "",
                                 "",
                                 childDataSnapshot.child("number").getValue().toString(),
                                 "",
                                 "",
                                 "",
                                 "",
                                 childDataSnapshot.child("deliveryState").getValue().toString(),
                                 childDataSnapshot.child("fio").getValue().toString(),
                                 childDataSnapshot.child("address").getValue().toString(),
                                 childDataSnapshot.child("tel").getValue().toString());

                         if (bill.getDeliveryState().equals("1") || bill.getDeliveryState().equals("2") || bill.getDeliveryState().equals("3")){
                             ordersList.add(bill.getNumber());
                         }
                     }

                 }

                 String [] values = ordersList.toArray(new String[ordersList.size()]);
                 ArrayAdapter<String> adapter = new ArrayAdapter<String>(DeliveryList.this, android.R.layout.simple_list_item_1, values);
                 orders.setAdapter(adapter);

             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });

        orders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectedNumber = (String) parent.getItemAtPosition(position);

                startActivity(new Intent(DeliveryList.this, DeliveryDetails.class));
            }
        });
    }
}
