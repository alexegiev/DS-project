import entities.Room;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerThread extends Thread{

    Socket clientSocket;

    public ServerThread(Socket clientSocket){
        this.clientSocket = clientSocket;
    }

    public void run(){
        // Process data from Worker
        ObjectOutputStream outClient = null;
        ObjectInputStream inClient = null;

        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        try {
            outClient = new ObjectOutputStream(clientSocket.getOutputStream());
            inClient = new ObjectInputStream(clientSocket.getInputStream());
            Room testingRoom = (Room) inClient.readObject();
            testingRoom.incrementRoomIncrement();

            // Connect to Worker and send data
            Socket workerSocket = new Socket("localhost", 9091);
            out = new ObjectOutputStream(workerSocket.getOutputStream());
            out.writeObject(testingRoom);
            out.flush();

            // Get data from Worker
            in = new ObjectInputStream(workerSocket.getInputStream());
            Room room = (Room) in.readObject();

            // Send data to Client
            outClient.writeObject(room);
            outClient.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
