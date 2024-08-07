package EJ1;

import java.util.Random;

public class MovimientoBancario implements Runnable {
    private Cuenta origen;
    private Cuenta destino;
    private Random rand = new Random();
    public MovimientoBancario(Cuenta origen, Cuenta destino) {
        this.origen = origen;
        this.destino = destino;
    }

    @Override
    public void run() {
        int cont = 0;
        try {
            while (cont < 50) {
                double amount = Math.round(rand.nextDouble() * 300);
                if (origen.saldo >= amount) {
                    Cuenta primero = (origen.id.compareTo(destino.id) < 0) ? origen : destino;
                    Cuenta segundo = (primero == origen) ? destino : origen;
                    synchronized (primero) {
                        synchronized (segundo) {

                            System.out.println("Antes - Saldo de " + origen.id + ": " + origen.saldo + ", Saldo de " + destino.id + ": " + destino.saldo);
                            origen.transferencia(destino, amount);
                            System.out.println("Después - Saldo de " + origen.id + ": " + origen.saldo + ", Saldo de " + destino.id + ": " + destino.saldo);
                            cont++;
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
