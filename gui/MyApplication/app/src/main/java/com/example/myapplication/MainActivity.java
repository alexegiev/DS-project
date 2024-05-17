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

    Client client = new Client();

    EditText editText;

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.Username);
        btn = (Button) findViewById(R.id.btn);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                if (client.validate("Renter", text)){
                    System.out.println("Valid username: " + text);
                    Intent i = new Intent(getApplicationContext(),ImageActivity.class);
                    i.putExtra("text",text);
                    startActivity(i);
                }
                else{
                    System.out.println("Invalid username: " + text);
                    Toast.makeText(MainActivity.this, "Invalid username, please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}