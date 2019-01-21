package com.example.dbryuzgin.myapplication;

import java.io.Serializable;

public class Bill implements Serializable {

    private String date, difference, money, number, product_ids, time, total, user_email;

    private Bill(){
    }

    Bill(String Date, String Difference, String Money, String Number, String Product_ids, String Time, String Total, String User_email) {
        this.date = Date;
        this.difference = Difference;
        this.money = Money;
        this.number = Number;
        this.product_ids = Product_ids;
        this.time = Time;
        this.total = Total;
        this.user_email = User_email;
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
}
