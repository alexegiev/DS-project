package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.text.InputType;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.Intent;


public class ImageActivity extends AppCompatActivity {

    Spinner spinner;
    TextView userInput;
    Button searchButton, clearButton;
    String startDate, endDate;

    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                ImageActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (startDate == null) {
                            startDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            userInput.setText(startDate);
                        } else {
                            endDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            userInput.setText(startDate + " - " + endDate);
                        }
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        spinner = findViewById(R.id.spinner);
        userInput = findViewById(R.id.userInput);
        searchButton = findViewById(R.id.button);
        clearButton = findViewById(R.id.clearButton);

       ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        R.array.filter_options, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String filter = parent.getItemAtPosition(position).toString();
                switch (filter) {
                    case "Area":
                        //handle the area filter
                        userInput.setInputType(InputType.TYPE_CLASS_TEXT);
                        userInput.setOnClickListener(null); // Remove the OnClickListener

                        startDate = null;
                        endDate = null;
                        break;
                    case "Dates":
                        userInput.setInputType(InputType.TYPE_NULL); // Remove the keyboard
                        userInput.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showDatePickerDialog();
                            }
                        });
                        break;
                    case "Capacity/People count":
                        //handle the capacity/people count filter
                        userInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                        userInput.setOnClickListener(null); // Remove the OnClickListener
                        startDate = null;
                        endDate = null;
                        break;
                    case "Price":
                        //handle the price filter
                        userInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                        userInput.setOnClickListener(null); // Remove the OnClickListener
                        startDate = null;
                        endDate = null;
                        break;
                    case "Room Name":
                        //handle the room name filter
                        userInput.setInputType(InputType.TYPE_CLASS_TEXT);
                        userInput.setOnClickListener(null); // Remove the OnClickListener
                        startDate = null;
                        endDate = null;
                        break;
                    case "Rating":
                        //handle the rating filter
                        userInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                        userInput.setOnClickListener(null); // Remove the OnClickListener
                        startDate = null;
                        endDate = null;
                        break;
                }
        }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no spinner filter selected here
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filter = spinner.getSelectedItem().toString();
                String userInputText = userInput.getText().toString();

                if ("Dates".equals(filter)) {
                    // Split the user input by the "-" character
                    String[] dates = userInputText.split(" - ");
                    if (dates.length != 2) {
                        Toast.makeText(ImageActivity.this, "Invalid date range. Please use dd/MM/yyyy - dd/MM/yyyy.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Parse the user input as dates
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                    try {
                        Date date1 = dateFormat.parse(dates[0]);
                        Date date2 = dateFormat.parse(dates[1]);

                        // Check if the second date is after the first date
                        if (date2.after(date1)) {
                            // Use the dates to perform the search
                            // ...
                        } else {
                            Toast.makeText(ImageActivity.this, "The second date should be after the first date.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (ParseException e) {
                        Toast.makeText(ImageActivity.this, "Invalid date format. Please use dd/MM/yyyy - dd/MM/yyyy.", Toast.LENGTH_SHORT).show();
                    }
                } else if ("Room Name".equals(filter)) {
                    // Use the room number to perform the search
                    // ...
                }

                // Create an Intent to start ResultsActivity
                Intent intent = new Intent(ImageActivity.this, ResultsActivity.class);

                // If you want to pass data to ResultsActivity, you can put it as extra
                // For example, if you want to pass the user input text
                intent.putExtra("userInput", userInput.getText().toString());

                // Start the ResultsActivity
                startActivity(intent);
            }

        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInput.setText("");
                startDate = null;
                endDate = null;
            }
        });
    }
}