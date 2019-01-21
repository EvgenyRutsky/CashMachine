package com.example.dbryuzgin.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class ProductAdder extends AppCompatActivity {

    public static EditText productName, provider, price, count;
    Button save, cancel;


    public static DatabaseReference myRef, providerRef, productRef, storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_adder);

        save = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);
        productName = (EditText) findViewById(R.id.productName);
        provider = (EditText) findViewById(R.id.provider);
        price = (EditText) findViewById(R.id.price);
        count = (EditText) findViewById(R.id.count);

        productName.setText(BarcodeScan.exportProduct.getName());
        price.setText(BarcodeScan.exportProduct.getPrice());

        myRef = FirebaseDatabase.getInstance().getReference();
        providerRef = myRef.child("Providers");
        productRef = myRef.child("Products");
        storageRef = myRef.child("Storage");




        providerRef.orderByChild("id").equalTo(BarcodeScan.exportProduct.getProvider_id()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()){
                        Provider providerName = new Provider(childDataSnapshot.child("id").getValue().toString(), childDataSnapshot.child("name").getValue().toString());
                        provider.setText(providerName.getName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        productName.setEnabled(false);
        provider.setEnabled(false);
        price.setEnabled(false);

        if (BarcodeScan.exportProvider != null && BarcodeScan.exportStorage != null) {

            provider.setText(BarcodeScan.exportProvider.getName());
            count.setText(BarcodeScan.exportStorage.getCount());
        } else {
            Toast.makeText(ProductAdder.this, "Проблема", Toast.LENGTH_SHORT).show();
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (count.getText().toString().equals("")){
                    Toast.makeText(ProductAdder.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                } else if (BarcodeScan.exportProvider != null && BarcodeScan.exportStorage != null) {
                        newProduct();
                        Toast.makeText(ProductAdder.this, "Продукт успешно добавлен", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ProductAdder.this, ProductAdding.class));
                } else {
                    updateStorage();
                    Toast.makeText(ProductAdder.this, "Количество продукта обновлено", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ProductAdder.this, ProductAdding.class));
                }
            }

        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProductAdder.this, "Добавление отменено", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ProductAdder.this, ProductAdding.class));
                cleaner();
            }

        });

    }

    public void updateStorage () {

        storageRef.orderByChild("product_id").equalTo(BarcodeScan.exportProduct.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        Storage updatedStorage = new Storage(childDataSnapshot.child("count").getValue().toString(), childDataSnapshot.child("product_id").getValue().toString());
                        Scan.increaseCount(updatedStorage.getProduct_id(), storageRef, count.getText().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }


    Boolean isProviderExists = false;
    Boolean isStorageExists = false;
    Boolean isProductExists = false;

    public void newProduct(){


        providerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                        Provider testProvider = new Provider (childDataSnapshot.child("id").getValue().toString(), childDataSnapshot.child("name").getValue().toString());

                        if(testProvider.getId().equals(BarcodeScan.exportProvider.getId())){

                            isProviderExists = true;
                        }
                    }
                    if (!isProviderExists){
                        pushProvider();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        storageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {


                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                        Storage testStorage = new Storage (childDataSnapshot.child("count").getValue().toString(), childDataSnapshot.child("product_id").getValue().toString());

                        if(testStorage.getProduct_id().equals(BarcodeScan.exportStorage.getProduct_id())){
                            isStorageExists = true;
                        }
                    }
                    if (!isStorageExists){
                        pushStorage();
                        Scan.increaseCount(BarcodeScan.exportStorage.getProduct_id(), storageRef, count.getText().toString());

                    } else {
                        Scan.increaseCount(BarcodeScan.exportStorage.getProduct_id(), storageRef, count.getText().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {


                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                        Product testProduct = new Product (childDataSnapshot.child("name").getValue().toString(), childDataSnapshot.child("price").getValue().toString(), childDataSnapshot.child("provider_id").getValue().toString(), childDataSnapshot.child("id").getValue().toString());

                        if(testProduct.getId().equals(BarcodeScan.exportProduct.getId())){
                            isProductExists = true;
                        }
                    }
                    if (!isProductExists){
                        pushProduct();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        updateStorage();

    }

    private void pushProduct(){
        productRef.push().setValue(BarcodeScan.exportProduct);

    }

    private void pushProvider(){
        providerRef.push().setValue(BarcodeScan.exportProvider);

    }

    private void pushStorage(){
        storageRef.push().setValue(BarcodeScan.exportStorage);

    }

    private void cleaner(){
        BarcodeScan.exportProduct = null;
        BarcodeScan.exportProvider = null;
        BarcodeScan.exportStorage = null;
    }
}
