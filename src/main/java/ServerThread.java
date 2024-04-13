import entities.Request;
import entities.Response;
import entities.Room;
import entities.WorkerInfo;
import tools.Mapper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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

            // Break down the request to Room and RequestId
            Request request = (Request) inClient.readObject();
            Room testingRoom = request.getRoom();
            int requestId = request.getRequestId();

            // Get Mapper from Master
            Mapper mapper = Master.getMapper();

            // Get from mapper the worker that will process the request
            int workerId = mapper.mapRequest(testingRoom.getRoomId());

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

            // Connect to Reducer and receive data
            Socket reducerSocket = fromReducer.accept();
            outReducer = new ObjectOutputStream(reducerSocket.getOutputStream());
            inReducer = new ObjectInputStream(reducerSocket.getInputStream());

            Response response = (Response) inReducer.readObject();
            outClient.writeObject(response);
            outClient.flush();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
