package EJ3;

import java.io.*;
import java.net.*;

public class Ejercicio3 {

    public static void main(String[] args) {
        //INDICAR EL USO DEL PROGRAMA SI NO SE RECIBEN 2 ARGUMENTOS
        if (args.length != 2) {
            System.out.println("Ingresa una URL para descargar el contenido");
            System.out.println("[USO:] java Ejercicio3 (URL origen) (Archivo destino)");
            return;
        }

        //VARIABLES DE ARGUMENTOS
        String origen = args[0];
        String destino = args[1];

        //PROGRAMA
        try {
            URL url = new URL(origen);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            int codigoRespuesta = conexion.getResponseCode();

            if (codigoRespuesta == HttpURLConnection.HTTP_OK) {
                InputStream is = conexion.getInputStream();
                BufferedReader lector = new BufferedReader(new InputStreamReader(is));
                BufferedWriter escritor = new BufferedWriter(new FileWriter(destino));

                String linea;
                while ((linea = lector.readLine()) != null) {
                    escritor.write(linea);
                    escritor.newLine();
                }
                lector.close();
                escritor.close();

                System.out.println("Guardado fichero " + destino);
            } else {
                // Si el código de respuesta no es HTTP_OK, imprime un mensaje de error.
                System.out.println("\u001B[31m[ERROR] No se ha podido descargar el contenido. Código HTTP de respuesta: "
                        + codigoRespuesta + "\u001B[0m");
            }
        } catch (MalformedURLException e) {
            System.out.println("El URL proporcionado no es válido.");
        } catch (IOException e) {
            System.out.println("Error de E/S: " + e.getMessage());
        }
    }
}
