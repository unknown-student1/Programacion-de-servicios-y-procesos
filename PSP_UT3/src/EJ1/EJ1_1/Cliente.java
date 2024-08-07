package EJ1.EJ1_1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345);
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("- Cliente Operaciones matemáticas -");
                System.out.print("Seleccione operador o comando (+,-,*,/, F, A): ");
                char operador = scanner.next().charAt(0);

                if (operador == 'F') {
                    System.out.println(" Finalizando la conexión ");
                    output.writeChar('F');
                    break;
                } else if (operador == 'A') {
                    System.out.println(" Abortando la conexión ");
                    output.writeChar('A');
                    break;
                }

                System.out.print("Número 1: ");
                long num1 = scanner.nextLong();
                System.out.print("Número 2: ");
                long num2 = scanner.nextLong();

                output.writeChar(operador);
                output.writeLong(num1);
                output.writeLong(num2);

                int numOperacion = input.readInt();
                long resultado = input.readLong();
                String operationStr = input.readUTF();

                System.out.println("N.º Ope. " + numOperacion + " - " + operationStr);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}