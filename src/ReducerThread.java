import entities.Room;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ReducerThread extends Thread {

    //fields

    Socket workerSocket;

    public ReducerThread(Socket workerSocket) {
        this.workerSocket = workerSocket;
    }

    public void run(){

        // Process data from Worker
        ObjectOutputStream out;
        ObjectInputStream in;

        try{
            out = new ObjectOutputStream(workerSocket.getOutputStream());
            in = new ObjectInputStream(workerSocket.getInputStream());

            Room testingRoom = (Room) in.readObject();
            testingRoom.incrementRoomIncrement();
            testingRoom.setReceivedFromReducer(true);

            // Connect to ServerThread and send data
            Socket serverSocket = new Socket("localhost", 9093);
            ObjectOutputStream serverOut = new ObjectOutputStream(serverSocket.getOutputStream());
            ObjectInputStream serverIn = new ObjectInputStream(serverSocket.getInputStream());
            serverOut.writeObject(testingRoom);
            serverOut.flush();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}