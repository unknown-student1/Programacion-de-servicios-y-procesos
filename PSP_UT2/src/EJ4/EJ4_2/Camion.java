package EJ4.EJ4_2;

import java.util.concurrent.Semaphore;

class Camion extends Thread {
    private int nombre;
    private Semaphore semaphore;

    public Camion(int nombre, Semaphore semaphore) {
        this.nombre = nombre;
        this.semaphore = semaphore;
    }

    private void cruzarPuente() throws InterruptedException {
        System.out.println("C_" + nombre + " (2) - PUENTE - Comienzo cruzar");
        Thread.sleep(500);
        System.out.println("C_" + nombre + " (3) - PUENTE - Fin cruzar");
    }

    private void cargarMineral() throws InterruptedException {
        System.out.println("C_" + nombre + " (4) – Esperando izquierda para cargar");
        semaphore.acquire();
        System.out.println("C_" + nombre + " (5) – Escabadora " + nombre + " cargando");
        Thread.sleep(1500);
        System.out.println("C_" + nombre + " (6) – Escabadora " + nombre + " – Fin carga");
        semaphore.release();
    }

    private void descargarMineral() {
        System.out.println("C_" + nombre + " (10) – Descargando camión");
    }

    public void run() {
        try {
            System.out.println("C_" + nombre + " (1) - Esperando derecha para cruzar puente");
            cruzarPuente();
            cargarMineral();
            descargarMineral();
            System.out.println("C_" + nombre + " ha terminado su ejecución");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
