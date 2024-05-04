package entities;

import java.net.InetAddress;

public class WorkerInfo {
    private String ip;
    private int port;
    InetAddress localIp;

    public WorkerInfo(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    // for local testing
    public WorkerInfo(InetAddress localIp, int port) {
        this.localIp = localIp;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}