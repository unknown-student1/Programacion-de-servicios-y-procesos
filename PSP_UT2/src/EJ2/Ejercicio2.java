package EJ2;

public class Ejercicio2 {
    public static void main(String[] args) {
        SistemaRed sistema = new SistemaRed("bgfd");  // Supongamos que la clave correcta es "bgfd"

        for (char letra = 'a'; letra <= 'z'; letra++) {
            new Thread(new AtaqueFuerzaBruta(sistema, letra)).start();
        }

        // Esperar hasta que se encuentre la clave
        while (!AtaqueFuerzaBruta.isClaveEncontrada()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("La clave correcta es: " + AtaqueFuerzaBruta.getClaveCorrecta());
    }
}