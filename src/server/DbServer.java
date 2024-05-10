package server;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;

import org.json.JSONObject;

import tables.LogEvent;
import tables.Robot;
import database.CSVEventHandler;
import database.CSVLogEventHandler;
import database.CSVRobotHandler;
import utils.GenerateId;

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

                // Iniciar un hilo para manejar la conexión con el cliente
                Thread clientThread = new Thread(() -> handleClient(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                // ("DBServer: Received message from client: " + inputLine);

                // Convert received JSON string to JSON object
                JSONObject jsonObject = new JSONObject(inputLine);

                // Get the values ​​of the JSONObject object
                String tableName = jsonObject.getString("tableName");

                // Por que no esta entrando a estos condicionales?
                if (tableName.equals("robots.csv")) {
                    handleRobotQueries(jsonObject, tableName);
                } else if (tableName.equals("logEvents.csv")) {
                    handleLogEventsQueries(jsonObject, tableName);
                } else if (tableName.equals("events.csv")) {
                    handleEventsQueries(jsonObject, tableName);

                }

            }
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleRobotQueries(JSONObject jsonObject, String tableName) {
        // int robotId = jsonObject.getInt("robotId");
        int robotType = jsonObject.getInt("robotType");
        boolean isTurnedOn = jsonObject.getBoolean("isTurnedOn");

        // Get next id
        int nextRobotId = GenerateId.getLastId("robots.csv") + 1;

        // Create instance of Roboy and CSVRobotHandler
        Robot nuevoRobot = new Robot(nextRobotId, robotType, isTurnedOn);
        CSVRobotHandler CSVRobotHandler = new CSVRobotHandler();

        try {
            CSVRobotHandler.saveRobot(nuevoRobot, tableName);
            // System.out.println("Robot successfully saved to CSV file.");

        } catch (IOException e) {
            System.out.println("Error saving robot: " + e.getMessage());
        }
    }

    public void handleLogEventsQueries(JSONObject jsonObject, String tableName) {
        int robotId = jsonObject.getInt("robotId");
        int avenue = jsonObject.getInt("avenue");
        int street = jsonObject.getInt("street");
        int sirens = jsonObject.getInt("sirens");

        // Create instance of Roboy and CSVRobotHandler
        LogEvent newLogEvent = new LogEvent(robotId, LocalDateTime.now(), avenue, street, sirens);
        CSVLogEventHandler CSVLogEventHandler = new CSVLogEventHandler();

        try {
            CSVLogEventHandler.saveEvent(newLogEvent, tableName);
            // System.out.println("Event successfully saved to CSV file.");

        } catch (IOException e) {
            System.out.println("Error saving robot: " + e.getMessage());
        }
    }

    public void handleEventsQueries(JSONObject jsonObject, String tableName) {
        int robotId = jsonObject.getInt("robotId");
        int avenue = jsonObject.getInt("avenue");
        int street = jsonObject.getInt("street");
        int sirens = jsonObject.getInt("sirens");

        // Create instance of Roboy and CSVRobotHandler
        LogEvent newLogEvent = new LogEvent(robotId, LocalDateTime.now(), avenue, street, sirens);
        CSVEventHandler CSVEventHandler = new CSVEventHandler();
        try {
            CSVEventHandler.saveAndUpdateEvent(newLogEvent, tableName);
            // System.out.println("Event successfully saved to CSV file.");
        } catch (IOException e) {
            System.out.println("Error saving robot: " + e.getMessage());
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
