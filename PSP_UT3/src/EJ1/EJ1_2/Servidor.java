package EJ1.EJ1_2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Servidor {
    private static int cont = 0;
    private static final int puerto = 12345;

    public static void main(String[] args){
        try{
            DatagramSocket ds = new DatagramSocket(puerto);
            while(true){
                System.out.println("Servidor esperando conexiones en el puerto " + puerto + "...");
                byte[] datosRecibidos = new byte[1024];
                DatagramPacket paquete = new DatagramPacket(datosRecibidos, datosRecibidos.length);
                ds.receive(paquete);
                System.out.println("Cliente conectado desde " + paquete.getAddress());
                procesarCliente(ds, paquete);
            }
        }catch (Exception e){
            System.err.println("Error producido en el servidor "+ e.getMessage());
        }
    }
    private static void procesarCliente(DatagramSocket ds, DatagramPacket paqueteRecibido) {
        try {
            // Flujo de entrada desde el cliente
            ByteArrayInputStream be = new ByteArrayInputStream(paqueteRecibido.getData());
            DataInputStream entrada = new DataInputStream(be);
            // Flujo de salida hacia el cliente
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            DataOutputStream salida = new DataOutputStream(bs);
            // Incrementar el contador de operaciones
            cont++;
            System.out.println("Cliente " + cont + " conectado.");

            // Leer datos enviados por el cliente
            char operacion = entrada.readChar();
            long resultado = 0;
            String operacionRealizada;
            long num1 = entrada.readLong();
            long num2 = entrada.readLong();

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

            // Enviar datos al cliente
            DatagramPacket paqueteEnviar = new DatagramPacket(bs.toByteArray(),
                    bs.size(), paqueteRecibido.getAddress(), paqueteRecibido.getPort());
            ds.send(paqueteEnviar);

            // Cerramos los flujos cuando se haya terminado de comunicar con el cliente
            entrada.close();
            salida.close();

        } catch (Exception e) {
            System.err.println("Error en la comunicación con el cliente: " + e.getMessage());
        }
    }
}
