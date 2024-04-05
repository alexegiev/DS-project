import entities.Room;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread{

    static ServerSocket fromReducer = null;
    Socket clientSocket;

    public ServerThread(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        if (fromReducer == null) {
            fromReducer = new ServerSocket(9093);
        }
    }

    public void run(){
        // Process data from Worker
        ObjectOutputStream outClient = null;
        ObjectInputStream inClient = null;

        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        ObjectOutputStream outReducer = null;
        ObjectInputStream inReducer = null;

        try {
            outClient = new ObjectOutputStream(clientSocket.getOutputStream());
            inClient = new ObjectInputStream(clientSocket.getInputStream());
            Room testingRoom = (Room) inClient.readObject();

            //TODO: Connect to multiple workers

            // Connect to Worker and send data
            Socket workerSocket = new Socket("localhost", 9091);
            out = new ObjectOutputStream(workerSocket.getOutputStream());
            out.writeObject(testingRoom);
            out.flush();

            // Connect to Reducer and receive data
            Socket reducerSocket = fromReducer.accept();
            outReducer = new ObjectOutputStream(reducerSocket.getOutputStream());
            inReducer = new ObjectInputStream(reducerSocket.getInputStream());

            Room room = (Room) inReducer.readObject();
            outClient.writeObject(room);
            outClient.flush();

//            // Get data from Worker
//            in = new ObjectInputStream(workerSocket.getInputStream());
//            Room room = (Room) in.readObject();
//
//            // Send data to Client
//            outClient.writeObject(room);
//            outClient.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
