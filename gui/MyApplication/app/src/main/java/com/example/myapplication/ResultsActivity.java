package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.backend.entities.Response;
import com.example.myapplication.backend.entities.Room;
import java.util.List;

public class ResultsActivity extends AppCompatActivity {
    String results;
    Response response;
    List<Room> rooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // Get the Response object from the Intent
        response = (Response) getIntent().getSerializableExtra("response");
        System.out.println("Response:" + response);


        if (response != null) {
            // Get the rooms from the response
            rooms = response.getRooms();
            System.out.println("Rooms: " + rooms);
        } else {
            System.out.println("NO ROOMS FOUND!!");
        }

        // Get the TextView from the layout
        TextView resultsTextView = findViewById(R.id.results);

        // Create a StringBuilder to build the results string
        StringBuilder results = new StringBuilder();

        // Check if rooms is not null before using it
        if (rooms != null) {
            // Loop through the list of rooms and add each room to the results string
            for (Room room : rooms) {
                results.append(room.toString()).append("\n");
            }
        }else {
            results.append("NO ROOMS FOUND!!");
        }

        // Set the text of the TextView with the results string
        resultsTextView.setText(results.toString());
    }
}