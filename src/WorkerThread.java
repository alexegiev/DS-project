import entities.Room;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class WorkerThread extends Thread{

    Socket masterSocket;

    public WorkerThread(Socket masterSocket){
        this.masterSocket = masterSocket;
    }

    public void run() {
        // Process data from Master
        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        try {

            out = new ObjectOutputStream(masterSocket.getOutputStream());
            in = new ObjectInputStream(masterSocket.getInputStream());

            Room testingRoom = (Room) in.readObject();
            testingRoom.incrementRoomIncrement();

            out.writeObject(testingRoom);
            out.flush();

            //TODO: REDUCER
//            // Connect to Reducer and send data
//            Socket reducerSocket = new Socket("localhost", 9092);
//            ObjectOutputStream reducerOut = new ObjectOutputStream(reducerSocket.getOutputStream());
//            ObjectInputStream reducerIn = new ObjectInputStream(reducerSocket.getInputStream());
//            reducerOut.writeObject(testingRoom);
//            reducerOut.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
