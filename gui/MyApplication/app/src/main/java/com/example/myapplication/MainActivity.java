package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.myapplication.backend.Client;

public class MainActivity extends AppCompatActivity {

    Client client;

    EditText editText;

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.Username);
        //TODO: Add setters/getters for the Client (username)
        //TODO: Add function with logic that returns statements to validate in the frontend
        btn = (Button) findViewById(R.id.btn);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
//                Toast.makeText(getApplicationContext(),text, Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(),ImageActivity.class);
                i.putExtra("text",text);
                startActivity(i);
            }
        });
    }
}