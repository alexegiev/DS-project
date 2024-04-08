package entities;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Manager extends Thread{
    public void run(){
        //Initialize socket and input/output streams
        Socket requestSocket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
    }

    public void addRoom(){
        // TODO
    }
    public void addRoomAvailabilityDate(){
        // TODO
    }
    public void showOwnedRooms(){
        // TODO
    }
}
