package server;

import java.io.*;
import java.net.*;

import org.json.JSONObject;


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

                // Iniciar un hilo para manejar la conexión con el cliente
                Thread clientThread = new Thread(() -> handleClient(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // private void handleClient(Socket clientSocket) {
    // try {
    // BufferedReader in = new BufferedReader(new
    // InputStreamReader(clientSocket.getInputStream()));

    // String inputLine;
    // while ((inputLine = in.readLine()) != null) {
    // System.out.println("DBServer: Received message from client: " + inputLine);
    // // Aquí puedes procesar el mensaje recibido según sea necesario
    // }

    // clientSocket.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }

    private void handleClient(Socket clientSocket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("DBServer: Received message from client: " + inputLine);

                // Convertir el JSON recibido en un objeto JSONObject
                JSONObject jsonObject = new JSONObject(inputLine);

                // Obtener los valores del objeto JSONObject
                int robotId = jsonObject.getInt("robotId");
                int robotType = jsonObject.getInt("robotType");
                boolean isTurnedOn = jsonObject.getBoolean("isTurnedOn");

                // Aquí puedes procesar los valores según sea necesario
                System.out.println("Robot ID: " + robotId);
                System.out.println("Robot Type: " + robotType);
                System.out.println("Robot is turned on: " + isTurnedOn);
            }

            clientSocket.close();
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
