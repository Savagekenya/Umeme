package com.example.admin;

public class Customer {

    String Token,phoneNumber,Email;

    public Customer(String token, String phoneNumber, String email) {
        Token = token;
        this.phoneNumber = phoneNumber;
        Email = email;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Customer(){

    }
}
