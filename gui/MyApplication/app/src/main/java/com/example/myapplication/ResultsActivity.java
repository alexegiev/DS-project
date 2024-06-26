package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.myapplication.backend.entities.Response;
import com.example.myapplication.backend.entities.Room;
import java.util.List;
import android.widget.LinearLayout;
import android.widget.Button;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;


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

        // Get the rooms from the response
        if (response != null) {
            rooms = response.getRooms();
        }

        //get the container where results will be displayed
        LinearLayout resultsContainer = findViewById(R.id.results);

        // Create a new LinearLayout
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        // Check if rooms is not null before using it
        if (rooms != null) {
            // Loop through the list of rooms and create a CardView for each room
            for (Room room : rooms) {
                // Create a new CardView to display each room
                CardView cardView = new CardView(this);

                // Set the CardView attributes
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, // Width
                        LinearLayout.LayoutParams.MATCH_PARENT  // Height
                );
                layoutParams.setMargins(0, 0, 0, 16); // Bottom margin
                cardView.setLayoutParams(layoutParams);
                cardView.setRadius(16); // Corner radius
                cardView.setCardElevation(8); // Elevation
                cardView.setContentPadding(16, 16, 16, 16); // Padding
                cardView.setCardBackgroundColor(getResources().getColor(R.color.results_containers_color)); // colour


                // Create a new RelativeLayout for the room details/ text inside the card
                RelativeLayout roomDetailsLayout = new RelativeLayout(this);
                RelativeLayout.LayoutParams roomDetailsLayoutParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, // Width
                        RelativeLayout.LayoutParams.WRAP_CONTENT  // Height
                );

                roomDetailsLayout.setLayoutParams(roomDetailsLayoutParams);

                // Create a new TextView for the room
                TextView textView = new TextView(this);

                // Create a new Button for booking the room
                Button button = new Button(this);
                button.setText("Book Now");

                // Set the Button layout parameters
                RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,  // Width
                        RelativeLayout.LayoutParams.WRAP_CONTENT   // Height
                );
                buttonParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM); // Align to the bottom of the parent
                buttonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT); // Align to the right of the parent
                button.setBackgroundColor(getResources().getColor(R.color.header_color)); //button colour
                button.setTextColor(getResources().getColor(R.color.white)); // button text colour

                button.setLayoutParams(buttonParams);


                // Getting the available dates for the rooms and displaying them

                Map<Date, Date> availableDatesMap = room.getAvailableDates();

                // Check if the map is not empty
                if (!availableDatesMap.isEmpty()) {
                    // Sort the entries by the key (start date)
                    TreeMap<Date, Date> sortedDatesMap = new TreeMap<>(availableDatesMap);

                    // Get the earliest fromDate and the latest toDate
                    Date first_fromDate = sortedDatesMap.firstKey();
                    Date second_fromDate = sortedDatesMap.lastKey();
                    Date first_toDate = sortedDatesMap.firstEntry().getValue();
                    Date second_toDate = sortedDatesMap.lastEntry().getValue();

                    // Create a SimpleDateFormat object for formatting the dates
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

                    // Format the dates into strings
                    String first_fromDateStr = dateFormat.format(first_fromDate);
                    String second_fromDateStr = dateFormat.format(second_fromDate);
                    String first_toDateStr = dateFormat.format(first_toDate);
                    String second_toDateStr = dateFormat.format(second_toDate);

                    // Set the TextView text that will appear in the CardView
                    String roomDetails = room.getRoomName() + "\n" +
                            "Area: " + room.getArea() + "\n" +
                            "Rating: " + room.getRating() + "\n" +
                            "Number of Reviews: " + room.getNumberOfReviews() + "\n" +
                            "Capacity: " + room.getCapacity() + " people" + "\n" +
                            "Price: " + room.getPrice() + " €" + "\n" +
                            "Available dates :" + first_fromDateStr + " - " + first_toDateStr + "\n" +
                            "and: " + second_fromDateStr + " - " + second_toDateStr + "\n";
                    textView.setText(roomDetails);
                    textView.setTextSize(16);

                    roomDetailsLayout.addView(textView);
                    roomDetailsLayout.addView(button);


                    ImageView imageView = new ImageView(this);
                    // Set the ImageView layout parameters
                    RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                            400,  // Width
                            400      // Height
                    );
                    imageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT); // Align to the right of the parent
                    imageParams.addRule(RelativeLayout.ALIGN_PARENT_TOP); // Align to the top of the parent
                    imageParams.topMargin = -50;
                    imageView.setLayoutParams(imageParams);

                    // Check if the room image bytes is not null
                    if (room.getRoomImage() != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(room.getRoomImage(), 0, room.getRoomImage().length);
                        // Set the Bitmap to the ImageView
                        imageView.setImageBitmap(bitmap);
                    }

                    roomDetailsLayout.addView(imageView);
                    cardView.addView(roomDetailsLayout);

                    //adding the on click listener to go to the booking activity page
                    button.setOnClickListener(v -> {
                        // Create a new Intent to start the BookingActivity
                        Intent intent = new Intent(ResultsActivity.this, BookingActivity.class);
                        // Pass the room object to the BookingActivity
                        intent.putExtra("room", room);
                        intent.putExtra("room_details", roomDetails);
                        intent.putExtra("image", room.getRoomImage());
                        // Start the BookingActivity
                        startActivity(intent);
                    });
                    resultsContainer.addView(cardView);
                }

            }
        } else {
            // Display a message if there are no rooms available
            TextView textView = new TextView(this);
            textView.setText("No rooms available");
            textView.setTextSize(24);
            resultsContainer.addView(textView);
        }
    }
}