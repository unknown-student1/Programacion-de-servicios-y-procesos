package EJ1.EJ1_1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    private static int contador=0;
    private static final int PORT=12345;

    public static void main(String[] args){
        try{
            ServerSocket serverSocket = new ServerSocket(PORT);
            while(true){
                System.out.println("Servidor esperando conexiones en puerto" + PORT +"...");
                Socket socket = serverSocket.accept();
                System.out.println("Cliente conectado desde" + socket.getInetAddress());
                procesarCliente(socket);
            }
        }catch(IOException e){
            System.err.println("Error desconocido del servidor: "+ e.getMessage());
        }
    }
    private static void procesarCliente(Socket socket){
        try{
            // Flujo de entrada desde el cliente
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            // Flujo de salida hacia el cliente
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            // Incrementar el contador de operaciones
            contador++;
            System.out.println("Cliente " + contador + " conectado.");

            // Leer datos enviados por el cliente
            char operacion = inputStream.readChar();
            long resultado = 0;
            String operacionRealizada;
            long num1 = inputStream.readLong();
            long num2 = inputStream.readLong();

            // Realizar la operación solicitada
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
                    resultado=0;
                    operacionRealizada = "Operación no válida.";
                    break;
            }

            // Escribir datos de resultado hacia el cliente
            outputStream.writeInt(contador);
            outputStream.writeLong(resultado);
            outputStream.writeUTF(operacionRealizada);

            // Cerrar flujos y el socket cuando se haya terminado de comunicar con el cliente
            inputStream.close();
            outputStream.close();
            socket.close();

        }catch(IOException e){
            System.err.println("Error en la comunicación con el cliente: "+ e.getMessage());
        }
    }
}
