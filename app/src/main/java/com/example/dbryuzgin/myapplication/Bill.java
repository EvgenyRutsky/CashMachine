package com.example.dbryuzgin.myapplication;

import java.io.Serializable;

public class Bill implements Serializable {

    private String date, difference, money, number, product_ids, time, total, user_email, deliveryState, fio, address, tel;

    private Bill(){
    }

    Bill(String Date, String Difference, String Money, String Number, String Product_ids, String Time, String Total, String User_email, String deliveryState, String fio, String address, String tel) {
        this.date = Date;
        this.difference = Difference;
        this.money = Money;
        this.number = Number;
        this.product_ids = Product_ids;
        this.time = Time;
        this.total = Total;
        this.user_email = User_email;
        this.deliveryState = deliveryState;
        this.fio = fio;
        this.address = address;
        this.tel = tel;
    }
    public String getDeliveryState () {
        return deliveryState;
    }

    public String getDate() {
        return date;
    }

    public String getDifference() {
        return difference;
    }

    public String getMoney() {
        return money;
    }

    public String getNumber() {
        return number;
    }

    public String getProduct_ids() {
        return product_ids;
    }

    public String getTime() {
        return time;
    }

    public String getTotal() {
        return total;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getFio(){ return fio; }

    public String getAddress(){ return address;}

    public String getTel(){ return tel; }


    public void setFio (String fio){
        this.fio = fio;
    }

    public  void setAddress (String address){
        this.address = address;
    }

    public void setTel (String tel){
        this.tel = tel;
    }
    public void setDeliveryState (String deliveryState){
        this.deliveryState = deliveryState;
    }
}
