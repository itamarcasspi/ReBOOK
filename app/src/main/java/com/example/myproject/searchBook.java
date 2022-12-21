package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class searchBook extends AppCompatActivity implements View.OnClickListener {
    private EditText search_input;
    private Button search_btn,menu;
    private Spinner search_param_dropdown;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);

        search_input = (EditText) findViewById(R.id.SEARCH_INPUT);

        search_btn = (Button)findViewById(R.id.SEARCHNOW_BTN);
        search_btn.setOnClickListener(this);

        menu = (Button) findViewById(R.id.SEARCH_TO_MENU);
        menu.setOnClickListener(this);


//        search_param_dropdown = (Spinner) findViewById(R.id.SEARCH_PARAM);
//        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.search_parameter, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
//        search_param_dropdown.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.SEARCH_TO_MENU:
                finish();
                break;
            case R.id.SEARCHNOW_BTN:
                String search_val = search_input.getText().toString().trim();
//                String search_param = search_param_dropdown.getSelectedItem().toString().trim();
                Intent intent = new Intent(searchBook.this, searchOutput.class);
//                intent.putExtra("param",search_param);
                intent.putExtra("value",search_val);
                startActivity(intent);
        }



    }
}