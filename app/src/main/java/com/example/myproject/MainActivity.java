package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button signup_btn;
    private MaterialButton login_btn;
    EditText email,password;
    private FirebaseAuth mAuth;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        signup_btn = (Button) findViewById(R.id.signup_btn);
        signup_btn.setOnClickListener(this);

        login_btn = (MaterialButton)  findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);





    }
    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.signup_btn:
                startActivity(new Intent(this, signup.class));
                break;
            case R.id.login_btn:
                userLogin();
                break;

        }
    }

    private void userLogin() {
        String email_str = email.getText().toString().trim();
        String password_str = password.getText().toString().trim();

        if(email_str.isEmpty())
        {
            email.setError("Please enter your email");
            email.requestFocus();
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email_str).matches())
        {
            email.setError("Please a valid Email");;
            email.requestFocus();
        }
        if(password_str.isEmpty())
        {
            password.setError("Please enter your Password");
            password.requestFocus();
        }
        mAuth.signInWithEmailAndPassword(email_str,password_str).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            user = snapshot.getValue(User.class);
                                            if(user != null)
                                            {
                                                if (user.banned.equals("false"))
                                                {
                                                    Toast.makeText(MainActivity.this,"Login successful", Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(MainActivity.this, UserMenu.class));
                                                }
                                                else
                                                {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                                    builder.setCancelable(true);
                                                    builder.setTitle("Ban notice");
                                                    builder.setMessage("User is banned for the following reason: "+user.banned);
                                                    builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            dialogInterface.cancel();
                                                        }
                                                    });
//                                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                                            FirebaseDatabase.getInstance().getReference("Books").child(post_ID)
//                                                                    .removeValue();
//                                                            finish();
//                                                        }
//                                                    });
                                                    builder.show();
                                                    FirebaseAuth.getInstance().signOut();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(MainActivity.this,"Please try again", Toast.LENGTH_LONG).show();

                                        }
                                    });

                }
                else
                {
                    Toast.makeText(MainActivity.this,"Could not login; Please try again", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

}