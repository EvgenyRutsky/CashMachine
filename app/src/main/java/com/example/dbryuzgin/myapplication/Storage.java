package com.example.dbryuzgin.myapplication;

public class Storage {

    private String count, product_id;

    private Storage(){
    }

    public Storage(String count, String product_id) {
        this.count = count;
        this.product_id = product_id;
    }

    public String getCount() {
        return count;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
}
