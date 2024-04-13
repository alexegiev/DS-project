import entities.Request;
import entities.Response;
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

            Response response = (Response) in.readObject();

            //TODO: Implement operations on Room object with map/reduce

            // Connect to ServerThread and send data
            Socket serverSocket = new Socket("localhost", 9093);
            ObjectOutputStream serverOut = new ObjectOutputStream(serverSocket.getOutputStream());
            ObjectInputStream serverIn = new ObjectInputStream(serverSocket.getInputStream());
            serverOut.writeObject(response);
            serverOut.flush();

        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            throw new RuntimeException(e);
        }
    }
}
