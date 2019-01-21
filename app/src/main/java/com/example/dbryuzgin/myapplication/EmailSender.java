package com.example.dbryuzgin.myapplication;


import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.core.Context;

import java.io.File;
import java.text.DecimalFormat;

public class EmailSender extends AppCompatActivity {

    EditText buyerEmail;
    Button skip, send;
    ImageView imgview;
    private String receiverEmail, subject, message;
    String[] products = new String[Product.counter];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_sender);

        buyerEmail = (EditText) findViewById(R.id.buyerEmail);
        skip = (Button) findViewById(R.id.skip);
        send = (Button) findViewById(R.id.send);
        imgview = (ImageView) findViewById(R.id.imgview);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cleanData();
                Intent intent = new Intent(EmailSender.this, Scan.class);
                startActivity(intent);

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = "";

                for (int i = 0; i < Product.counter; i++) {

                    message = new StringBuilder().append(message).append(Product.productInfos[i]).append("\n\n").toString();

                }

                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{buyerEmail.getText().toString()});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Покупка № " + successOrder.billNumber);
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Добрый день!\n\n Благодарим вас за покупку в нашем магазине! \n\n" + message + "\n " +
                        "Стоимость покупок: " + successOrder.cartTotal + "\n Внесено: " + Cart.enteredMoney + "\n Сдача: " + Cart.difference +
                        "\n\n\n За дополнительной информацией обратитесь по телефону: +123451234567" + "\n\n Хорошего дня!");
                emailIntent.setType("application/octet-stream");
                startActivity(Intent.createChooser(emailIntent, "Выберите Email клиент:"));
                cleanData();
            }
        });



    }
    private void cleanData() {
        for (int i = 0; i< Product.counter; i++){
            Product.productInfos[i]="";
            Scan.items[i]="";
        }

        Product.total = 0;
        Product.counter = 0;
    }
}
