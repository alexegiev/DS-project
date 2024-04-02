import java.io.*;
import java.net.*;

import entities.Room;

public class Client extends Thread{

    //private fields

    Room roomTest;

    Client(Room room) {
        this.roomTest = room;
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

            // write object Room
            out.writeObject(roomTest);
            out.flush();

            // Wait and receive data from Master
            Room room = (Room) in.readObject();

            // print the received results (from Room.toString())
            System.out.println("Room: " + room.toString());

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

        // TODO: Implement the client(Menu options, Manager or Renter, operations, etc.)

    }
}