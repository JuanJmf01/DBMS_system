package server;

import java.io.*;
import java.net.*;

public class DbServer {
    private ServerSocket serverSocket;

    public DbServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        System.out.println("DBServer: Waiting for client connection...");
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("DBServer: Client connected.");

                new Thread(() -> {
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            System.out.println("DBServer: Received message from client: " + inputLine);
                            // Procesar solicitud y responder
                            out.println("DBServer: Message received.");
                        }

                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}