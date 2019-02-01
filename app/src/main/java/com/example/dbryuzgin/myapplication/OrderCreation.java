package com.example.dbryuzgin.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderCreation extends AppCompatActivity {

    private DatabaseReference providersRef;
    private List<String> providersList = new ArrayList<String>();
    private List<String> phonesList = new ArrayList<String>();

    ListView providers, phones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_creation);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        providers = (ListView) findViewById(R.id.left);
        phones = (ListView) findViewById(R.id.right);

        providersRef = FirebaseDatabase.getInstance().getReference().child("Providers");

        providersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                        Provider provider = new Provider(childDataSnapshot.child("id").getValue().toString(), childDataSnapshot.child("name").getValue().toString(), childDataSnapshot.child("phone").getValue().toString());
                        providersList.add(provider.getName());
                        phonesList.add(provider.getPhone());

                    }
                }

                String [] values1 = providersList.toArray(new String[providersList.size()]);
                String [] values2 = phonesList.toArray(new String[phonesList.size()]);

                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(OrderCreation.this, android.R.layout.simple_list_item_1, values1);
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(OrderCreation.this, android.R.layout.simple_list_item_1, values2);
                providers.setAdapter(adapter1);
                phones.setAdapter(adapter2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        phones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedNumber = (String) parent.getItemAtPosition(position);

                Toast.makeText(OrderCreation.this, selectedNumber, Toast.LENGTH_SHORT).show();
                makeCall(selectedNumber);
            }
        });


    }

    public void makeCall(String number){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

}
