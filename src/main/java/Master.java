import entities.WorkerInfo;
import tools.Mapper;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

/*
*
* This is the Master class of the Distributed System
* It's main purpose is to
*   Listen for incoming Client connections
*   Send the data of the Client to the Worker class
*   Accept data from Reducer class
*   Send data from the Reducer class, to the Client
*/
public class Master {

    // Define socket that receives incoming requests
    ServerSocket server = null;

    // Define socket that receives info for Worker
    ServerSocket worker = null;
    Socket workerSocket = null;

    // Define socket that is used to handle the connection
    Socket clientSocket = null;
    Socket reducerSocket = null;

    ObjectOutputStream clientOut = null;
    ObjectInputStream clientIn = null;

    private static int clientCount = 1;
    private static int workerCount = 1;

    // List that contains all connected Workers
    static ArrayList<WorkerInfo> workers = new ArrayList<>();

    // Mapper object
    static Mapper mapper = null;

    public Master() {
        mapper= new Mapper(0);
        startServer();
    }

    public static Mapper getMapper() {
        return mapper;
    }

    public static ArrayList<WorkerInfo> getWorkerSockets() {
        return workers;
    }

    void startServer() {
        try {
            server = new ServerSocket(9090);
            worker = new ServerSocket(9095);
            System.out.println("Server started at port: " + server.getLocalPort());

            // Start separate threads for accepting workers and clients
            new Thread(this::waitForWorker).start();
            new Thread(this::waitForClient).start();
        }
        catch (SocketException e) {
            e.printStackTrace();
        }
        catch(IOException e){
            System.out.println("Error: " + e);
            return;
        }
    }

    void waitForWorker() {
        while (true) {
            try {
                // Functionality for Workers requests
                Socket newWorkerSocket = worker.accept();

                // Create a new ObjectInputStream object
                ObjectOutputStream out = new ObjectOutputStream(newWorkerSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(newWorkerSocket.getInputStream());

                // Store the IP of Worker
                InetAddress workerIp = (InetAddress) in.readObject();

                // Store the Port of Worker
                int workerPort = (int) in.readObject();

                // Create WorkerInfo object and save to workers List
                WorkerInfo workerInfo = new WorkerInfo(workerIp, workerPort);
                workers.add(workerInfo);

                System.out.println("Worker's IP: " + workerIp + " Port: " + workerPort);

                System.out.println("Worker No. " + workers.size() + " connected ");
                mapper.increaseWorkers();

            }
            catch(Exception e){
                System.out.println("Error: " + e);
                e.printStackTrace();
                return;
            } finally {
                try {
                    if (workerSocket != null) {
                        workerSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void waitForClient() {
        while (true) {
            try {
                // Functionality for Client requests
                System.out.println("Waiting for client request");
                clientSocket = server.accept();
                System.out.println("Client No. " + clientCount + " connected ");
                clientCount++;

                // create a new ServerThread object
                ServerThread serverThread = new ServerThread(clientSocket);
                serverThread.start();
            }
            catch(Exception e){
                System.out.println("Error: " + e);
                e.printStackTrace();
                return;
            }
        }
    }

    public static void main(String[] args){

        Master master = new Master();
    }
}