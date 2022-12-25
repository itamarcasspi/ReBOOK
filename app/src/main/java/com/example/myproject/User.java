package com.example.myproject;

public class User {
    public String email,password,first_name,phone_num,privilege;


    public User()
    {

    }
    public User(String email_, String password_,String first_name_,String phone_,String privilege_)
    {
        this.email = email_;
        this.password = password_;
        this.first_name = first_name_;
        this.phone_num = phone_;
        this.privilege = privilege_;

    }
}
