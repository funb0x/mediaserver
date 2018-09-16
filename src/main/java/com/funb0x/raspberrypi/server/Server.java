package com.funb0x.raspberrypi.server;

import com.funb0x.webcam.image.WebcamImagePublisher;
import com.funb0x.webcam.image.listener.impl.SocketImageListenerImpl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static final ExecutorService executorService = Executors.newFixedThreadPool(20);

    public static void main(String[] args) {
        new Server().run();
    }

    public void run() {
        int port = 8080;
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        try {
            while (true) {
                Socket socket = serverSocket.accept();
                WebcamImagePublisher.getInstance().subscribe(new SocketImageListenerImpl(socket));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error accepting connection on port: " + port);
        }
    }
}

