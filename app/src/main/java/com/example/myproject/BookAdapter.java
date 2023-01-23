package com.example.myproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        if(!book.taken_by.equals("none"))
        {
            holder.bookName.setTextColor(Color.RED);
        }

        FirebaseDatabase.getInstance().getReference("Users")
                .child((book.userID))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User userProfile = snapshot.getValue(User.class);

                        if (userProfile != null) {
                            String name = userProfile.first_name;

                            holder.bookPoster.setText(name);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//        holder.bookPoster.setText();
        holder.book_id = book.post_id;

    }

    @Override
    public int getItemCount() {
        return booklist.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView bookName,bookCity, bookPoster;
        String book_id;



        public MyViewHolder(@NonNull View v)
        {
            super(v);
            bookName = v.findViewById(R.id.ITEM_BOOKNAME);
            bookCity = v.findViewById(R.id.ITEM_CITY);
            bookPoster = v.findViewById(R.id.ITEM_USER);


            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(v.getContext(),bookDetails.class);
                    intent.putExtra("book_id",book_id);
                    view.getContext().startActivity(intent);
                }
            });
        }



    }

}
