package queries;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class GameClient {
    public static void main(String[] args) {
        // Datos iniciales de los robots
        String[] robotData = {
                "Robot1 Active Red 10 20",
                "Robot2 Active Blue 30 40",
        };

        // Crear un hilo para cada robot
        for (String data : robotData) {
            String[] parts = data.split("\\s+");
            String name = parts[0];
            int initialX = Integer.parseInt(parts[3]);
            int initialY = Integer.parseInt(parts[4]);

            new Thread(() -> {
                try (Socket socket = new Socket("localhost", 12345);
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                    int x = initialX;
                    int y = initialY;

                    while (true) {
                        // Enviar la información de posición al servidor
                        out.println(name + " " + x + " " + y);
                        Thread.sleep(500); // Esperar un segundo

                        // Actualizar la posición sumando 1 a las coordenadas X y Y
                        x++;
                        y++;
                    }

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

}
