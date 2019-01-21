package com.example.dbryuzgin.myapplication;

import android.support.annotation.NonNull;
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
import java.util.Date;

public class StatisticsMaker {

    public static DatabaseReference billsRef, storageRef, productsRef;

    public static void statsMaker (final Date startDate, final Date endDate, final TextView buyersCount, final TextView productsCount, final TextView totalMoney){

        billsRef = FirebaseDatabase.getInstance().getReference().child("Bills");

        billsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int buyers = 0;
                double totalSaled = 0;
                String[] baseProductsCount;
                int arrayLength = 0;
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

                if(dataSnapshot.exists()){

                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()){

                        Date baseDate = null;
                        try {
                            baseDate = dateFormat.parse(childDataSnapshot.child("date").getValue().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if ( (baseDate.after(startDate) || baseDate.equals(startDate)) && baseDate.before(endDate)) {
                            buyers++;
                            totalSaled = totalSaled + Double.parseDouble(childDataSnapshot.child("total").getValue().toString());
                            baseProductsCount = childDataSnapshot.child("product_ids").getValue().toString().split(" ");
                            arrayLength = arrayLength + baseProductsCount.length;
                        }
                    }

                    String totalMoneyStr = new DecimalFormat("#0.00").format(totalSaled);

                    buyersCount.setText(String.valueOf(buyers));
                    productsCount.setText(String.valueOf(arrayLength));
                    totalMoney.setText(totalMoneyStr);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    public static void getLowCountProducts(){
//
//        storageRef = FirebaseDatabase.getInstance().getReference().child("Storage");
//
//        storageRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//
//                String ids = new String();
//                if(dataSnapshot.exists()){
//
//                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()){
//                        Storage storage = new Storage(childDataSnapshot.child("count").getValue().toString(), childDataSnapshot.child("product_id").getValue().toString());
//
//                        if(Integer.parseInt(storage.getCount()) <= 10){
//                            ids = ids + storage.getCount().toString() + " ";
//                        }
//                    }
//                }
//
//                String[] product_ids = ids.split(" ");
//                getLowCountProductsHandler(product_ids);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

//    public static void getLowCountProductsHandler (String[] ids){
//
//        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
//        final String[] productNames = new String[ids.length];
//
//        for (int i = 0; i < ids.length; i++){
//
//            final int finalI = i;
//            productsRef.orderByChild("id").equalTo(ids[i]).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                    if(dataSnapshot.exists()){
//
//                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()){
//                            Product product = new Product(childDataSnapshot.child("name").getValue().toString(), childDataSnapshot.child("price").getValue().toString(), childDataSnapshot.child("provider_id").getValue().toString(), childDataSnapshot.child("id").getValue().toString());
//                            productNames[finalI] = product.getName().toString();
//
//                        }
//                    }
//
//                }
//
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//
//        }
//    }

}
