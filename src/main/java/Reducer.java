import entities.Room;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Reducer {

    ServerSocket reducer = null;

    private static int workerCount = 1;

    public static void main(String[] args) {
        new Reducer();
    }

    public Reducer() {
        try {
            reducer = new ServerSocket(9092);
            System.out.println("Reducer started at port: " + reducer.getLocalPort());
            waitForWorker();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void waitForWorker() {

        while (true) {
            try {
                System.out.println("Waiting for Worker request");
                Socket workerSocket = reducer.accept();
                System.out.println("Connected to Worker: " + workerCount + " Times");
                workerCount++;

                // create a new WorkerThread object
                ReducerThread reducerThread = new ReducerThread(workerSocket);
                reducerThread.start();
            } catch (IOException e) {
                System.out.println("Error: " + e);
            }
        }
    }
}


//    public Reducer() {
//        try {
//            reducer = new ServerSocket(9092);
//            System.out.println("Reducer started at port: " + reducer.getLocalPort());
//            waitForWorker();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    void waitForWorker() {
//        while (true) {
//            try {
//
//
//                // Wait for Worker to connect
//                System.out.println("Waiting for Worker request");
//                workerSocket = reducer.accept();
//                System.out.println("Connected to Worker");
//
//                // read data from Worker
//                outWorker = new ObjectOutputStream(workerSocket.getOutputStream());
//                inWorker = new ObjectInputStream(workerSocket.getInputStream());
//                Room room = (Room) inWorker.readObject();
//                System.out.println("Room: " + room.toString());
//
////                // Connect to Master and send data
////                masterSocket = new Socket("localhost", 9090);
////                outMaster = new ObjectOutputStream(masterSocket.getOutputStream());
////                inMaster = new ObjectInputStream(masterSocket.getInputStream());
////                outMaster.writeObject(room);
////                outMaster.flush();
//
//            } catch (Exception e) {
//                System.out.println("Error: " + e);
//            }
//        }
//      }
//
