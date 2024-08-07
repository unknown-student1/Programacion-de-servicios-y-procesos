package EJ4;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HTTPCliente {

    private final String server;  // Nombre del servidor
    private final int port;       // Puerto del servidor

    public HTTPCliente(String server, int port) {
        this.server = server;
        this.port = port;
    }

    // Método para enviar una solicitud POST con datos de formulario
    public void sendPostRequest(Map<String, String> formData) throws IOException {
        try (Socket socket = new Socket(server, port);  // Crear el socket para la conexión
             PrintWriter out = new PrintWriter(socket.getOutputStream());  // Salida para enviar datos
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {  // Entrada para recibir datos

            // Construir el cuerpo de la solicitud con los datos del formulario
            String requestBody = formData.entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .reduce((a, b) -> a + "&" + b)
                    .orElse("");

            // Escribir la solicitud POST al servidor
            out.println("POST / HTTP/1.1");
            out.println("Host: " + server);
            out.println("Content-Type: application/x-www-form-urlencoded");
            out.println("Content-Length: " + requestBody.getBytes(StandardCharsets.UTF_8).length);
            out.println();
            out.println(requestBody);
            out.flush();

            // Leer y mostrar la respuesta del servidor
            String responseLine;
            while ((responseLine = in.readLine()) != null) {
                System.out.println(responseLine);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        // Crear instancia del cliente HTTP
        HTTPCliente client = new HTTPCliente("localhost", 5000);

        // Preparar los datos del formulario
        Map<String, String> formData = new HashMap<>();
        formData.put("campo1", "value1");
        formData.put("campo2", "value2");

        // Enviar la solicitud POST
        client.sendPostRequest(formData);

        // Abrir el navegador predeterminado en la URL del servidor
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            //new ProcessBuilder("Arc.exe", "http://localhost:5000").start();
            new ProcessBuilder("cmd", "/c", "start", "msedge", "http://localhost:5000").start();
        }
    }
}