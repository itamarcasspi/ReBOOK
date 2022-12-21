package com.example.myproject;

public class Book {
    public String name, description, city, userID, post_date, post_id;

    public Book()
    {

    }

    public Book(String name_, String description_, String city_, String userID_, String date, String id_)
    {
        this.name = name_;
        this.description = description_;
        this.city = city_;
        this.userID = userID_;
        this.post_date = date;
        this.post_id = id_;

    }
}
