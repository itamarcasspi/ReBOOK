package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class UserMenu extends AppCompatActivity implements View.OnClickListener {
    private TextView logout_btn,givebook_btn,getbook_btn,mybooks_btn,pending_btn
            ,books_taken,books_given;
    private DatabaseReference reference;
    private FirebaseUser user;
    private String userID;
    private User userProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);

        logout_btn = findViewById(R.id.LOGOUT_BTN);
        logout_btn.setOnClickListener(this);

        givebook_btn=findViewById(R.id.giveBook_button);
        givebook_btn.setOnClickListener(this);

        getbook_btn = findViewById(R.id.getBook_button);
        getbook_btn.setOnClickListener(this);

        mybooks_btn = findViewById(R.id.myBooks_button);
        mybooks_btn.setOnClickListener(this);

        pending_btn = findViewById(R.id.PENDING_BOOKS);
        pending_btn.setOnClickListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView greeting_name = (TextView)findViewById(R.id.first_name_greet);
        final TextView books_taken = findViewById(R.id.BOOKS_TAKEN);
        final TextView books_given = findViewById(R.id.BOOKS_GIVEN);
        final TextView books_pending = findViewById(R.id.BOOKS_TO_TAKE);
        final TextView books_waiting = findViewById(R.id.BOOKS_TO_GIVE);

        reference.child((userID)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userProfile = snapshot.getValue(User.class);

                if(userProfile != null)
                {
                    String name = userProfile.first_name;

                    greeting_name.setText("Welcome, "+name+"!");
                    books_taken.setText("Books taken: "+userProfile.books_took);
                    books_given.setText("Books given: "+userProfile.books_given);
                    books_pending.setText("Books to take: "+userProfile.books_to_take);
                    books_waiting.setText("Books to give: "+userProfile.books_to_give);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserMenu.this,"Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.LOGOUT_BTN:
                FirebaseAuth.getInstance().signOut();
                finish();
                break;
            case R.id.giveBook_button:
                startActivity(new Intent(UserMenu.this, giveBook.class));
                break;
            case R.id.getBook_button:
                startActivity(new Intent(UserMenu.this, allBooks.class));
                break;

            case R.id.myBooks_button:
                startActivity(new Intent(UserMenu.this, myListed.class));
                break;

            case R.id.PENDING_BOOKS:
                startActivity(new Intent(UserMenu.this, booksToCollect.class));


        }

    }

    @Override
    protected void onRestart() {

        super.onRestart();
        logout_btn = findViewById(R.id.LOGOUT_BTN);
        logout_btn.setOnClickListener(this);

        givebook_btn=findViewById(R.id.giveBook_button);
        givebook_btn.setOnClickListener(this);

        getbook_btn = findViewById(R.id.getBook_button);
        getbook_btn.setOnClickListener(this);

        mybooks_btn = findViewById(R.id.myBooks_button);
        mybooks_btn.setOnClickListener(this);

        pending_btn = findViewById(R.id.PENDING_BOOKS);
        pending_btn.setOnClickListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView greeting_name = (TextView)findViewById(R.id.first_name_greet);
        final TextView books_taken = findViewById(R.id.BOOKS_TAKEN);
        final TextView books_given = findViewById(R.id.BOOKS_GIVEN);
        final TextView books_pending = findViewById(R.id.BOOKS_TO_TAKE);
        final TextView books_waiting = findViewById(R.id.BOOKS_TO_GIVE);

        reference.child((userID)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userProfile = snapshot.getValue(User.class);

                if(userProfile != null)
                {
                    String name = userProfile.first_name;

                    greeting_name.setText("Welcome, "+name+"!");
                    books_taken.setText("Books taken: "+userProfile.books_took);
                    books_given.setText("Books given: "+userProfile.books_given);
                    books_pending.setText("Books to take: "+userProfile.books_to_take);
                    books_waiting.setText("Books to give: "+userProfile.books_to_give);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserMenu.this,"Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

}