package tools;

import entities.Room;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
public class Parser {

    public JsonArray readJsonFile(String filePath) {
        try (JsonReader reader = Json.createReader(Files.newBufferedReader(Paths.get(filePath).toAbsolutePath()))) {
            return reader.readArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Room> parseJsonArray(JsonArray jsonArray) {
        List<Room> rooms = new ArrayList<>();

        for (JsonValue jsonValue : jsonArray) {
            JsonObject jsonObject = jsonValue.asJsonObject();
            // Assuming Room has a constructor that accepts all necessary parameters
            Room room = new Room(
                    jsonObject.getString("roomName"),
                    jsonObject.getInt("roomId"),
                    jsonObject.getInt("managerId"),
                    jsonObject.getString("area"),
                    jsonObject.getJsonNumber("rating").doubleValue(),
                    jsonObject.getInt("numberOfReviews"),
                    jsonObject.getString("roomImage")
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