package com.example.dbryuzgin.myapplication;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.constraint.Constraints.TAG;

public class StatisticsMakerForTopProducts {

    public static DatabaseReference billsRef;
    public static DatabaseReference productsRef;

    public static void getTopSaleProducts (final TextView[] productViews, final TextView[] rateViews, final AlertDialog.Builder builder) {

        billsRef = FirebaseDatabase.getInstance().getReference().child("Bills");

        billsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String allBills = new String();

                if (dataSnapshot.exists()) {

                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        Bill bill = new Bill(" ", " ", " ", " ", childDataSnapshot.child("product_ids").getValue().toString(), " ", " ", " ", " ", " ", " ", " ");
                        allBills = allBills + bill.getProduct_ids();
                    }
                }

                if (allBills.equals("")){
                    builder.setTitle("Информация")
                            .setMessage("В данный момент нет проданных товаров")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    countSalesAndDetermineProducts(productViews, rateViews, allBills);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public static void countSalesAndDetermineProducts (final TextView[] productViews, final TextView[] rateViews, String allBills){

        final ArrayList<ProductsStatsHandler> productsStatsHandlers = new ArrayList<>();

        String[] s1 = allBills.split(" ");

        Map<Integer, Integer> test = new HashMap<>();

        for (int i = 0; i < s1.length; i++) {
            int tempProductId = Integer.parseInt(s1[i]);

            if (!test.containsKey(tempProductId)) {
                test.put(tempProductId, 1);
            } else {
                test.put(tempProductId, test.get(tempProductId) + 1);
            }
        }

        List<Integer> salesCountList = new ArrayList<>(test.values());

        for (int i = 0; i < salesCountList.size(); i++){
            ProductsStatsHandler productsStatsHandler = new ProductsStatsHandler(i + 1, "1", salesCountList.get(i), 0);
            productsStatsHandlers.add(productsStatsHandler);
        }

        Collections.sort(productsStatsHandlers);

        int[] orderedProductIDsBySalesCount = new int[productsStatsHandlers.size()];

        for (int i = 0; i <productsStatsHandlers.size(); i++){
            orderedProductIDsBySalesCount[i] = productsStatsHandlers.get(i).getId();
        }

        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");








//                 _______         _______     _______   ________
//        |     |  |               |      |    |     |   |
//        |     |  |               |      |    |     |   |
//        |_____|  |______         |______|    |_____|   |_______
//        |     |  |               |           |     |   |       |
//        |     |  |               |           |     |   |       |
//        |     |  |______         |           |     |   |_______|    |_|














//        for (int i = 0; i < orderedProductIDsBySalesCount.length; i++){
//
//            final int finalI = i;
//            productsRef.orderByChild("id").equalTo(orderedProductIDsBySalesCount[i]).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                    if(dataSnapshot.exists()){
//
//                        String name = "";
//                        double price = 0;
//
//                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()){
//                            Product product = new Product(childDataSnapshot.child("name").getValue().toString(), childDataSnapshot.child("price").getValue().toString(),
//                                    childDataSnapshot.child("provider_id").getValue().toString(), childDataSnapshot.child("id").getValue().toString());
//                            name = product.getName();
//                            price = Double.parseDouble(product.getPrice());
//                            productsStatsHandlers.get(finalI).setName(name);
//                            //handler (productsStatsHandlers, finalI, name, price);
//                        }
//
//                        productsCleaner();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//            setDataToView(productsStatsHandlers, productViews, rateViews);
//        }


        setDataToView(productsStatsHandlers, productViews, rateViews);

    }


    public static void handler(ArrayList<ProductsStatsHandler> productsStatsHandlers, int index, String name, double price){
        productsStatsHandlers.get(index).setName(name);
        productsStatsHandlers.get(index).setTotal(productsStatsHandlers.get(index).getSalesCount()*price);
    }

    public static void setDataToView (ArrayList<ProductsStatsHandler> productsStatsHandlers,  TextView[] productViews, TextView[] rateViews) {

        for (int i = 0; i < 4; i++){

            //productViews[i].setText(productsStatsHandlers.get(i).getName());
            rateViews[i].setText(String.valueOf(productsStatsHandlers.get(i).getSalesCount()));

        }

//        !!! удалить нахуй потом ↓


        productViews[0].setText("Молоко");
        productViews[1].setText("Хлеб");
        productViews[2].setText("Хрен");
        productViews[3].setText("Кефир");


    }

    public static void productsCleaner(){
        for (int i=0; i < Product.counter; i++ ){
            Product.productInfos[i] = "";
        }
        Product.total = 0;
        Product.counter = 0;
    }

}
