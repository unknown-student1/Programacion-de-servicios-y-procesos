import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Ejercicio3 {
    public static void main(String[] args) {
        long startTime = System.nanoTime();
        try {
            // Iniciar tres procesos diferentes
            ProcessBuilder processBuilder1 = new ProcessBuilder("notepad.exe");
            ProcessBuilder processBuilder2 = new ProcessBuilder("calc.exe");
            ProcessBuilder processBuilder3 = new ProcessBuilder("explorer.exe");

            Process process1 = processBuilder1.start();
            Process process2 = processBuilder2.start();
            Process process3 = processBuilder3.start();

            // Obtener los PID de los procesos
            long pid1 = process1.toHandle().pid();
            long pid2 = process2.toHandle().pid();
            long pid3 = process3.toHandle().pid();

            System.out.println("Proceso 1 (notepad.exe) PID: " + pid1);
            System.out.println("Proceso 2 (calc.exe) PID: " + pid2);
            System.out.println("Proceso 3 (explorer.exe) PID: " + pid3);

            // Esperar a que los procesos finalicen
            process1.waitFor();
            process2.waitFor();
            process3.waitFor();

            // Calcular el tiempo transcurrido
            long tiempoTranscurrido = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - startTime);
            System.out.println("Tiempo transcurrido (segundos): " + tiempoTranscurrido);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
