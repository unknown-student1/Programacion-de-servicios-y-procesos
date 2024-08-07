package EJ2;

import java.util.concurrent.Semaphore;

public class SistemaRed {
    private String claveCorrecta;
    private Semaphore semaforo; //sistema de seguridad

    //El constructor tiene la clave del sistema de red
    public SistemaRed(String claveCorrecta) {
        this.claveCorrecta = claveCorrecta;
        this.semaforo = new Semaphore(4);
    }

    //Método que verifica si la clave pasada es la correcta
    public boolean claveOk(String clave) throws InterruptedException {
        semaforo.acquire();
        try {
            return clave.equals(claveCorrecta);
        } finally {
            semaforo.release();
        }
    }
}
