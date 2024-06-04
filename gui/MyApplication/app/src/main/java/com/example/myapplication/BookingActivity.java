package com.example.myapplication;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.myapplication.backend.entities.Room;
import android.widget.EditText;
import android.widget.DatePicker;
import android.widget.Button;
import java.util.Calendar;
import java.util.Date;


public class BookingActivity extends AppCompatActivity {

    Room room;
    String room_details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        // Get the Room object from the Intent
        room = (Room) getIntent().getSerializableExtra("room");
        room_details= getIntent().getStringExtra("room_details");
        TextView roomDetails = findViewById(R.id.chosen_room);
        roomDetails.setText(room_details);

        byte[] roomImageBytes = getIntent().getByteArrayExtra("image");
        Bitmap bitmap = BitmapFactory.decodeByteArray(roomImageBytes, 0, roomImageBytes.length);
        ImageView roomImageView = findViewById(R.id.room_image);
        roomImageView.setImageBitmap(bitmap);





    }
}
