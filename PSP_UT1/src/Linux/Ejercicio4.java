package Linux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Ejercicio4 {
    public static void main(String[] args) {
        try {
            // Ejecuta el ccmando "ls -al /tmp"
            Process process = Runtime.getRuntime().exec("ls -al /tmp");

            // Obtenemos la salida del comando
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String linea; // Variable para guardar cada linea del comando

            // Bucle para recorrer la salida del comando
            while ((linea = reader.readLine()) != null) {
                // Filtra y muestra solo los archivos que pertenecen al usuario
                if (linea.contains(" " + System.getProperty("user.name") + " ")) {
                    System.out.println(linea);
                }
            }

            // Cierra el lector
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
