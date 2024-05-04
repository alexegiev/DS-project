import entities.Request;
import entities.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ClientThread extends Thread{

    Request request;
    ArrayList<Integer> requestIds;

    ClientThread(Request request) {
        this.request = request;
    }

    ClientThread(Request request, ArrayList<Integer> requestIds) {
        this.request = request;
        this.requestIds = requestIds;
    }

    public void run() {

        //Initialize socket and input/output streams
        Socket requestSocket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        try {

            // create a socket to connect to the server on port 9090
            requestSocket = new Socket("localhost", 9090);

            // create the streams to send and receive data from server
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            //write object Request
            out.writeObject(request);
            out.flush();

            // Wait and receive data from Master
            Response response = (Response) in.readObject();

            //print the received results (from Request.toString())
            if(response.getAction().equals("Show Owned Rooms") || response.getAction().equals("Add Room Availability Date") || response.getAction().equals("Search Room")) {
                System.out.println(response.getResponse());
            }
            else if(response.getAction().equals("Add Room")) {
                System.out.println("Response: " + response.getResponse());
            }

        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");

        } catch (SocketException se) {
            System.err.println("Socket was closed unexpectedly!");

        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();

        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                if (requestSocket != null) {
                    requestSocket.close();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
