package com.example.dbryuzgin.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;

public class Cart extends AppCompatActivity {

    private DatabaseReference myRef;
    public static String enteredMoney;
    public static String difference;

    TextView row1, row2, row3, row4, row5, toPay2;
    Button goToScan, pay;
    EditText money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        goToScan = (Button) findViewById(R.id.goToScan);
        pay = (Button) findViewById(R.id.pay);
        money = (EditText) findViewById(R.id.money);

        myRef = FirebaseDatabase.getInstance().getReference();
        final TextView[] rows = new TextView[]{
                row1 = (TextView) findViewById(R.id.row1),
                row2 = (TextView) findViewById(R.id.row2),
                row3 = (TextView) findViewById(R.id.row3),
                row4 = (TextView) findViewById(R.id.row4),
                row5 = (TextView) findViewById(R.id.row5)
        };

        toPay2 = (TextView) findViewById(R.id.toPay2);

        final String cartTotal = new DecimalFormat("#0.00").format(Product.total);

        toPay2.setText(cartTotal);

        goToScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Cart.this, Scan.class));

            }
        });

        for (int i = 0; i < Product.counter; i++) {

            rows[i].setText(Product.productInfos[i]);

        }

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Product.counter == 0){
                    Toast.makeText(Cart.this, "Нет продуктов к оплате", Toast.LENGTH_SHORT).show();
                } else {

                    if (money.getText().toString().equals("") || Double.parseDouble(cartTotal) > Double.parseDouble(money.getText().toString())) {

                        Toast.makeText(Cart.this, "Введите корректную внесенную сумму", Toast.LENGTH_SHORT).show();

                    } else {

                        Scan.decreaseCount(Scan.items, FirebaseDatabase.getInstance().getReference().child("Storage"));

                        enteredMoney = money.getText().toString();
                        double tmp = Double.parseDouble(money.getText().toString()) - Double.parseDouble(cartTotal);
                        difference = new DecimalFormat("#0.00").format(tmp);
                        startActivity(new Intent(Cart.this, successOrder.class));
                    }
                }
            }
        });
    }
}