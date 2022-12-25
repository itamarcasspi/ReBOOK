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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class searchOutput extends AppCompatActivity implements View.OnClickListener{
    TextView menu_btn, search_string;
    RecyclerView recyclerView;
    DatabaseReference database;
    BookAdapter adapter;
    ArrayList<Book> booklist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_output);

        menu_btn = findViewById(R.id.SEARCHRESULT_menu);
        menu_btn.setOnClickListener(this);


        recyclerView = findViewById(R.id.SEARCH_BOOK_LST);
        database = FirebaseDatabase.getInstance().getReference("Books");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Extract the search parameters from serach page:
        Intent get_intent = getIntent();
        String search_value = get_intent.getStringExtra("value");
        String search_parm = get_intent.getStringExtra("param");

        //Display what was the search input from previous activity.
        search_string = (TextView)findViewById(R.id.search_result_string);
        search_string.setText("Showing result for - " + search_value);



        //Create a booklist of all the relevant books for the search input.
        booklist = new ArrayList<>();
        adapter = new BookAdapter(this,booklist);
        recyclerView.setAdapter(adapter);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {

                    Book book = dataSnapshot.getValue(Book.class);
                    if(book.name.equals(search_value) || book.city.equals(search_value)) {
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
        switch(view.getId()){
            case R.id.SEARCHRESULT_menu:
                finish();
                break;


        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        menu_btn = findViewById(R.id.SEARCHRESULT_menu);
        menu_btn.setOnClickListener(this);


        recyclerView = findViewById(R.id.SEARCH_BOOK_LST);
        database = FirebaseDatabase.getInstance().getReference("Books");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Extract the search parameters from serach page:
        Intent get_intent = getIntent();
        String search_value = get_intent.getStringExtra("value");
        String search_parm = get_intent.getStringExtra("param");

        //Display what was the search input from previous activity.
        search_string = (TextView)findViewById(R.id.search_result_string);
        search_string.setText("Showing result for - " + search_value);



        //Create a booklist of all the relevant books for the search input.
        booklist = new ArrayList<>();
        adapter = new BookAdapter(this,booklist);
        recyclerView.setAdapter(adapter);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {

                    Book book = dataSnapshot.getValue(Book.class);
                    if(book.name.equals(search_value) || book.city.equals(search_value)) {
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