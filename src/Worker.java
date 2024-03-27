import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Worker extends Thread{
    private Socket socket;

    public Worker(Socket socket) {
        this.socket = socket;

        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String message = in.readLine();
            System.out.println("Printed From Worker: Message from Master: " + message);
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }
}
