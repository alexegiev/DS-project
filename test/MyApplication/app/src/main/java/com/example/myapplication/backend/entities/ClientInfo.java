package com.example.myapplication.backend.entities;

import java.net.Socket;

public class ClientInfo {

    int requestId;
    Socket clientSocket;

    public ClientInfo(int requestId, Socket clientSocket) {
        this.requestId = requestId;
        this.clientSocket = clientSocket;
    }

    public int getRequestId() {
        return requestId;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
}
