package EJ3;

import java.io.*;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) {
        try {
            Socket so = new Socket("localhost", 12345);
            BufferedReader leer = new BufferedReader(new InputStreamReader(System.in));
            BufferedWriter escribir = new BufferedWriter(new OutputStreamWriter(so.getOutputStream()));

            System.out.println("Ingrese la operación (G/D/R/L):");
            String operacion = leer.readLine();
            escribir.write(operacion);
            escribir.newLine();

            System.out.println("Ingrese el nombre del archivo:");
            String nomFichero = leer.readLine();
            escribir.write(nomFichero);
            escribir.newLine();

            if (operacion.equals("R")) {
                System.out.println("Ingrese el nuevo nombre de archivo para la operación RENAME:");
                String nomFichero2 = leer.readLine();
                escribir.write(nomFichero2);
                escribir.newLine();
            }

            escribir.flush();

            BufferedReader Server = new BufferedReader(new InputStreamReader(so.getInputStream()));
            String respuesta;
            while ((respuesta = Server.readLine()) != null) {
                System.out.println(respuesta);
            }

            so.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
