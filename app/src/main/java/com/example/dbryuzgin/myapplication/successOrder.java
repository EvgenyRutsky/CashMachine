package com.example.dbryuzgin.myapplication;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class successOrder extends AppCompatActivity {

    TextView difference, worker, date, time, orderNumber, toPaySuccess, moneySuccess, number, type;
    Button finishPayment;
    Button orderDelivery;
    static String cartTotal = new DecimalFormat("#0.00").format(Product.total);
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef, billsRef;
    public static String billNumber;
    private String userEmail = "";
    public static Bill tempBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_order);

        difference = (TextView) findViewById(R.id.difference);
        worker = (TextView) findViewById(R.id.worker);
        date = (TextView) findViewById(R.id.date);
        time = (TextView) findViewById(R.id.time);
        orderNumber = (TextView) findViewById(R.id.orderNumber);
        toPaySuccess = (TextView) findViewById(R.id.toPaySuccess);
        moneySuccess = (TextView) findViewById(R.id.moneySuccess);
        number = (TextView) findViewById(R.id.orderNumber);
        type = (TextView) findViewById(R.id.type);
        finishPayment = (Button) findViewById(R.id.finishPayment);
        orderDelivery = (Button) findViewById(R.id.delivery);
        final Date currentDate = new Date();
        final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        final DateFormat numberFormat = new SimpleDateFormat("ssmmHHyyyyMMdd");

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    userEmail = user.getEmail();
                    worker.setText("Продавец: " + userEmail);
                }
            }
        };

        billNumber = numberFormat.format(currentDate);

        number.setText("Номер чека: " + billNumber);

        toPaySuccess.setText("К оплате: " + new DecimalFormat("#0.00").format(Product.total));

        if (Cart.paymentType.equals("cash")) {
            moneySuccess.setText("Внесено: " + Cart.enteredMoney);
            difference.setText("Сдача: " + Cart.difference);
            type.setText("Оплата произведена наличными");
        } else if (Cart.paymentType.equals("card")){
            moneySuccess.setText("Внесено: " + new DecimalFormat("#0.00").format(Product.total));
            difference.setText("");
            type.setText("Оплата произведена картой " + CardPaymentActivity.shortCardType + " **** " + CardPaymentActivity.shortCardNumber);
        }

        date.setText("Дата покупки: " + dateFormat.format(currentDate));
        time.setText("Время покупки: " + timeFormat.format(currentDate));



        finishPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                captureScreen(numberFormat, currentDate);

                Bill bill = new Bill(dateFormat.format(currentDate), Cart.difference, Cart.enteredMoney, billNumber, BarcodeScan.idsList, timeFormat.format(currentDate),
                        new DecimalFormat("#0.00").format(Product.total), userEmail, "0","", "", "");

                myRef = FirebaseDatabase.getInstance().getReference();
                billsRef = myRef.child("Bills");
                billsRef.push().setValue(bill);

                BarcodeScan.idsList = "";

                Intent intent = new Intent(successOrder.this, EmailSender.class);
                startActivity(intent);

            }
        });

        orderDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //captureScreen(numberFormat, currentDate); скриншот чека - пересмотреть потом

                 tempBill = new Bill(dateFormat.format(currentDate), Cart.difference, Cart.enteredMoney, billNumber, BarcodeScan.idsList, timeFormat.format(currentDate),
                        new DecimalFormat("#0.00").format(Product.total), userEmail, "0", "", "", "");

//                myRef = FirebaseDatabase.getInstance().getReference();
//                billsRef = myRef.child("Bills");
//                billsRef.push().setValue(tempBill);

                BarcodeScan.idsList = "";

                Intent intent = new Intent(successOrder.this, DeliveryData.class);
                startActivity(intent);

            }
        });


        //ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},00);

    }

    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void captureScreen(DateFormat dateFormat, Date date) {
        View v = getWindow().getDecorView().getRootView();
        v.setDrawingCacheEnabled(true);
        Bitmap bmp = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        try {
            FileOutputStream fos = new FileOutputStream(new File(getExternalCacheDir(), dateFormat.format(date) + ".jpg"));
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
