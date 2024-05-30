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
import com.example.myapplication.backend.Client;
import com.example.myapplication.backend.ClientThread;
import com.example.myapplication.backend.entities.Request;
import com.example.myapplication.backend.entities.Response;
import com.example.myapplication.backend.entities.Room;
import java.util.List;




public class ImageActivity extends AppCompatActivity {

    Spinner spinner;
    TextView userInput;
    Button searchButton, clearButton;
    String startDate, endDate;

    Client client = new Client();

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
                } else if (filter.equals("Capacity/People count") ||filter.equals("Price")||filter.equals("Rating")) { // handle the case when the filter is Capacity/People count,
                    userInput.setInputType(InputType.TYPE_CLASS_NUMBER);                                               // Price or Rating
                    userInput.setOnClickListener(null);
                    startDate = null;
                    endDate = null;
                }
        }

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

                if (userInputText.isEmpty()) {
                    Toast.makeText(ImageActivity.this, "Please enter some input before searching.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Create a new Request object
                Request request = new Request();
                request.setFilterType(filter);
                request.setFilterValue(userInputText);
                System.out.println(filter + userInputText);
                // Set the Request object in the Client
                client.setRequest(request);

                // Now you can add the filter type and value to the Request
                client.addRequestFilterType(filter);
                client.addRequestFilterValue(userInputText);
                System.out.println("User input textview: " + userInput);
                System.out.println("User input string: " + userInputText);


                // Check the filter type and perform the search
                if (filter.equals("Area") || filter.equals("Room Name")) {
                    Toast.makeText(ImageActivity.this, "Searching for " + filter + " with value: " + userInputText, Toast.LENGTH_SHORT).show();

                } else if (filter.equals("Dates")) {// Split the user input by the "-" character
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
                }
                else if (filter.equals("Capacity/People count") || filter.equals("Price") || filter.equals("Rating")) {
                    if (userInputText.matches("\\d+")) {
                        int userInputInt = Integer.parseInt(userInputText);
                        if(userInputInt < 0){
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
                               System.out.println("Response in Image activity: " + response);


                               // Start the ResultsActivity
                               startActivity(intent);
                           }
                       });



                    }
                }).start();


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