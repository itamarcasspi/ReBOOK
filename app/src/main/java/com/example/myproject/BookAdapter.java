package com.example.myproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {
    Context context;
    ArrayList<Book> booklist;

    public BookAdapter(Context context, ArrayList<Book> booklist)
    {
        this.context = context;
        this.booklist = booklist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Book book = booklist.get(position);
        holder.bookName.setText(book.name);
        holder.bookCity.setText(book.city);
    }

    @Override
    public int getItemCount() {
        return booklist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView bookName,bookCity;


        public MyViewHolder(@NonNull View v)
        {
            super(v);
            bookName = v.findViewById(R.id.ITEM_BOOKNAME);
            bookCity = v.findViewById(R.id.ITEM_CITY);
        }

    }

}