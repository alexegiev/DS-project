import entities.*;
import tools.Mapper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread{

    static ServerSocket fromReducer = null;
    Socket clientSocket;

    public ServerThread(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        if (fromReducer == null) {
            fromReducer = new ServerSocket(9093);
        }
    }

    public void run(){
        // Process data from Worker
        ObjectOutputStream outClient = null;
        ObjectInputStream inClient = null;

        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        ObjectOutputStream outReducer = null;
        ObjectInputStream inReducer = null;

        try {
            outClient = new ObjectOutputStream(clientSocket.getOutputStream());
            inClient = new ObjectInputStream(clientSocket.getInputStream());
            //Room testingRoom = (Room) inClient.readObject(); TESTING

            Request request = (Request) inClient.readObject();
            int requestId = request.getRequestId();
            System.out.println("Request ID: " + requestId + " received");

            //Create new ClientInfo object and save to clients List
            ClientInfo saveClientInfo = new ClientInfo(requestId, clientSocket);
            Master.clients.add(saveClientInfo);
            System.out.println("Client's Request ID: " + requestId + " connected ");

            // Check the Request's action to know if we need one worker or all
            if (request.getAction().equals("Show Owned Rooms") || request.getAction().equals("Add Room Availability Date") || request.getAction().equals("Search Room") || request.getAction().equals("Book Room") || request.getAction().equals("Add Rating")) {

                // Send the Request to all workers
                for (WorkerInfo workerInfo : Master.getWorkerSockets()) {
                    Socket connectedWorker = new Socket(workerInfo.getIp(), workerInfo.getPort());
                    out = new ObjectOutputStream(connectedWorker.getOutputStream());
                    in = new ObjectInputStream(connectedWorker.getInputStream());
                    out.writeObject(request);
                    out.flush();
                }
            } else if (request.getAction().equals("Logout")) {
                // Get from request the client's list of requestIds
                ArrayList<Integer> requestIds = request.getRequestIds();

                // For every requestId in the list, check if the same id exists in Master.clients
                // If it does, remove it from Master.clients
                for (int id : requestIds) {
                    for (ClientInfo clientInfo : Master.clients) {
                        if (clientInfo.getRequestId() == id) {
                            // close the socket
                            clientInfo.getClientSocket().close();
                            Master.clients.remove(clientInfo);
                            System.out.println("Client with Request ID: " + id + " logged out");
                            break;
                        }
                    }
                }
            }
            else{

                // Get Mapper from Master
                Mapper mapper = Master.getMapper();

                // Get from mapper the worker that will process the request
                int workerId = mapper.mapRequest(requestId);

                // Get WorkerInfo from List
                WorkerInfo workerInfo = Master.getWorkerSockets().get(workerId);

                // Print worker IP and socket
                System.out.println("Selected Worker:  IP: " + workerInfo.getIp() + " Port: " + workerInfo.getPort());

                // Connect to Worker and send data
                Socket connectedWorker = new Socket(workerInfo.getIp(), workerInfo.getPort());
                out = new ObjectOutputStream(connectedWorker.getOutputStream());
                in = new ObjectInputStream(connectedWorker.getInputStream());
                out.writeObject(request);
                out.flush();
            }



            // Connect to Reducer and receive data
            Socket reducerSocket = fromReducer.accept();
            outReducer = new ObjectOutputStream(reducerSocket.getOutputStream());
            inReducer = new ObjectInputStream(reducerSocket.getInputStream());

            Response response = (Response) inReducer.readObject();

            // Get the request ID from the response
            int responseId = response.getRequestId();

            // From Master.clients, get the client socket with the appropriate requestId
            for (ClientInfo clientInfo : Master.clients) {
                if (clientInfo.getRequestId() == responseId) {
                    outClient.writeObject(response);
                    outClient.flush();
                    break;
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
