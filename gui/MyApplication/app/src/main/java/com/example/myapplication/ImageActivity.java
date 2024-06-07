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
import com.example.myapplication.backend.*;
import com.example.myapplication.backend.entities.*;


public class ImageActivity extends AppCompatActivity {

    Spinner spinner;
    TextView userInput;
    Button searchButton, clearButton;
    String startDate, endDate;

    Client client = new Client();

    //Showing the date picker and getting the date from the user
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
                        if (startDate == null) { //setting the start date
                            startDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            userInput.setText(startDate);
                        } else {  //setting the end date and setting the format of the user input for the dates
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

        //setting the spinner adapter for the filter options
       ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        R.array.filter_options, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String filter = parent.getItemAtPosition(position).toString();

                if (filter.equals("Area")|| filter.equals("Room Name")) {  // handle the case when the filter is Area or Room Name
                    userInput.setInputType(InputType.TYPE_CLASS_TEXT);
                    userInput.setOnClickListener(null);
                    startDate = null;
                    endDate = null;
                } else if (filter.equals("Dates")) { //handle the case when the filter is Dates because we need to show the date picker dialog
                    userInput.setInputType(InputType.TYPE_NULL);
                    userInput.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDatePickerDialog();
                        }
                    });
                } else if (filter.equals("Capacity/People count") ||filter.equals("Price")||filter.equals("Rating")) { // handle the case when the filter is
                    userInput.setInputType(InputType.TYPE_CLASS_NUMBER);                                               // Capacity/People count,
                    userInput.setOnClickListener(null);                                                                // Price or Rating
                    startDate = null;
                    endDate = null;
                }
        }
            //handle the case when the user does not select any filter and prompt the user to select one before searching
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                searchButton.setEnabled(false);
                Toast.makeText(ImageActivity.this, "You cannot search without choosing a filter!", Toast.LENGTH_SHORT).show();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filter = spinner.getSelectedItem().toString();
                String userInputText = userInput.getText().toString();

                // Check if the user input is empty and prompt the user to enter some input before searching
                if (userInputText.isEmpty()) {
                    Toast.makeText(ImageActivity.this, "Please enter some input before searching.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Create a new Request object
                Request request = new Request();
                request.setAction("Search Room");
                request.setFilterType(filter);
                request.setFilterValue(userInputText);
                System.out.println(filter + userInputText);
                // Set the Request object in the Client
                client.setRequest(request);


                // Check the filter type and perform the search
                if (filter.equals("Area") || filter.equals("Room Name")) {  //case when the filter is Area or Room Name
                    Toast.makeText(ImageActivity.this, "Searching for " + filter + " with value: " + userInputText, Toast.LENGTH_SHORT).show();

                } else if (filter.equals("Dates")) { // case when the filter is Dates
                    String[] dates = userInputText.split(" - "); // Split the user input by the "-" character to check validity
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
                        if (date1.after(date2)) {
                            Toast.makeText(ImageActivity.this, "The second date should be after the first date.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (ParseException e) {
                        Toast.makeText(ImageActivity.this, "Invalid date format. Please use dd/MM/yyyy - dd/MM/yyyy.", Toast.LENGTH_SHORT).show();
                    }
                }
                // case when filter is Capacity/People Count, Price, Rating
                else if (filter.equals("Capacity/People count") || filter.equals("Price") || filter.equals("Rating")) {
                    if (userInputText.matches("\\d+")) { // check if input is valid
                        int userInputInt = Integer.parseInt(userInputText);
                        if(userInputInt < 0){ // checking if input is >0
                            Toast.makeText(ImageActivity.this, "Please enter a positive number", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        Toast.makeText(ImageActivity.this, "Invalid number format", Toast.LENGTH_SHORT).show();
                    }
                }



                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Response response = client.searchRoom(filter, userInputText);


                        // Use runOnUiThread to update the UI on the main thread
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               Intent intent = new Intent(ImageActivity.this, ResultsActivity.class);
                               intent.putExtra("response", response);

                               // Start the ResultsActivity
                               startActivity(intent);
                           }
                       });
                    }
                }).start();
            }
        });

        // clear button to clear all user input
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