package EJ4;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaHTTPServer implements Runnable {

    static final File WEB_ROOT = new File(".");// Directorio raíz del servidor web
    static final String DEFAULT_FILE = "index.html";// Archivo por defecto a servir
    static final String FILE_NOT_FOUND = "404.html";// Archivo a servir en caso de error 404
    static final String METHOD_NOT_SUPPORTED = "505.html";// Archivo a servir en caso de método no soportado
    static final int PORT = 5000;// Puerto en el que el servidor escucha
    static final boolean verbose = true;// Modo verbose para detalles de ejecución

    private final Socket connect;// Socket de conexión con el cliente

    public JavaHTTPServer(Socket c) {
        connect = c;
    }

    public static void main(String[] args) {
        try {
            // Creación del socket del servidor
            ServerSocket serverConnect = new ServerSocket(PORT);
            System.out.println("Servidor iniciado.\nEsperando conexiones en el puerto : " + PORT + " ...\n");

            while (true) {
                try {
                    // Aceptar la conexión de un cliente
                    JavaHTTPServer myServer = new JavaHTTPServer(serverConnect.accept());

                    if (verbose) {
                        System.out.println("Conexión iniciada. (" + new Date() + ")");
                    }

                    // Crear un nuevo hilo para manejar la solicitud del cliente
                    Thread thread = new Thread(myServer);
                    thread.start();
                } catch (IOException e) {
                    Logger.getLogger(JavaHTTPServer.class.getName()).log(Level.SEVERE, "Error aceptando la conexión", e);
                }
            }
        } catch (IOException e) {
            Logger.getLogger(JavaHTTPServer.class.getName()).log(Level.SEVERE, "Error al inicializar el servidor", e);
        }
    }

    @Override
    public void run() {
        String fileRequested;
        PrintWriter out = null;
        BufferedOutputStream dataOut = null;

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connect.getInputStream()))) {
            out = new PrintWriter(connect.getOutputStream());
            dataOut = new BufferedOutputStream(connect.getOutputStream());
            // Leer la primera línea de la solicitud del cliente
            String input = in.readLine();
            if (input == null) {
                throw new IOException("Client closed connection before sending data.");
            }
            StringTokenizer parse = new StringTokenizer(input);
            String method = parse.nextToken().toUpperCase();
            fileRequested = parse.nextToken().toLowerCase();

            int n_lin = 1;
            System.out.println(
                    "\n\n================ INICIO PETICIÓN =======================================\n" +
                            n_lin + "- " + input);

            // Si el método no es GET, HEAD o POST, se devuelve un error 501
            if (!method.equals("GET") && !method.equals("HEAD") && !method.equals("POST")) {
                if (verbose) {
                    System.out.println("501 Not Implemented : " + method + " method.");
                }

                File file = new File(WEB_ROOT, METHOD_NOT_SUPPORTED);
                int fileLength = (int) file.length();
                String contentMimeType = "text/html";
                byte[] fileData = readFileData(file, fileLength);

                out.println("HTTP/1.1 501 Not Implemented");
                out.println("Server: Java HTTP Server from SSaurel : 1.0");
                out.println("Date: " + new Date());
                out.println("Content-type: " + contentMimeType);
                out.println("Content-length: " + fileLength);
                out.println();
                out.flush();

                dataOut.write(fileData, 0, fileLength);
                dataOut.flush();
            } else {
                // Manejo de solicitudes POST
                if (method.equals("POST")) {
                    handlePostRequest(in, out, dataOut);
                    // Manejo de solicitudes GET
                } else if (method.equals("GET")) {
                    handleGetRequest(out, dataOut, fileRequested);
                }
            }

            muestraRestoPeticion(in);
            System.out.println("\n\n----- FIN PETICION ----\n");
        } catch (FileNotFoundException fnfe) {
            try {
                if (out != null && dataOut != null) {
                    fileNotFound(out, dataOut);
                }
            } catch (IOException ioe) {
                Logger.getLogger(JavaHTTPServer.class.getName()).log(Level.SEVERE, "Error con el archivo no encontrado", ioe);
            }
        } catch (IOException ioe) {
            Logger.getLogger(JavaHTTPServer.class.getName()).log(Level.SEVERE, "Error de servidor", ioe);
        } finally {
            try {
                connect.close();
                System.out.println("\n\nCerramos sockets ...");
            } catch (Exception e) {
                Logger.getLogger(JavaHTTPServer.class.getName()).log(Level.SEVERE, "Error cerrando sockets", e);
            }

            if (verbose) {
                System.out.println("Conexión cerrada.\n");
            }
        }
    }

    // Método para manejar archivos no encontrados (Error 404)
    private void fileNotFound(PrintWriter out, BufferedOutputStream dataOut) throws IOException {
        File file = new File(WEB_ROOT, FILE_NOT_FOUND);
        int fileLength = (int) file.length();
        String content = "text/html";
        byte[] fileData = readFileData(file, fileLength);

        out.println("HTTP/1.1 404 File Not Found");
        out.println("Server: Java HTTP Server from SSaurel : 1.0");
        out.println("Date: " + new Date());
        out.println("Content-type: " + content);
        out.println("Content-length: " + fileLength);
        out.println();
        out.flush();

        dataOut.write(fileData, 0, fileLength);
        dataOut.flush();

        if (verbose) {
            System.out.println("File not found.");
        }
    }

    // Método para manejar solicitudes GET
    private void handleGetRequest(PrintWriter out, BufferedOutputStream dataOut, String fileRequested) throws IOException {
        // Si se solicita un directorio, agregar el archivo por defecto
        if (fileRequested.endsWith("/")) {
            fileRequested += DEFAULT_FILE;
        }

        fileRequested = fileRequested.replace("/html/", "/");

        File file = new File(WEB_ROOT, fileRequested);
        int fileLength = (int) file.length();
        String content = getContentType(fileRequested);

        // Manejo específico para "formdatos.html"
        if (fileRequested.equals("formdatos.html")) {
            sendFormDataResponse(out, dataOut, "");
        } else {
            byte[] fileData = readFileData(file, fileLength);

            out.println("HTTP/1.1 200 OK");
            out.println("Server: Java HTTP Server from SSaurel : 1.0");
            out.println("Date: " + new Date());
            out.println("Content-type: " + content);
            out.println("Content-length: " + fileLength);
            out.println();
            out.flush();

            dataOut.write(fileData, 0, fileLength);
            dataOut.flush();
        }

        if (verbose) {
            System.out.println("File " + fileRequested + " of type " + content + " returned");
        }
    }

    // Método para manejar solicitudes POST
    private void handlePostRequest(BufferedReader in, PrintWriter out, BufferedOutputStream dataOut) throws IOException {
        StringBuilder requestBody = new StringBuilder();
        int contentLength = 0;

        // Leer encabezados para obtener la longitud del contenido
        String line;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            if (line.startsWith("Content-Length: ")) {
                contentLength = Integer.parseInt(line.substring("Content-Length: ".length()));
            }
        }

        // Leer el cuerpo de la solicitud POST
        for (int i = 0; i < contentLength; i++) {
            char c = (char) in.read();
            requestBody.append(c);
            System.out.print(c);
        }

        System.out.println("FormData: " + requestBody);
        sendFormDataResponse(out, dataOut, requestBody.toString());
    }

    // Método para extraer el valor de un campo específico del formulario
    private String extractFieldValue(String formData, String fieldName) {
        System.out.println("FormData: " + formData);

        String[] fields = formData.split("&");

        for (String field : fields) {
            String[] keyValue = field.split("=");

            if (keyValue.length == 2) {
                // Decodificar los valores ya que pueden estar codificados
                String decodedFieldName = java.net.URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                String decodedValue = java.net.URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);

                System.out.println("Decoded Field: " + decodedFieldName);
                System.out.println("Decoded Value: " + decodedValue);

                if (decodedFieldName.equals(fieldName)) {
                    System.out.println("Match! Returning: " + decodedValue);
                    return decodedValue;
                }
            }
        }
        return "";
    }

    // Método para obtener el contenido del archivo HTML con los datos del formulario
    private String getFileContentWithFormData(String formData) throws IOException {
        File file = new File(WEB_ROOT, "formdatos.html");
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("{{campo1}}")) {
                    line = line.replace("{{campo1}}", extractFieldValue(formData, "campo1"));
                }
                if (line.contains("{{campo2}}")) {
                    line = line.replace("{{campo2}}", extractFieldValue(formData, "campo2"));
                }
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    // Método para enviar la respuesta con los datos del formulario
    private void sendFormDataResponse(PrintWriter out, BufferedOutputStream dataOut, String formData) throws IOException {
        String modifiedContent = getFileContentWithFormData(formData);

        out.println("HTTP/1.1 200 OK");
        out.println("Server: Java HTTP Server from SSaurel : 1.0");
        out.println("Date: " + new Date());
        out.println("Content-type: text/html");
        out.println("Content-length: " + modifiedContent.length());
        out.println();
        out.flush();

        dataOut.write(modifiedContent.getBytes());
        dataOut.flush();
    }

    // Método para leer los datos de un archivo
    private byte[] readFileData(File file, int fileLength) throws IOException {
        byte[] fileData = new byte[fileLength];
        try (FileInputStream fileIn = new FileInputStream(file)) {
            fileIn.read(fileData);
        }
        return fileData;
    }

    // Método para obtener el tipo de contenido según la extensión del archivo
    private String getContentType(String fileRequested) {
        if (fileRequested.endsWith(".htm") || fileRequested.endsWith(".html")) {
            return "text/html";
        } else {
            return "text/plain";
        }
    }

    // Método para mostrar el resto de la petición
    private void muestraRestoPeticion(BufferedReader in) throws IOException {
        int car;
        int n_lin = 1;
        while (in.ready()) {
            car = in.read();
            if (car == '\n') {
                n_lin++;
                System.out.print("\n" + n_lin + "- ");
            } else {
                System.out.print((char) car);
            }
        }
    }
}
