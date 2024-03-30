import entities.Room;

import java.io.*;
import java.net.*;

public class Worker extends Thread {

    //fields
    ObjectInputStream in;
    ObjectOutputStream out;
    private int sleepTime;
    private static final Object lock = new Object();

    public Worker(Socket connectionSocket) {

        // sleep between 0 to 3 seconds
        sleepTime =(int) (Math.random() * 3000);

        try{
            out = new ObjectOutputStream(connectionSocket.getOutputStream());
            in = new ObjectInputStream(connectionSocket.getInputStream());
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    public void run() {
        try{
            // change values ONLY FOR TESTING
            Room testingRoom = (Room) in.readObject();
            testingRoom.setTesting(true);
            testingRoom.incrementRoomIncrement();
            Thread.sleep(sleepTime);

            // Connect to Reducer and send data
            synchronized (lock) {
                Socket reducerSocket = new Socket("localhost", 9092);
                ObjectOutputStream reducerOut = new ObjectOutputStream(reducerSocket.getOutputStream());
                reducerOut.writeObject(testingRoom);
                reducerOut.flush();
            }

        } catch(IOException | ClassNotFoundException e){
            System.out.println("Error: " + e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        finally {
//            try {
//                in.close();
//                out.close();
//            } catch (IOException e) {
//                System.out.println("Error: " + e);
//            }
//        }



    }
}