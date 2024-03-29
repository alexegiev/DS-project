import entities.Room;

import java.io.*;
import java.net.*;

public class Worker extends Thread {

    //fields
    ObjectInputStream in;
    ObjectOutputStream out;
    private int sleepTime;

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
            Thread.sleep(sleepTime);

            //write data and flush
            out.writeObject(testingRoom);
            out.flush();

        } catch(IOException | ClassNotFoundException e){
            System.out.println("Error: " + e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                System.out.println("Error: " + e);
            }
        }



    }
}