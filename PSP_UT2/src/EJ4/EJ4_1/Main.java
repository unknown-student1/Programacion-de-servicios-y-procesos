package EJ4.EJ4_1;

import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(1);

        Camion camion1 = new Camion(1, semaphore);
        Camion camion2 = new Camion(2, semaphore);
        Camion camion3 = new Camion(3, semaphore);
        Camion camion4 = new Camion(4, semaphore);
        Camion camion5 = new Camion(5, semaphore);
        Camion camion6 = new Camion(6, semaphore);

        camion1.start();
        camion2.start();
        camion3.start();
        camion4.start();
        camion5.start();
        camion6.start();
    }
}
