package com.example.dbryuzgin.myapplication;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pro100svitlo.creditCardNfcReader.CardNfcAsyncTask;
import com.pro100svitlo.creditCardNfcReader.utils.CardNfcUtils;

import java.text.DecimalFormat;

public class CardPaymentActivity extends AppCompatActivity implements CardNfcAsyncTask.CardNfcInterface{

    private CardNfcAsyncTask mCardNfcAsyncTask;
    private NfcAdapter mNfcAdapter;
    private AlertDialog mTurnNfcDialog;
    private ProgressDialog mProgressDialog;
    private boolean mIsScanNow;
    private boolean mIntentFromCreate;
    private CardNfcUtils mCardNfcUtils;

    public static String shortCardType;
    public static String shortCardNumber;

    TextView cardNumber, cardExpirationDate, totalToPay;
    Button finishPayment;
    ImageView visaIcon, mastercardIcon;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_payment);

        builder = new AlertDialog.Builder(CardPaymentActivity.this);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null){
            builder.setTitle("Невозможно совершить операцию")
                    .setMessage("На данном девайсе нет поддержки NFC")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startActivity(new Intent(CardPaymentActivity.this, Cart.class));
                        }
                    })
                    .show();
        } else {

            finishPayment = (Button) findViewById(R.id.finishPayment);
            cardNumber = (TextView) findViewById(R.id.cardNumber);
            cardExpirationDate = (TextView) findViewById(R.id.cardExpirationDate);
            totalToPay = (TextView) findViewById(R.id.totalToPay);
            visaIcon = (ImageView) findViewById(R.id.visa);
            mastercardIcon = (ImageView) findViewById(R.id.mastercard);

            totalToPay.setText(new DecimalFormat("#0.00").format(Product.total));
            visaIcon.setVisibility(View.INVISIBLE);
            mastercardIcon.setVisibility(View.INVISIBLE);

            mCardNfcUtils = new CardNfcUtils(this);
            createProgressDialog();
            mIntentFromCreate = true;
            onNewIntent(getIntent());
        }

        finishPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cart.paymentType = "card";
                Cart.difference = "0.00";
                startActivity(new Intent(CardPaymentActivity.this, successOrder.class));

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIntentFromCreate = false;
        if (mNfcAdapter != null && !mNfcAdapter.isEnabled()){
            showTurnOnNfcDialog();
        } else if (mNfcAdapter != null){
            mCardNfcUtils.enableDispatch();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mNfcAdapter != null) {
            mCardNfcUtils.disableDispatch();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mNfcAdapter != null && mNfcAdapter.isEnabled()) {
            mCardNfcAsyncTask = new CardNfcAsyncTask.Builder(this, intent, mIntentFromCreate)
                    .build();
        }
    }

    @Override
    public void startNfcReadCard() {
        mIsScanNow = true;
        mProgressDialog.show();
    }

    @Override
    public void cardIsReadyToRead() {
        String card = mCardNfcAsyncTask.getCardNumber();
        card = getPrettyCardNumber(card);
        String expiredDate = mCardNfcAsyncTask.getCardExpireDate();
        String cardType = mCardNfcAsyncTask.getCardType();
        cardNumber.setText(card);
        cardExpirationDate.setText(expiredDate);
        parseCardType(cardType);
        shortCardNumber = card.substring(card.length() - 4);
        if (cardType.equals("NAB_VISA")) {
            shortCardType = "VISA";
        } else if (cardType.equals("MASTER_CARD")){
            shortCardType = "MASTER CARD";
        }
    }

    @Override
    public void doNotMoveCardSoFast() {

    }

    @Override
    public void unknownEmvCard() {

    }

    @Override
    public void cardWithLockedNfc() {

    }

    @Override
    public void finishNfcReadCard() {
        mProgressDialog.dismiss();
        mCardNfcAsyncTask = null;
        mIsScanNow = false;
    }

    private void createProgressDialog(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Сканирование");
        mProgressDialog.setMessage("Пожалуйста, не относите карту...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
    }

    private void showTurnOnNfcDialog(){
        if (mTurnNfcDialog == null) {
            String title = "Внимание!";
            String mess = "Для продолжения операции необходимо включить NFC на устройстве";
            String pos = "Включить";
            String neg = "Отмена";
            mTurnNfcDialog = new AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage(mess)
                    .setPositiveButton(pos, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (android.os.Build.VERSION.SDK_INT >= 16) {
                                startActivity(new Intent(android.provider.Settings.ACTION_NFC_SETTINGS));
                            } else {
                                startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                            }
                        }
                    })
                    .setNegativeButton(neg, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            onBackPressed();
                        }
                    }).create();
        }
        mTurnNfcDialog.show();
    }

    private String getPrettyCardNumber(String card){
        String div = " - ";
        return  card.substring(0,4) + div + card.substring(4,8) + div + card.substring(8,12)
                +div + card.substring(12,16);
    }

    private void parseCardType(String cardType){
        if (cardType.equals("NAB_VISA")){
            visaIcon.setVisibility(View.VISIBLE);
        } else if (cardType.equals("MASTER_CARD")){
            mastercardIcon.setVisibility(View.VISIBLE);
        }
    }
}

