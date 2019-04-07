package com.example.dbryuzgin.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import static android.support.v4.content.ContextCompat.startActivity;

public class BarcodeScan {

    CameraView cameraView;
    Button scan;
    Context context;
    String activity;
    public static DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Products");
    public static String idsList = "";
    public static Product exportProduct;
    public static Storage exportStorage;
    public static Provider exportProvider;
    public static String[] items = new String[5];


    public BarcodeScan(CameraView cameraView, Button scan, Context context, String activity) {
        this.cameraView = cameraView;
        this.scan = scan;
        this.context = context;
        this.activity = activity;
    }

    public void scan (){

        scan.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                cameraView.start();
                cameraView.captureImage();
            }
        });

        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                Bitmap bitmap = Bitmap.createBitmap(cameraKitImage.getBitmap());
                cameraView.stop();
                runDetector(bitmap);

            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });

    }


    public void runDetector (Bitmap bitmap) {

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(context.getApplicationContext())
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        if (!barcodeDetector.isOperational()) {
            Toast.makeText(context, "Повторите попытку", Toast.LENGTH_SHORT).show();
        } else {

            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            try {
                SparseArray<Barcode> barcodes = barcodeDetector.detect(frame);
                Barcode thisCode = barcodes.valueAt(0);

                searchBase(thisCode.rawValue);


            } catch (Exception e) {

                Toast.makeText(context, "QR код не распознан", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void searchBase (final String item){

        myRef.orderByChild("id").equalTo(item).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()){
                        Product product = new Product(childDataSnapshot.child("name").getValue().toString(), childDataSnapshot.child("price").getValue().toString(),
                                childDataSnapshot.child("provider_id").getValue().toString(), childDataSnapshot.child("id").getValue().toString());

                        if(activity.equals("Scan")) {
                            addProductToCart(product, item);
                        } else if (activity.equals("ProductRedactor")){
                            Toast.makeText(context, "Продукт " + product.getName() + " добавлен в редактор", Toast.LENGTH_SHORT).show();
                            editProduct(product);
                        } else if (activity.equals("ProductAdding")){
                            Toast.makeText(context, "Продукт " + product.getName() + " распознан", Toast.LENGTH_SHORT).show();
                            editProduct(product);
                        }

                    }

                } else {

                    if (activity.equals("ProductAdding")){
                        qrParser(item);
                        Toast.makeText(context, "Продукт " + exportProduct.getName() + " распознан", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Совпадение не найдено", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    private void addProductToCart(Product product, String item){
        Toast.makeText(context, product.getName() + " в корзине", Toast.LENGTH_SHORT).show();
        exportProduct = product;
        items[Product.counter - 1] = item;
    }

    private void editProduct(Product product){
        exportProduct = product;
    }

    private void qrParser(String item) {

        Product localProduct = new Product(item.split("\n")[0], item.split("\n")[1], item.split("\n")[2], item.split("\n")[3]);
        Provider localProvider = new Provider(item.split("\n")[2], item.split("\n")[4], item.split("\n")[5]);
        Storage localStorage = new Storage("0", item.split("\n")[3]);

        exportProduct = localProduct;
        exportProvider = localProvider;
        exportStorage = localStorage;

    }
}
