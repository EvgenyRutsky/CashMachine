package com.example.dbryuzgin.myapplication;

public class User {

    private String email, password;

    private User(){
    }

    User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
