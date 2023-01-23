package com.example.myproject;

public class User {
    public String email,password,first_name,phone_num,privilege,banned;
    public int books_took, books_given,books_to_take,books_to_give;


    public User()
    {

    }
    public User(String email_, String password_,String first_name_,String phone_,String privilege_
    ,String banned_, int books_took_, int books_given_,int books_to_take_, int books_to_give_)
    {
        this.email = email_;
        this.password = password_;
        this.first_name = first_name_;
        this.phone_num = phone_;
        this.privilege = privilege_;
        this.banned = banned_;
        this.books_took = books_took_;
        this.books_given = books_given_;
        this.books_to_give = books_to_give_;
        this.books_to_take = books_to_take_;
    }
}
