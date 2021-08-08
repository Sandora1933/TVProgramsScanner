package com.example.tvprogramsscanner.other;

import com.example.tvprogramsscanner.service.RequestService;

import java.io.IOException;

public class MyRunnable implements Runnable {

    RequestService requestService;
    String serialNumber;
    int borderId, direction;

    String jsonResponseString = null;

    public MyRunnable(RequestService service, String serialNumber, int borderId, int direction){
        this.requestService = service;
        this.serialNumber = serialNumber;
        this.borderId = borderId;
        this.direction = direction;
    }

    @Override
    public void run() {
        try {
            requestService.makeRequest(serialNumber, borderId, direction);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getJsonResponseString() {
        return jsonResponseString;
    }
}
