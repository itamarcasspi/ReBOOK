package com.example.myproject;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class booksToCollect extends AppCompatActivity implements View.OnClickListener{
    TextView menu_btn;
    String curr_user_id;
    RecyclerView recyclerView;
    DatabaseReference database;
    BookAdapter adapter;
    ArrayList<Book> booklist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_to_collect);

        menu_btn = findViewById(R.id.mybooks_menu);
        menu_btn.setOnClickListener(this);

        recyclerView = findViewById(R.id.MY_BOOK_LST);
        database = FirebaseDatabase.getInstance().getReference("Books");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        booklist = new ArrayList<>();
        adapter = new BookAdapter(this,booklist);
        recyclerView.setAdapter(adapter);

        curr_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {

                    Book book = dataSnapshot.getValue(Book.class);
                    if(book.taken_by.equals(curr_user_id)) {
                        booklist.add(book);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(booksToCollect.this,"Something went wrong!", Toast.LENGTH_LONG).show();

            }
        });


    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.mybooks_menu:
                finish();
                break;

        }

    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        menu_btn = findViewById(R.id.mybooks_menu);
        menu_btn.setOnClickListener(this);

        recyclerView = findViewById(R.id.MY_BOOK_LST);
        database = FirebaseDatabase.getInstance().getReference("Books");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        booklist = new ArrayList<>();
        adapter = new BookAdapter(this,booklist);
        recyclerView.setAdapter(adapter);

        curr_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {

                    Book book = dataSnapshot.getValue(Book.class);
                    if(book.taken_by.equals(curr_user_id)) {
                        booklist.add(book);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(booksToCollect.this,"Something went wrong!", Toast.LENGTH_LONG).show();

            }
        });

    }
}