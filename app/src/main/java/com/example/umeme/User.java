package com.example.umeme;

public class User {

    String Phone,Amount,Meter;

    public User(String phone, String amount, String meter) {
        Phone = phone;
        Amount = amount;
        Meter = meter;
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

    public String getMeter() {
        return Meter;
    }

    public void setMeter(String meter) {
        Meter = meter;
    }
}
