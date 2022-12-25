package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class bookDetails extends AppCompatActivity implements View.OnClickListener{
    private ImageView back_btn, delete_btn;
//    private Book this_book;
    private TextView BOOK_NAME,BOOK_DESC,BOOK_CITY, POSTER_NAME,POSTER_PHONE,POST_DATE;
    private String post_ID,curr_user_ID,user_priv;
    private Spinner cityMenu;
    private DatabaseReference reference;
    private FirebaseUser user;
    private String userID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);


        back_btn = findViewById(R.id.book_detail_RETURN);
        back_btn.setOnClickListener(this);

        delete_btn = findViewById(R.id.book_detail_DELETE);


        BOOK_NAME = findViewById(R.id.book_detail_NAME);
        BOOK_DESC = findViewById(R.id.book_detail_DESC);
        BOOK_CITY = findViewById(R.id.book_detail_CITY);
        POSTER_NAME = findViewById(R.id.book_detail_POSTER);
        POST_DATE = findViewById(R.id.book_detail_DATE);
        POSTER_PHONE = findViewById(R.id.book_detail_PHONE);


        curr_user_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference_users = FirebaseDatabase.getInstance().getReference("Users");

        //find out curr user's privilege
        reference_users.child(curr_user_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null)
                {
                    if(userProfile.privilege.equals("1"))
                    {

                        delete_btn.setVisibility(View.VISIBLE);
                        delete_btn.setOnClickListener(bookDetails.this);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(bookDetails.this,"Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });

        Intent prev_int = getIntent();
        post_ID = prev_int.getStringExtra("book_id");
//        BOOK_NAME.setText(post_ID);
        reference = FirebaseDatabase.getInstance().getReference("Books");
        reference.child(post_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Book this_book = snapshot.getValue(Book.class);



                if(this_book != null)
                {
                    BOOK_NAME.setText(this_book.name);
                    BOOK_DESC.setText(this_book.description);
                    BOOK_CITY.setText(this_book.city);
                    POST_DATE.setText("Posted on "+this_book.post_date+",");
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(this_book.userID)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    User userProfile = snapshot.getValue(User.class);


                                    if(userProfile != null)
                                    {
                                        POSTER_NAME.setText(userProfile.first_name);
                                        POSTER_PHONE.setText(userProfile.phone_num);

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(bookDetails.this,"Something went wrong!", Toast.LENGTH_LONG).show();

                                }
                            });
                }
                //user_priv = 1 -> user is an admin
                if (this_book.userID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                {
                    delete_btn.setVisibility(View.VISIBLE);
                    delete_btn.setOnClickListener(bookDetails.this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(bookDetails.this,"Something went wrong!", Toast.LENGTH_LONG).show();

            }
        });


    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.book_detail_RETURN:
                finish();
                break;
            case R.id.book_detail_DELETE:
                FirebaseDatabase.getInstance().getReference("Books").child(post_ID)
                        .removeValue();
                finish();



        }
    }





}





