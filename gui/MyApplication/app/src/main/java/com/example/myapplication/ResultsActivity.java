package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.myapplication.backend.entities.Response;
import com.example.myapplication.backend.entities.Room;
import java.util.List;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.view.ViewGroup;
import java.util.Date;

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

        if (response != null) {
            // Get the rooms from the response
            rooms = response.getRooms();
        }

        // Get the ScrollView from the layout
        LinearLayout resultsContainer = findViewById(R.id.results);

        // Create a new LinearLayout
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        // Check if rooms is not null before using it
        if (rooms != null) {
            // Loop through the list of rooms and create a CardView for each room
            for (Room room : rooms) {
                // Create a new CardView
                CardView cardView = new CardView(this);

                // Create a new ImageView for the room image
                ImageView imageView = new ImageView(this);

                // Set the CardView attributes
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, // Width
                        LinearLayout.LayoutParams.WRAP_CONTENT  // Height
                );
                layoutParams.setMargins(0, 0, 0, 16); // Bottom margin
                cardView.setLayoutParams(layoutParams);
                cardView.setRadius(16); // Corner radius
                cardView.setCardElevation(8); // Elevation
                cardView.setContentPadding(16, 16, 16, 16); // Padding

                // Check if the room image bytes is not null
                if (room.getRoomImage() != null) {
                    // Decode the byte array into a Bitmap
                    Bitmap bitmap = BitmapFactory.decodeByteArray(room.getRoomImage(), 0, room.getRoomImage().length);

                    // Set the Bitmap to the ImageView
                    imageView.setImageBitmap(bitmap);
                }

                // Create a new TextView for the room
                TextView textView = new TextView(this);

                String roomDetails = "Room Name: " + room.getRoomName() + "\n" +
                        "Area: " + room.getArea() + "\n" +
                        "Rating: " + room.getRating() + "\n" +
                        "Number of Reviews: " + room.getNumberOfReviews() + "\n" +
                        "Capacity: " + room.getCapacity() + "\n" +
                        "Price: " + room.getPrice() + "\n" +
                        "Available from: " + room.getFrom() + "\n" +
                        "Available to: " + room.getTo() + "\n" +
                        "available dates: " + room.getAvailableDates() + "\n";
                textView.setText(roomDetails);
                textView.setTextSize(16);

                // Add the TextView to the CardView
                cardView.addView(textView);

                // Add the ImageView to the CardView
                cardView.addView(imageView);

                // Add the CardView to the LinearLayout
                resultsContainer.addView(cardView);
            }
        } else {
            // If there are no rooms, show a message
            TextView textView = new TextView(this);
            textView.setText("No rooms found");
            textView.setTextSize(16);
            linearLayout.addView(textView);
        }
    }
}