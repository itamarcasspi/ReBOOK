package com.example.myproject;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myproject.databinding.ActivitySignupBinding;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


import java.util.regex.Pattern;

public class signup extends AppCompatActivity implements View.OnClickListener{
    private Button sign_in;
    private Button sign_up;
    private FirebaseAuth mAuth;
    private AppBarConfiguration appBarConfiguration;
    private ActivitySignupBinding binding;
    private EditText TextEmail,TextPassword,Text_first_name,Text_last_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sign_in = (Button) findViewById(R.id.signin_refferal);
        sign_in.setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();
        sign_up = (Button) findViewById(R.id.signup_btn);
        sign_up.setOnClickListener(this);

        TextEmail = (EditText) findViewById(R.id.email);
        TextPassword = (EditText) findViewById(R.id.password);
        Text_first_name = (EditText) findViewById(R.id.first_name);
        Text_last_name = (EditText) findViewById(R.id.second_name);

    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.signin_refferal:
                startActivity(new Intent(this,MainActivity.class));
                break;

            case R.id.signup_btn:
                registerUser();
                break;
        }

    }

    private void registerUser(){
        String email = TextEmail.getText().toString().trim();
        String password = TextPassword.getText().toString().trim();
        String first_name = Text_first_name.getText().toString().trim();
        String last_name = Text_last_name.getText().toString().trim();

        if(email.isEmpty())
        {
            TextEmail.setError("Email field is empty.");
            TextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            TextEmail.setError("Please provide valid email");
            TextEmail.requestFocus();
            return;

        }

        if(password.isEmpty())
        {
            TextPassword.setError("Password field is empty.");
            TextPassword.requestFocus();
            return;

        }
        if(password.length()<6)
        {
            TextPassword.setError("Password should be at least 6 charcters long.");
            TextPassword.requestFocus();
            return;

        }

        if(first_name.isEmpty())
        {
            Text_first_name.setError("First name field is empty");
            Text_first_name.requestFocus();
            return;

        }
        if(last_name.isEmpty())
        {
            Text_last_name.setError("Last name field is empty");
            Text_last_name.requestFocus();
            return;

        }
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            User new_user = new User(email,password,first_name,last_name);


                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(new_user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(signup.this,"Successfully registered.", Toast.LENGTH_LONG).show();
                                            }
                                            else
                                            {
                                                Toast.makeText(signup.this,"Failed register.", Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    });
                        }
                        else
                        {
                            Toast.makeText(signup.this,"Error occured.", Toast.LENGTH_LONG).show();

                        }
                    }
                });





    }
}