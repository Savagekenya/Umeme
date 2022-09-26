package com.example.umeme;

public class User {

    String Phone,Amount;

    public User(String phone, String amount) {
        Phone = phone;
        Amount = amount;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public User() {

    }
}
