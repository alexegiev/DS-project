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
        ObjectInputStream in = null;
        ObjectOutputStream out = null;

        try {

            // create a socket to connect to the server on port 9090
            requestSocket = new Socket("localhost", 9090);

            // create the streams to send and receive data from server
            in = new ObjectInputStream(requestSocket.getInputStream());
            out = new ObjectOutputStream(requestSocket.getOutputStream());

            // write object Room
            out.writeObject(roomTest);
            Room room = (Room) in.readObject();

            // print the received results (from Room.toString())
            System.out.println("Room: " + room.toString());

        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");

        } catch (IOException ioException) {
            ioException.printStackTrace();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        } finally {
            try {
                in.close();	out.close();
                requestSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public static void main(String args[]){

        for (int i = 0; i < 50; i++)
            new Client(new Room(i)).start();
    }
}
