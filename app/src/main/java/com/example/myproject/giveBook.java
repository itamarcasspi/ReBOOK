package com.example.myproject;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class giveBook extends AppCompatActivity implements View.OnClickListener{
    private Button give_btn;
    private Button menu_btn;
    private EditText BOOK_NAME,BOOK_DESC,BOOK_CITY;
    private Spinner cityMenu;
    private DatabaseReference reference;
    private FirebaseUser user;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_book);

        give_btn = (Button)findViewById(R.id.give_away);
        give_btn.setOnClickListener(this);

        menu_btn = (Button)findViewById(R.id.menu_btn);
        menu_btn.setOnClickListener(this);

        BOOK_NAME = findViewById(R.id.give_book_name);
        BOOK_DESC = findViewById(R.id.give_book_description);
//        BOOK_CITY = findViewById(R.id.city_list_dropdown);

        cityMenu = findViewById(R.id.city_list_dropdown);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        cityMenu.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){

            case R.id.menu_btn:
                startActivity(new Intent(giveBook.this, UserMenu.class));
                break;

            case R.id.give_away:
                postBook();
                break;


        }
    }

    private void postBook()
    {
        String book_name = BOOK_NAME.getText().toString().trim();
        String book_desc = BOOK_DESC.getText().toString().trim();
        String book_city = cityMenu.getSelectedItem().toString().trim();

        if(book_name.isEmpty())
        {
            BOOK_NAME.setError("Please enter book name");
            BOOK_NAME.requestFocus();
            return;
        }
        if(book_desc.isEmpty())
        {
            BOOK_DESC.setError("Please enter book name");
            BOOK_DESC.requestFocus();
            return;

        }
        if(book_city.equals("Please choose a city"))
        {

            cityMenu.requestFocus();
            return;

        }

        Book newBook = new Book(book_name,book_desc,book_city,FirebaseAuth.getInstance().getCurrentUser().getUid());
        String unique_id = FirebaseDatabase.getInstance().getReference("Books").push().getKey();
        FirebaseDatabase.getInstance().getReference("Books")
                .child(unique_id)
                .setValue(newBook).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(giveBook.this,"Successfully posted.", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(giveBook.this, UserMenu.class));

                        }
                        else
                        {
                            Toast.makeText(giveBook.this,"Failed to post.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}