package com.example.myapplication.backend.tools;
/*
* Purpose of Mapper is to:
*   Be called each time a client request is made
*   Using a Hash function H(reqId) = reqId % numberOfWorkers
*   Determine to which worker will the request be forward
*/
public class Mapper {

    // Fields
    private int numberOfWorkers;

    // Constructor
    public Mapper(int numberOfWorkers) {
        this.numberOfWorkers = numberOfWorkers;
    }

    // Methods
    public int mapRequest(int requestId) {
        if (numberOfWorkers == 0) {
            throw new RuntimeException("No workers available");
        }
        return requestId % numberOfWorkers;
    }

    // Getters and Setters
    public int getNumberOfWorkers() {
        return numberOfWorkers;
    }

    public void setNumberOfWorkers(int numberOfWorkers) {
        this.numberOfWorkers = numberOfWorkers;
    }

    public void increaseWorkers() {
        this.numberOfWorkers++;
    }
}