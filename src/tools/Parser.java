//package tools;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonParser;
//import entities.Room;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class Parser {
//
//    public JsonArray readJsonFile(String filePath) {
//        try (FileReader reader = new FileReader(filePath)) {
//            return JsonParser.parseReader(reader).getAsJsonArray();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public List<Room> parseJsonArray(JsonArray jsonArray) {
//        List<Room> rooms = new ArrayList<>();
//        Gson gson = new Gson();
//
//        for (JsonElement jsonElement : jsonArray) {
//            Room room = gson.fromJson(jsonElement, Room.class);
//            rooms.add(room);
//        }
//
//        return rooms;
//    }
//
//    public void storeData(List<Room> rooms) {
//        for (Room room : rooms) {
//            System.out.println(room);
//        }
//    }
//}