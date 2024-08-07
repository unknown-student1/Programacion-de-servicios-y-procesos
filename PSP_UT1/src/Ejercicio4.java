import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Ejercicio4 {
    public static void main(String[] args) {
        try {
            // Ejecutar el comando "tasklist"
            Process proceso = Runtime.getRuntime().exec("tasklist");

            // Leer la salida del comando
            BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
            String linea; // Variable para guardar cada linea del comando

            // Mostrar encabezado
            System.out.println("Nombre de imagen               PID Nombre de sesión Núm. de ses Uso de memor\n" +
                    "========================= ======== ================ =========== ============");

            // Bucle para recorrer cada línea
            while ((linea = reader.readLine()) != null) {
                if (linea.contains("Services")) { // Condicional que comprueba si la línea contine la palabra "Services"
                    System.out.println(linea);
                }
            }

            // Esperar a que termine el proceso
            int exitVal = proceso.waitFor();

            if (exitVal == 0) {
                System.out.println("\nComando ejecutado correctamente");
            } else {
                System.out.println("\nError al ejecutar el comando");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
