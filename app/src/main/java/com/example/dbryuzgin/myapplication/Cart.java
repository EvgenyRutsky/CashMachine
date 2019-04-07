package com.example.dbryuzgin.myapplication;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;

public class Cart extends AppCompatActivity {

    public static String enteredMoney;
    public static String difference;
    public static String paymentType;

    TextView row1, row2, row3, row4, row5, toPay2;
    Button goToScan, pay, cardPayment;
    EditText money;
    NumberPicker numberPicker1, numberPicker2, numberPicker3, numberPicker4, numberPicker5;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        goToScan = (Button) findViewById(R.id.goToScan);
        pay = (Button) findViewById(R.id.pay);
        money = (EditText) findViewById(R.id.money);
        cardPayment = (Button) findViewById(R.id.cardPayment);
        paymentType = "cash";
        builder = new AlertDialog.Builder(Cart.this);

        final TextView[] rows = new TextView[]{
                row1 = (TextView) findViewById(R.id.row1),
                row2 = (TextView) findViewById(R.id.row2),
                row3 = (TextView) findViewById(R.id.row3),
                row4 = (TextView) findViewById(R.id.row4),
                row5 = (TextView) findViewById(R.id.row5)
        };

        final NumberPicker[] numberPickers = new NumberPicker[]{
                numberPicker1 = (NumberPicker) findViewById(R.id.numberPicker1),
                numberPicker2 = (NumberPicker) findViewById(R.id.numberPicker2),
                numberPicker3 = (NumberPicker) findViewById(R.id.numberPicker3),
                numberPicker4 = (NumberPicker) findViewById(R.id.numberPicker4),
                numberPicker5 = (NumberPicker) findViewById(R.id.numberPicker5)
        };

        for (int i = 0; i < numberPickers.length; i++){
            numberPickers[i].setMinValue(1);
            numberPickers[i].setMaxValue(100);
            numberPickers[i].setWrapSelectorWheel(false);
            numberPickers[i].setVisibility(View.INVISIBLE);
            numberPickers[i].setFocusable(false);
        }

        toPay2 = (TextView) findViewById(R.id.toPay2);

        toPay2.setText("0.0");

        goToScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Cart.this, Scan.class));

            }
        });

        for (int i = 0; i < Product.counter; i++) {

            rows[i].setText(Product.productInfos[i]);
        }

        for (int i = 0; i < rows.length; i++) {

            if (rows[i].getText().toString() == ""){
                rows[i].setLongClickable(false);
            }
        }

        for (int i = 0; i < rows.length; i++){
            final int finalI = i;
            rows[i].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    builder.setTitle("Удаление товара")
                            .setMessage("Желаете убрать выбранный товар из корзины?")
                            .setPositiveButton("Нет", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("Да", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    int k = 0, counter = 0;
                                    for (int j = 0; j < rows.length; j++){
                                        if (rows[j].getText().toString() == rows[finalI].getText().toString()){
                                            counter++;
                                            while (k < rows.length - 1){
                                                rows[k].setText(rows[k + 1].getText().toString());
                                                numberPickers[k].setValue(numberPickers[k + 1].getValue());
                                                k++;
                                            }
                                            rows[rows.length - counter].setText("");
                                            numberPickers[rows.length - counter].setVisibility(View.INVISIBLE);
                                            k = 0;
                                        }
                                    }
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    return true;
                }
            });
        }


        cartHandler(numberPickers, rows, toPay2);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Product.counter == 0){
                    Toast.makeText(Cart.this, "Нет продуктов к оплате", Toast.LENGTH_SHORT).show();
                } else {

                    if (money.getText().toString().equals("") || Double.parseDouble(toPay2.getText().toString()) > Double.parseDouble(money.getText().toString())) {

                        Toast.makeText(Cart.this, "Введите корректную внесенную сумму", Toast.LENGTH_SHORT).show();

                    } else {

                        int deltasCounter = 0;
                        for (int i = 0; i < numberPickers.length; i++){
                            if (!rows[i].getText().toString().equals("")) {
                               deltasCounter++;
                            }
                        }

                        String deltas[] = new String[deltasCounter];

                        for (int i = 0; i < numberPickers.length; i++){
                            if (!rows[i].getText().toString().equals("")) {
                                 deltas[i] = String.valueOf(numberPickers[i].getValue());
                            }
                        }

                        for (int i = 0; i < numberPickers.length; i++){
                            if (!rows[i].getText().toString().equals("")) {

                                Scan.decreaseCount(Scan.items, FirebaseDatabase.getInstance().getReference().child("Storage"), deltas);
                                String str = new String(new char[numberPickers[i].getValue()]).replace("\0", BarcodeScan.items[i] + " ");
                                BarcodeScan.idsList += str;

                            }
                        }

                        enteredMoney = money.getText().toString();
                        double tmp = Double.parseDouble(money.getText().toString()) - Double.parseDouble(toPay2.getText().toString());
                        difference = new DecimalFormat("#0.00").format(tmp);
                        Product.total = Double.parseDouble(toPay2.getText().toString());
                        startActivity(new Intent(Cart.this, successOrder.class));
                    }
                }
            }
        });


        cardPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Product.counter == 0){
                    Toast.makeText(Cart.this, "Нет продуктов к оплате", Toast.LENGTH_SHORT).show();
                } else {
                    enteredMoney = toPay2.getText().toString();
                    Product.total = Double.parseDouble(toPay2.getText().toString());
                    startActivity(new Intent(Cart.this, CardPaymentActivity.class));
                    }
                }

        });

    }

    public static void cartHandler (NumberPicker[] numberPickers, TextView[] rows, final TextView toPay2){

        for (int i = 0; i < numberPickers.length; i++){
            if (!rows[i].getText().toString().equals("")) {
                numberPickers[i].setVisibility(View.VISIBLE);

                String str;
                str = rows[i].getText().toString().substring(rows[i].getText().toString().length() - 3);
                toPay2.setText(String.valueOf(Double.parseDouble(toPay2.getText().toString()) + Double.parseDouble(str)));

                final String finalStr = str;

                numberPickers[i].setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                        toPay2.setText(String.valueOf(new DecimalFormat("##.##").format(Double.parseDouble(toPay2.getText().toString()) + Double.parseDouble(finalStr)*(newVal - oldVal))));

                    }
                });
            }
        }
    }
}