package com.example.dbryuzgin.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.List;

public class Scan extends AppCompatActivity {

    public static DatabaseReference myRef;

    Button scan;
    Button goToMenu;
    Button goToCart;
    CameraView cameraView;
    public static String[] items;

    @Override
    protected void onResume(){
        super.onResume();
        cameraView.start();

    }

    @Override
    protected void onPause(){
        super.onPause();
        cameraView.stop();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        scan = (Button) findViewById(R.id.scan);
        goToMenu = (Button) findViewById(R.id.goToMenu);
        goToCart = (Button) findViewById(R.id.goToCart);
        cameraView = (CameraView) findViewById(R.id.cameraView);

        myRef = BarcodeScan.myRef;

        goToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Scan.this, Cart.class));
            }

        });

        goToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Scan.this, MainActivity.class));
            }

        });


        BarcodeScan barcodeScan = new BarcodeScan(cameraView, scan, Scan.this, "Scan");
        barcodeScan.scan();
        items = BarcodeScan.items;
    }

    public static void decreaseCount (String[]items,final DatabaseReference countRef){

        for (int i = 0; i < Product.counter; i++) {

            countRef.orderByChild("product_id").equalTo(items[i]).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                            String key = childDataSnapshot.getKey();
                            Storage storage = new Storage(childDataSnapshot.child("count").getValue().toString(), childDataSnapshot.child("product_id").getValue().toString());
                            storageHandlerDecreaser(storage, key, countRef);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });
        }
    }

    public static void increaseCount (String item, final DatabaseReference countRef, final String delta){

        countRef.orderByChild("product_id").equalTo(item).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        String key = childDataSnapshot.getKey();
                        Storage storage = new Storage(childDataSnapshot.child("count").getValue().toString(), childDataSnapshot.child("product_id").getValue().toString());
                        storageHandlerIncreaser(storage, key, countRef, delta);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public static void storageHandlerDecreaser (Storage storage, String key, DatabaseReference countRef){
        storage.setCount(String.valueOf(Integer.parseInt(storage.getCount()) - 1));
        countRef.child(key).setValue(storage);
    }

    public static void storageHandlerIncreaser (Storage storage, String key, DatabaseReference countRef, String delta){
        storage.setCount(String.valueOf(Integer.parseInt(storage.getCount()) + Integer.parseInt(delta)));
        countRef.child(key).setValue(storage);
        BarcodeScan.exportStorage = null;
        BarcodeScan.exportProvider = null;
        BarcodeScan.exportProduct = null;
    }
}
