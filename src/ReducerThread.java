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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
