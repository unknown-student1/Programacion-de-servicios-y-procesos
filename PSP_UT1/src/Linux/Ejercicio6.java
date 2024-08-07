package Linux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Ejercicio6 {

    public static void main(String[] args) {
        try {
            // Ejecuta el comando y captura la salida
            Process process = Runtime.getRuntime().exec("ls -al /home/luis /home/luis/Imágenes/");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            ArrayList<Long> sizes = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                sizes.addAll(Columna(line));
            }

            // Calcula el tamaño total y el tamaño del archivo más grande
            long totalSize = SumaMayor(sizes);
            long maxSize = NumeroMayor(sizes);

            // Muestra los resultados
            System.out.println("Total size: " + totalSize + " bytes");
            System.out.println("Largest file size: " + maxSize + " bytes");

            // Mostrar tamaño en MB
            System.out.printf("\nTotal size (MB): %.2f MB%n", totalSize / 1048576.0);
            System.out.printf("Largest file size (MB): %.2f MB%n", maxSize / 1048576.0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Extrae los tamaños de los archivos de cada línea de la salida del comando
    public static ArrayList<Long> Columna(String input) {
        ArrayList<Long> fileSizes = new ArrayList<>();
        if (!input.trim().isEmpty()) {  // Asegura que la línea no esté vacía
            String[] parts = input.split("\\s+");
            if (parts.length > 4) { // Asegura que haya suficientes partes en la línea
                try {
                    long size = Long.parseLong(parts[4]); // El tamaño del archivo está en la quinta columna
                    fileSizes.add(size);
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing file size: " + e.getMessage());
                }
            }
        }
        return fileSizes;
    }

    // Suma todos los elementos de la matriz
    public static long SumaMayor(ArrayList<Long> sizes) {
        long total = 0;
        for (long size : sizes) {
            total += size;
        }
        return total;
    }

    // Encuentra el número más grande en la matriz
    public static long NumeroMayor(ArrayList<Long> sizes) {
        long max = 0;
        for (long size : sizes) {
            if (size > max) {
                max = size;
            }
        }
        return max;
    }
}