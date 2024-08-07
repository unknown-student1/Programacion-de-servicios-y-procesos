package EJ1;

public class Ejercicio1 {
    public static void main(String[] args) throws InterruptedException {
        // Crear 4 cuentas corrientes
        Cuenta cuenta1 = new Cuenta("Cuenta1", 200);
        Cuenta cuenta2 = new Cuenta("Cuenta2", 200);
        Cuenta cuenta3 = new Cuenta("Cuenta3", 200);
        Cuenta cuenta4 = new Cuenta("Cuenta4", 200);

        // Crear hilos para simular movimientos aleatorios de dinero
        Thread hilo1 = new Thread(new MovimientoBancario(cuenta1, cuenta2));
        Thread hilo2 = new Thread(new MovimientoBancario(cuenta2, cuenta3));
        Thread hilo3 = new Thread(new MovimientoBancario(cuenta3, cuenta4));
        Thread hilo4 = new Thread(new MovimientoBancario(cuenta4, cuenta1));

        // Iniciar los hilos
        hilo1.start();
        hilo2.start();
        hilo3.start();
        hilo4.start();

        // Esperar que los hilos terminen su ejecución
        hilo1.join();
        hilo2.join();
        hilo3.join();
        hilo4.join();

        // Imprimir los saldos después de que todos los hilos hayan terminado
        System.out.println("Suma de los saldos de todas las cuentas: " +
                (cuenta1.saldo + cuenta2.saldo + cuenta3.saldo + cuenta4.saldo));
    }
}
