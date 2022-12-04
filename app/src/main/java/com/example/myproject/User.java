package com.example.myproject;

public class User {
    public String email,password,first_name,last_name;

    public User()
    {

    }
    public User(String email_, String password_,String first_name_,String last_name_)
    {
        this.email = email_;
        this.password = password_;
        this.first_name = first_name_;
        this.last_name = last_name_;
    }
}
