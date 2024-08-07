package EJ1.EJ1_3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    private static final int puerto = 12345;

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            while (true) {
                // Lectura de la operación y operandos desde el usuario
                System.out.print("Ingrese la operación (+, -, *, /) o 's' para salir: ");
                String Operacion = scanner.nextLine();

                if (Operacion.equals("s")) {
                    break;
                }

                char operacion = Operacion.charAt(0);

                System.out.print("Ingrese el primer número: ");
                long num1 = scanner.nextLong();

                System.out.print("Ingrese el segundo número: ");
                long num2 = scanner.nextLong();

                // Limpiar el buffer del scanner
                scanner.nextLine();

                // Crear socket y conectar al servidor
                Socket socket = new Socket("localhost", puerto);

                // Obtener flujos de entrada y salida
                DataInputStream entrada = new DataInputStream(socket.getInputStream());
                DataOutputStream salida = new DataOutputStream(socket.getOutputStream());

                // Enviar datos al servidor
                salida.writeChar(operacion);
                salida.writeLong(num1);
                salida.writeLong(num2);

                // Recibir resultado del servidor
                int numOperacion = entrada.readInt();
                long resultado = entrada.readLong();
                String operacionRealizada = entrada.readUTF();

                // Mostrar resultado
                System.out.println("Resultado de la operación " + numOperacion + ": " + operacionRealizada);

                // Cerrar conexión con el servidor
                entrada.close();
                salida.close();
                socket.close();
            }

            scanner.close();

        } catch (Exception e) {
            System.err.println("Error en el cliente: " + e.getMessage());
        }
    }
}
