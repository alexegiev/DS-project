import java.io.*;
import java.net.*;

import entities.Request;
import entities.Room;

public class Client extends Thread{

    //private fields

    Request request;

    Client(Request request) {
        this.request = request;
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

//            // write object Room
//            out.writeObject(request);
//            out.flush();

            // Wait and receive data from Master
            Request request1 = (Request) in.readObject();

//            // print the received results (from Room.toString())
//            System.out.println("Room: " + room.toString());

            //print the received results (from Request.toString())
            System.out.println("Request: " + request1.toString());

        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");

        } catch (SocketException se) {
            System.err.println("Socket was closed unexpectedly!");

        } catch (IOException ioException) {
            ioException.printStackTrace();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();

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

    public static void main(String args[]){

        //TESTING
        new Client(new Request(0,new Room("room1",1,1,"area1",1.5,5, "asdasdas"))).start();
    }
}