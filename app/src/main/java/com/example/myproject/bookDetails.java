package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class bookDetails extends AppCompatActivity implements View.OnClickListener {
    private ImageView back_btn, delete_btn;
    //    private Book this_book;
    private TextView BOOK_NAME, BOOK_DESC, BOOK_CITY, POSTER_NAME, TAKE_BOOK, POST_DATE,
            MARK_TAKEN, FORFEIT_BOOK,PHONE_NUM,TAKER_NUM;
    private String post_ID, curr_user_ID, user_priv;
    private Spinner cityMenu;
    private DatabaseReference reference;
    private FirebaseUser user;
    private String userID;
    private Book this_book;
    private ArrayList<Book> booklist;
    private User poster_user,curr_user;
    private Button BAN_USER;


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

        TAKE_BOOK = findViewById(R.id.take_book_details);

        MARK_TAKEN = findViewById(R.id.mark_as_taken);

        FORFEIT_BOOK = findViewById(R.id.forfeit_book);

        BAN_USER = findViewById(R.id.ban_btn);

        PHONE_NUM = findViewById(R.id.PHONE_NUM);

        TAKER_NUM = findViewById(R.id.TAKER_PHONE);

        curr_user_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference_users = FirebaseDatabase.getInstance().getReference("Users");

        //find out curr user's privilege
        reference_users.child(curr_user_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                curr_user = snapshot.getValue(User.class);

                if (curr_user != null) {
                    //If user is an admin:
                    if (curr_user.privilege.equals("1")) {

                        delete_btn.setVisibility(View.VISIBLE);
                        delete_btn.setOnClickListener(bookDetails.this);
                        BAN_USER.setVisibility(View.VISIBLE);
                        BAN_USER.setOnClickListener(bookDetails.this);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(bookDetails.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });

        Intent prev_int = getIntent();
        post_ID = prev_int.getStringExtra("book_id");
//        BOOK_NAME.setText(post_ID);
        reference = FirebaseDatabase.getInstance().getReference("Books");
        reference.child(post_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                this_book = snapshot.getValue(Book.class);


                if (this_book != null) {
                    BOOK_NAME.setText(this_book.name);
                    BOOK_DESC.setText(this_book.description);
                    BOOK_CITY.setText(this_book.city);
                    POST_DATE.setText("Posted on " + this_book.post_date + ",");
                    //get the poster's user profile.
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(this_book.userID)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    poster_user = snapshot.getValue(User.class);


                                    if (poster_user != null) {
                                        POSTER_NAME.setText(poster_user.first_name);
                                        if (this_book.taken_by.equals(curr_user_ID)) {
                                            PHONE_NUM.setVisibility(View.VISIBLE);
                                            PHONE_NUM.setText(poster_user.phone_num);

                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(bookDetails.this, "Something went wrong!", Toast.LENGTH_LONG).show();

                                }
                            });

                }
                //case when book poster is current user
                if (this_book.userID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    delete_btn.setVisibility(View.VISIBLE);
                    delete_btn.setOnClickListener(bookDetails.this);
                    //if book is taken by someone and it is the users book, allow mark as taken.
                    if (!this_book.taken_by.equals("none")) {
                        MARK_TAKEN.setVisibility(View.VISIBLE);
                        MARK_TAKEN.setOnClickListener(bookDetails.this);

                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(this_book.taken_by)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        User taker = snapshot.getValue(User.class);
                                        if(taker != null)
                                        {
                                            TAKER_NUM.setText("Contact num- "+taker.phone_num);
                                            TAKER_NUM.setVisibility(View.VISIBLE);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                    }


                }
                //not poster's book
                else {
                    //book is not taken
                    if (this_book.taken_by.equals("none")) {
                        TAKE_BOOK.setVisibility(View.VISIBLE);
                        TAKE_BOOK.setOnClickListener(bookDetails.this);
                    }
                    //if book is taken by current user, allow to forfeit it.
                    else if (this_book.taken_by.equals(curr_user_ID)) {
                        FORFEIT_BOOK.setVisibility(View.VISIBLE);
                        FORFEIT_BOOK.setOnClickListener(bookDetails.this);



                    }
                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(bookDetails.this, "Something went wrong!", Toast.LENGTH_LONG).show();

            }
        });

//
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.book_detail_RETURN:
                finish();
                break;
            case R.id.book_detail_DELETE:
                AlertDialog.Builder builder = new AlertDialog.Builder(bookDetails.this);
                builder.setCancelable(true);
                builder.setTitle("Delete post");
                builder.setMessage("Are you sure you want to delete this book's listing?");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference("Books").child(post_ID)
                                .removeValue();
                        if(!this_book.taken_by.equals("none"))
                        {
                            //lower book poster books to give
                            poster_user.books_to_give--;
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(this_book.userID).setValue(poster_user);
                            //lower taker books to take
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(this_book.taken_by)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            User taker_user = snapshot.getValue(User.class);

                                            if(taker_user != null)
                                            {
                                                taker_user.books_to_take--;
                                                FirebaseDatabase.getInstance().getReference("Users")
                                                        .child(this_book.taken_by).setValue(taker_user);

                                            }


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(bookDetails.this, "Something went wrong!", Toast.LENGTH_LONG).show();

                                        }
                                    });
                        }
                        finish();
                    }
                });
                builder.show();

                break;
            case R.id.take_book_details:

                this_book.taken_by = curr_user_ID;
                FirebaseDatabase.getInstance().getReference("Books").child(post_ID)
                        .setValue(this_book);
                //raise curr user's books to take count
                curr_user.books_to_take++;
                FirebaseDatabase.getInstance().getReference("Users").child(curr_user_ID)
                                .setValue(curr_user);

                //raise book poster waiting book count
                poster_user.books_to_give++;
                FirebaseDatabase.getInstance().getReference("Users").child(this_book.userID)
                        .setValue(poster_user);

                Toast.makeText(this, "Book taken!", Toast.LENGTH_LONG);
                finish();
                break;
            case R.id.forfeit_book:
                this_book.taken_by = "none";
                FirebaseDatabase.getInstance().getReference("Books").child(post_ID)
                        .setValue(this_book);
                //lower curr user's books to take count
                curr_user.books_to_take--;
                FirebaseDatabase.getInstance().getReference("Users").child(curr_user_ID)
                        .setValue(curr_user);

                poster_user.books_to_give--;
                FirebaseDatabase.getInstance().getReference("Users").child(this_book.userID)
                        .setValue(poster_user);


                Toast.makeText(this, "Book forfeited!", Toast.LENGTH_LONG);
                finish();
                break;
            case R.id.ban_btn:
                final EditText ban_reason = new EditText(this);
                AlertDialog.Builder ban_builder = new AlertDialog.Builder(bookDetails.this);
                ban_builder.setCancelable(true);
                ban_builder.setTitle("Ban user");
                ban_builder.setMessage("Enter ban reason");
                ban_builder.setView(ban_reason);
                ban_builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                ban_builder.setPositiveButton("Ban user", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        String reason_string = ban_reason.getText().toString();
                        poster_user.banned = reason_string;
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(this_book.userID).setValue(poster_user);

                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                    Book book = dataSnapshot.getValue(Book.class);
                                    if(book!= null)
                                    {
                                        if(book.userID.equals(this_book.userID))
                                        {
                                            FirebaseDatabase.getInstance().getReference("Books")
                                                    .child(book.post_id)
                                                    .removeValue();
                                        }
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }


                        });
                        finish();

                    }
                });
                ban_builder.show();
                break;


            case R.id.mark_as_taken:
                //when marked as, should delete book and raise counters for poster and taker.
                FirebaseDatabase.getInstance().getReference("Books")
                        .child(this_book.post_id)
                        .removeValue();
                //raise poster books given.
                poster_user.books_given++;
                poster_user.books_to_give--;
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(this_book.userID).setValue(poster_user);


                //raise taker books taken

                FirebaseDatabase.getInstance().getReference("Users")
                        .child(this_book.taken_by)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User taker_user = snapshot.getValue(User.class);

                                if(taker_user != null)
                                {
                                    taker_user.books_took++;
                                    taker_user.books_to_take--;
                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(this_book.taken_by).setValue(taker_user);

                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(bookDetails.this, "Something went wrong!", Toast.LENGTH_LONG).show();

                            }
                        });

                Toast.makeText(bookDetails.this, "Book giving process is complete!", Toast.LENGTH_LONG).show();
                finish();
                break;

        }

    }


}





