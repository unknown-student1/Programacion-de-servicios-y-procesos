package EJ3;

import java.io.*;
import java.net.*;
import java.util.Arrays;

public class Servidor {
    private static final String ubicacion = "src/EJ3/";

    public static void main(String[] args) {
        try {
            ServerSocket srvSocket = new ServerSocket(12345);
            System.out.println("Servidor escuchando en el puerto 12345...");

            while (true) {
                Socket clSocket = srvSocket.accept();
                System.out.println("Cliente conectado: " + clSocket.getInetAddress());

                // Crear un nuevo hilo para manejar la conexión con el cliente actual
                Thread clientThread = new Thread(() -> procesarCliente(clSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void procesarCliente(Socket clSocket) {
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(clSocket.getInputStream()));
            PrintWriter salida = new PrintWriter(clSocket.getOutputStream(), true);

            String operacion = entrada.readLine();
            String rutaFichero = entrada.readLine();

            if (operacion.equals("G")) {
                enviarFichero(salida, rutaFichero);
            } else if (operacion.equals("D")) {
                borrarFichero(salida, rutaFichero);
            } else if (operacion.equals("R")) {
                String nuevaRutaFichero = entrada.readLine();
                renombrarFichero(salida, rutaFichero, nuevaRutaFichero);
            } else if (operacion.equals("L")) {
                listarFicheros(salida);
            }

            clSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void enviarFichero(PrintWriter out, String rutaFichero) {
        try {
            File fichero = new File(ubicacion, rutaFichero);
            if (fichero.exists()) {
                out.println("0"); // Fichero existente
                out.println(fichero.length()); // Longitud del fichero

                FileInputStream ficheroIS = new FileInputStream(fichero);
                byte[] buffer = new byte[1024];
                int bytesLeidos;

                while ((bytesLeidos = ficheroIS.read(buffer)) != -1) {
                    System.out.println("Bytes leídos: " + bytesLeidos);
                    out.write(Arrays.toString(buffer), 0, bytesLeidos);
                }

                ficheroIS.close();
            } else {
                out.println("1"); // Error: El fichero no existe
            }
        } catch (IOException e) {
            out.println("4"); // Error desconocido
        }
    }

    private static void borrarFichero(PrintWriter out, String rutaFichero) {
        try {
            File fichero = new File(ubicacion, rutaFichero);
            if (fichero.exists()) {
                fichero.delete();
                out.println("0"); // Operación exitosa
            } else {
                out.println("1"); // Error: El fichero no existe
            }
        } catch (Exception e) {
            out.println("4"); // Error desconocidocarch
        }
    }

    private static void renombrarFichero(PrintWriter out, String antiguaRuta, String nuevaRuta) {
        try {
            File antiguoFichero = new File(ubicacion, antiguaRuta);
            if (antiguoFichero.exists()) {
                File nuevoFichero = new File(ubicacion, nuevaRuta);
                if (nuevoFichero.exists()) {
                    out.println("2"); // El nuevo nombre no es correcto
                } else {
                    antiguoFichero.renameTo(nuevoFichero);
                    out.println("0"); // Operación exitosa
                }
            } else {
                out.println("1"); // Error: El fichero no existe
            }
        } catch (Exception e) {
            out.println("2"); // Error desconocido
        }
    }

    private static void listarFicheros(PrintWriter out) {
        try {
            File carpetaBase = new File(ubicacion);
            File[] ficheros = carpetaBase.listFiles();
            if (ficheros != null) {
                out.println("0"); // Operación exitosa
                out.println(ficheros.length); // Número de archivos

                for (File fichero : ficheros) {
                    out.println(fichero.getName()); // Nombre del archivo
                }
            } else {
                out.println("4"); // Error desconocido
            }
        } catch (Exception e) {
            out.println("4"); // Error desconocido
        }
    }
}
