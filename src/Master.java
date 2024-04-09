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
    ArrayList<WorkerInfo> workers = new ArrayList<WorkerInfo>();
    ArrayList<Socket> workerSockets = new ArrayList<>();

    public static void main(String[] args){

        // TODO: Parsing JSON contains rooms
        new Master().startServer();
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
                workerSockets.add(newWorkerSocket);
                System.out.println("Worker No. " + workerSockets.size() + " connected ");

                // Get the IP and Port of the Worker
                ObjectOutputStream out = new ObjectOutputStream(newWorkerSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(newWorkerSocket.getInputStream());
                InetAddress workerIp = (InetAddress) in.readObject();
                int workerPort = (int) in.readObject();

                // Store the Worker's information
                workers.add(new WorkerInfo(workerIp, workerPort, newWorkerSocket));

                System.out.println("Worker's IP: " + workerIp + " Worker's Port: " + workerPort);
            }
            catch(Exception e){
                System.out.println("Error: " + e);
                e.printStackTrace();
                return;
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
}

class WorkerInfo {
    InetAddress ip;
    int port;
    Socket socket;

    WorkerInfo(InetAddress ip, int port, Socket socket) {
        this.ip = ip;
        this.port = port;
        this.socket = socket;
    }
}