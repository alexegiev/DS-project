import entities.Room;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Reducer {

    ServerSocket reducerSocket = null;
    Socket masterSocket = null;

    ObjectInputStream in;
    ObjectOutputStream out;

    public Reducer(){
        try {
            reducerSocket = new ServerSocket(9092);
            while (true) {

                System.out.println("Waiting for worker request");
                Socket workerSocket = reducerSocket.accept();
                System.out.println("Connected to worker "
                        + workerSocket.getInetAddress().getHostAddress());

                // Process data from Worker
                out = new ObjectOutputStream(workerSocket.getOutputStream());
                in = new ObjectInputStream(workerSocket.getInputStream());
                Room testingRoom = (Room) in.readObject();
                testingRoom.incrementRoomIncrement();

                // Connect to Master and send data
                masterSocket = new Socket("localhost", 9090);
                System.out.println("Connected to Master "
                        + masterSocket.getInetAddress().getHostAddress());
                out = new ObjectOutputStream(masterSocket.getOutputStream());
                out.writeObject(testingRoom);
                out.flush();
            }
        }
        catch(Exception e){
            System.out.println("Error: " + e);
            return;
        }
    }

    public static void main(String[] args){
        new Reducer();
    }


}
