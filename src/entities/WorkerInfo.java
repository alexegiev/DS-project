package entities;

import java.net.InetAddress;

public class WorkerInfo {
    private InetAddress ip;
    private int port;

    public WorkerInfo(InetAddress ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public InetAddress getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}