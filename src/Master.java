import java.io.IOException;
import java.net.*;

public class Master {

    private ServerSocket serverSocket;

    public ServerSocket connect(int port){
        try{
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port + " localhost" + port);
            return serverSocket;
        } catch (Exception e){
            System.out.println("Error: " + e);
            return null;
        }
    }





    public static void main(String[] args) {

        int port = 8080;
        Master master = new Master();

        try{
            ServerSocket serverSocket = master.connect(port);
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
            }
        } catch (IOException e) {
            System.out.println("Error: " + e);
            e.printStackTrace();
        }

    }
}