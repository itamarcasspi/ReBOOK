package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class allBooks extends AppCompatActivity implements View.OnClickListener {
    TextView menu_btn,search_now;
    EditText search_input;

    RecyclerView recyclerView;
    DatabaseReference database;
    BookAdapter adapter;
    ArrayList<Book> booklist;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_books);

        recyclerView = findViewById(R.id.BOOK_LST);
        database = FirebaseDatabase.getInstance().getReference("Books");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        menu_btn = findViewById(R.id.allbooks_menu);
        menu_btn.setOnClickListener(this);

        search_now = findViewById(R.id.search_now);
        search_now.setOnClickListener(this);

        search_input = findViewById(R.id.allbooks_search_in);


        booklist = new ArrayList<>();
        adapter = new BookAdapter(this,booklist);
        recyclerView.setAdapter(adapter);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Book book = dataSnapshot.getValue(Book.class);
                    if(book.taken_by.equals("none")) {
                        booklist.add(book);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.allbooks_menu:
                finish();
                break;
            case R.id.search_now:
                String search_val = search_input.getText().toString().trim();
//                String search_param = search_param_dropdown.getSelectedItem().toString().trim();
                Intent intent = new Intent(allBooks.this, searchOutput.class);
//                intent.putExtra("param",search_param);
                intent.putExtra("value",search_val);
                startActivity(intent);

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recyclerView = findViewById(R.id.BOOK_LST);
        database = FirebaseDatabase.getInstance().getReference("Books");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        menu_btn = findViewById(R.id.allbooks_menu);
        menu_btn.setOnClickListener(this);

        booklist = new ArrayList<>();
        adapter = new BookAdapter(this,booklist);
        recyclerView.setAdapter(adapter);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Book book = dataSnapshot.getValue(Book.class);
                    if(book.taken_by.equals("none")) {
                        booklist.add(book);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}