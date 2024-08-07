package EJ2;

public class AtaqueFuerzaBruta implements Runnable {
    private static final String ALFABETO = "abcdefghijklmnopqrstuvwxyz";
    private static volatile boolean claveEncontrada = false; //bandera para verificar si la clave es la del sistema
    private static volatile String claveCorrecta = null; //Aquí guardamos la clave correcta
    private static volatile int intento = 0;//Para el mensaje de los intentos

    private SistemaRed sistema; //sistema de red que usaremos en el constructor del hilo
    private char letraInicial; //La letra que busca cada hilo

    public AtaqueFuerzaBruta(SistemaRed sistema, char letraInicial) {
        this.sistema = sistema;
        this.letraInicial = letraInicial;
    }

    public void run() {
        for (int i = 0; i < ALFABETO.length() && !claveEncontrada; i++) {
            for (int j = 0; j < ALFABETO.length() && !claveEncontrada; j++) {
                for (int k = 0; k < ALFABETO.length() && !claveEncontrada; k++) {
                    String clave = "" + letraInicial + ALFABETO.charAt(i) + ALFABETO.charAt(j) + ALFABETO.charAt(k);
                    try {
                        boolean acierto = sistema.claveOk(clave);
                        if (acierto) {
                            claveEncontrada = true;
                            claveCorrecta = clave;
                        }
                        // Imprimir el mensaje de cada intento
                        synchronized (AtaqueFuerzaBruta.class) {
                            System.out.println(++intento + ".º - H-" + letraInicial + ", prueba '" + clave + "' - " + (acierto ? "Acierto" : "Fallo"));
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static boolean isClaveEncontrada() {
        return claveEncontrada;

    }

    public static String getClaveCorrecta() {
        return claveCorrecta;
    }
}