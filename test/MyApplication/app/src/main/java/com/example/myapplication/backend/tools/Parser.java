package com.example.myapplication.backend.tools;

import com.example.myapplication.backend.entities.Room;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Parser {

    public JsonArray readJsonFile(String filePath) {
        String correctPath = filePath.replace("static", "com\\example\\myapplication\\backend\\static");
        try (JsonReader reader = Json.createReader(Files.newBufferedReader(Paths.get(correctPath).toAbsolutePath()))) {
            return reader.readArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] getImageBytes(String imageName) {
        try {
            String filePath = "src\\main\\java\\com\\example\\myapplication\\backend\\static\\" + imageName;
            Path path = Paths.get(filePath);
            return Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Room> parseJsonArray(JsonArray jsonArray) {
        List<Room> rooms = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); // Adjust this to match your date format

        for (JsonValue jsonValue : jsonArray) {
            JsonObject jsonObject = jsonValue.asJsonObject();

            // Parse the availableDates key
            JsonObject availableDatesJson = jsonObject.getJsonObject("availableDates");
            Map<Date, Date> availableDates = new HashMap<>();
            for (String key : availableDatesJson.keySet()) {
                try {
                    Date keyDate = format.parse(key);
                    Date valueDate = format.parse(availableDatesJson.getString(key));
                    availableDates.put(keyDate, valueDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            // Get the image bytes
            byte[] imageBytes = getImageBytes(jsonObject.getString("roomImage"));

            // Assuming Room has a constructor that accepts all necessary parameters
            Room room = new Room(
                    jsonObject.getString("roomName"),
                    jsonObject.getInt("roomId"),
                    jsonObject.getString("managerUsername"),
                    jsonObject.getString("area"),
                    jsonObject.getJsonNumber("rating").doubleValue(),
                    jsonObject.getInt("numberOfReviews"),
                    jsonObject.getInt("capacity"),
                    jsonObject.getInt("price"),
                    imageBytes,
                    availableDates  // Add the availableDates map to the Room constructor
            );
            rooms.add(room);
        }

        return rooms;
    }

    public void storeData(List<Room> rooms) {
        for (Room room : rooms) {
            System.out.println(room);
        }
    }
}