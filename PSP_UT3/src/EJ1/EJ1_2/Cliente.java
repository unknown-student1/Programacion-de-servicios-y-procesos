package EJ1.EJ1_2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Cliente {
    private static final int puerto = 12345;

    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress servidorDireccion = InetAddress.getByName("localhost");

            Scanner scanner = new Scanner(System.in);

            while (true) {
                // Lectura de la operación y operandos desde el usuario
                System.out.print("Ingrese la operación (+, -, *, /) o 'F' 'A' para salir: ");
                String tipoOperacion = scanner.nextLine();

                if (tipoOperacion.equals("A")) {
                    break;
                }

                char operacion = tipoOperacion.charAt(0);

                System.out.print("Ingrese el primer número: ");
                long num1 = scanner.nextLong();

                System.out.print("Ingrese el segundo número: ");
                long num2 = scanner.nextLong();

                // Limpiar el buffer del scanner
                scanner.nextLine();

                // Creación del paquete a enviar al servidor
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                DataOutputStream salida = new DataOutputStream(bs);

                salida.writeChar(operacion);
                salida.writeLong(num1);
                salida.writeLong(num2);

                DatagramPacket paqueteEnviar = new DatagramPacket(bs.toByteArray(), bs.size(), servidorDireccion, puerto);

                // Envío del paquete al servidor
                socket.send(paqueteEnviar);

                // Recepción de la respuesta del servidor
                byte[] datosRecibidos = new byte[1024];
                DatagramPacket paqueteRecibido = new DatagramPacket(datosRecibidos, datosRecibidos.length);
                socket.receive(paqueteRecibido);

                // Procesamiento de la respuesta del servidor
                ByteArrayInputStream be = new ByteArrayInputStream(paqueteRecibido.getData());
                DataInputStream entrada = new DataInputStream(be);

                int numOperacion = entrada.readInt();
                long resultado = entrada.readLong();
                String operacionRealizada = entrada.readUTF();

                System.out.println("Resultado de la operación " + numOperacion + ": " + operacionRealizada);
            }

            // Cierre del socket al salir del bucle
            socket.close();
            scanner.close();

        } catch (Exception e) {
            System.err.println("Error en el cliente: " + e.getMessage());
        }
    }
}
