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

public class Editor extends AppCompatActivity {

    public static EditText productName, provider, price;
    Button save, cancel;
    public static DatabaseReference myRef, providerRef, productRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        save = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);
        productName = (EditText) findViewById(R.id.productName);
        provider = (EditText) findViewById(R.id.provider);
        price = (EditText) findViewById(R.id.price);

        myRef = FirebaseDatabase.getInstance().getReference();
        providerRef = myRef.child("Providers");
        productRef = myRef.child("Products");

        productName.setText(BarcodeScan.exportProduct.getName());
        price.setText(BarcodeScan.exportProduct.getPrice());

        providerRef.orderByChild("id").equalTo(BarcodeScan.exportProduct.getProvider_id()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()){
                        Provider providerName = new Provider(childDataSnapshot.child("id").getValue().toString(), childDataSnapshot.child("name").getValue().toString());
                        provider.setText(providerName.getName());
                    }

                } else {
                    Toast.makeText(Editor.this, "Совпадение не найдено", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (productName.getText().toString().equals("") || price.getText().toString().equals("") || provider.getText().toString().equals("")){
                    Toast.makeText(Editor.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                } else {
                    updateProductInfo();
                    startActivity(new Intent(Editor.this, ProductRedactor.class));
                }
            }

        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Editor.this, "Редактирование отменено", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Editor.this, ProductRedactor.class));
                cleaner();
            }

        });

    }

    public void updateProductInfo (){

        providerRef.orderByChild("id").equalTo(BarcodeScan.exportProduct.getProvider_id()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        String providerKey = childDataSnapshot.getKey();
                        Provider updatedProvider = new Provider(childDataSnapshot.child("id").getValue().toString(), childDataSnapshot.child("name").getValue().toString());
                        providerHandler(updatedProvider, providerKey, providerRef);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


        productRef.orderByChild("id").equalTo(BarcodeScan.exportProduct.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        String productKey = childDataSnapshot.getKey();
                        Product updatedProduct = new Product(childDataSnapshot.child("name").getValue().toString(), childDataSnapshot.child("price").getValue().toString(), childDataSnapshot.child("provider_id").getValue().toString(), childDataSnapshot.child("id").getValue().toString());
                        productHandler(updatedProduct, productKey, productRef);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    private void providerHandler (Provider updatedProvider, String key, DatabaseReference myRef){

        updatedProvider.setName(provider.getText().toString());
        myRef.child(key).setValue(updatedProvider);
    }

    private void productHandler (Product updatedProduct, String key, DatabaseReference myRef){

        updatedProduct.setName(productName.getText().toString());
        updatedProduct.setPrice(price.getText().toString());
        myRef.child(key).setValue(updatedProduct);
        Toast.makeText(Editor.this, "Продукт был успешно обновлен", Toast.LENGTH_SHORT).show();

        cleaner();
    }

    private void cleaner(){
        BarcodeScan.exportProduct = null;
    }

}
