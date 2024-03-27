import java.io.*;
import java.net.*;

public class ServerThread extends Thread {
    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;

        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String message = in.readLine();
            System.out.println("Message from Master: " + message);

            // send message to worker
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("Message from ServerThread");

            Worker worker = new Worker(socket);
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }
}