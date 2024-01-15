package com.hit.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static boolean serverUp = true;
    private static ExecutorService pool = Executors.newFixedThreadPool(4);

    public Server() {
    }

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(12345);

            while(serverUp) {
                Socket someClient = server.accept();
                (new Thread(new HandleRequest(someClient))).start();
            }

            server.close();
        } catch (Exception var3) {
            System.out.println("Connection timed out");
        }

    }
}