package entities;

import java.net.InetAddress;
import java.net.Socket;

public class WorkerInfo {
    InetAddress ip;
    int port;
    Socket socket;

    public WorkerInfo(InetAddress ip, int port, Socket socket) {
        this.ip = ip;
        this.port = port;
        this.socket = socket;
    }
}