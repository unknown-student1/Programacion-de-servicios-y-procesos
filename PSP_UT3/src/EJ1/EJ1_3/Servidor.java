package EJ1.EJ1_3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    private static int cont = 0;
    private static final int puerto = 12345;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(puerto);
            System.out.println("Servidor esperando conexiones en el puerto " + puerto + "...");

            while (true) {
                Socket socket = serverSocket.accept();
                cont++;

                System.out.println("Cliente " + cont + " conectado desde " + socket.getInetAddress());

                // Crear un nuevo hilo para manejar la conexión con el cliente
                Thread Cliente = new Thread(() -> procesarCliente(socket));
                Cliente.start();
            }
        } catch (Exception e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }

    private static void procesarCliente(Socket socket) {
        try {
            DataInputStream entrada = new DataInputStream(socket.getInputStream());
            DataOutputStream salida = new DataOutputStream(socket.getOutputStream());

            // Incrementar el contador de operaciones
            System.out.println("Cliente " + cont + " conectado.");

            // Leer datos enviados por el cliente
            char operacion = entrada.readChar();
            long resultado = 0;
            String operacionRealizada;
            long num1 = entrada.readLong();
            long num2 = entrada.readLong();

            // Realizar la operación
            switch (operacion) {
                case '+':
                    resultado = num1 + num2;
                    operacionRealizada = num1 + " + " + num2 + " = " + resultado;
                    break;
                case '-':
                    resultado = num1 - num2;
                    operacionRealizada = num1 + " - " + num2 + " = " + resultado;
                    break;
                case '*':
                    resultado = num1 * num2;
                    operacionRealizada = num1 + " * " + num2 + " = " + resultado;
                    break;
                case '/':
                    if (num2 != 0) {
                        resultado = num1 / num2;
                        operacionRealizada = num1 + " / " + num2 + " = " + resultado;
                    } else {
                        operacionRealizada = "Error: División por cero.";
                    }
                    break;
                default:
                    resultado = 0;
                    operacionRealizada = "Operación no válida.";
                    break;
            }

            // Escribir datos de resultado para el cliente
            salida.writeInt(cont);
            salida.writeLong(resultado);
            salida.writeUTF(operacionRealizada);

            // Cerrar la conexión con el cliente
            entrada.close();
            salida.close();
            socket.close();

        } catch (Exception e) {
            System.err.println("Error en la comunicación con el cliente: " + e.getMessage());
        }
    }
}
