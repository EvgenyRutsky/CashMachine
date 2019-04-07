package com.example.dbryuzgin.myapplication;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Product implements Serializable {

    private String name, id, provider_id, price;
    public static int counter = 0;
    public static String[] productInfos = new String[5];
    public static double total = 0;

    private Product(){
    }

    Product(String name, String price, String provider_id, String id) {
        this.name = name;
        this.id = id;
        this.provider_id = provider_id;
        this.price = price;
        productInfos[counter] = "Наименование:  " + name +
                "\n                   Цена:  " + price;
        counter++;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public String getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }
}
