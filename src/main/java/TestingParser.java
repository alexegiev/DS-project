import entities.Room;
import tools.Parser;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import java.util.List;


public class TestingParser {

    public static void main(String[] args) {
        Parser parser = new Parser();
        String filePath = "src/main/java/static/manager1.json";
        JsonArray jsonArray = parser.readJsonFile(filePath);
        List<Room> rooms = parser.parseJsonArray(jsonArray);

        for (Room room : rooms) {
            System.out.println(room.toString());
        }
    }
}
