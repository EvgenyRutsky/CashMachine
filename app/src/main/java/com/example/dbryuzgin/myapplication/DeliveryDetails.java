package com.example.dbryuzgin.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeliveryDetails extends AppCompatActivity {

    TextView orderNumber, clientName, address, tel, deliveryState;
    Button complete, cancel;
    String selectedNumber = DeliveryList.selectedNumber;
    String billKey;
    private DatabaseReference billsRef = FirebaseDatabase.getInstance().getReference().child("Bills");

    Bill selectedBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_details);

        orderNumber = findViewById(R.id.orderNumber1);
        clientName = findViewById(R.id.clientName1);
        address = findViewById(R.id.address1);
        tel = findViewById(R.id.tel1);
        cancel = findViewById(R.id.cancel);
        complete = findViewById(R.id.complete);
        deliveryState = findViewById(R.id.deliveryState1);

        orderNumber.setText(selectedNumber);

        billsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                        Bill bill = new Bill(childDataSnapshot.child("date").getValue().toString(),
                                childDataSnapshot.child("difference").getValue().toString(),
                                childDataSnapshot.child("money").getValue().toString(),
                                childDataSnapshot.child("number").getValue().toString(),
                                childDataSnapshot.child("product_ids").getValue().toString(),
                                childDataSnapshot.child("time").getValue().toString(),
                                childDataSnapshot.child("total").getValue().toString(),
                                childDataSnapshot.child("user_email").getValue().toString(),
                                childDataSnapshot.child("deliveryState").getValue().toString(),
                                childDataSnapshot.child("fio").getValue().toString(),
                                childDataSnapshot.child("address").getValue().toString(),
                                childDataSnapshot.child("tel").getValue().toString());

                        if (bill.getNumber().equals(selectedNumber)){
                            selectedBill = bill;
                            fillTheScreen();
                            billKey = childDataSnapshot.getKey();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedBill.setDeliveryState("2");
                selectedBillHandler(selectedBill, billKey, billsRef);

                Intent intent = new Intent(DeliveryDetails.this, DeliveryDetails.class);
                startActivity(intent);


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedBill.setDeliveryState("3");
                selectedBillHandler(selectedBill, billKey, billsRef);

                Intent intent = new Intent(DeliveryDetails.this, DeliveryDetails.class);
                startActivity(intent);

            }
        });




    }

    private void selectedBillHandler (Bill updatedBill, String billKey, DatabaseReference myRef){

        myRef.child(billKey).setValue(updatedBill);
    }

    private void fillTheScreen () {

            clientName.setText(selectedBill.getFio());
            address.setText(selectedBill.getAddress());
            tel.setText(selectedBill.getTel());

            if (selectedBill.getDeliveryState().equals("1")) {
                deliveryState.setText("В пути");
                deliveryState.setTextColor(0xffFFA500);
            } else if (selectedBill.getDeliveryState().equals("2")) {
                deliveryState.setText("Доставлен");
                deliveryState.setTextColor(0xff008000);
            } else if (selectedBill.getDeliveryState().equals("3")) {
                deliveryState.setText("Отменён");
                deliveryState.setTextColor(0xffFF0000);
            }

    }
}
