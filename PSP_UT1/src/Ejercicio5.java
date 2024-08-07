import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Ejercicio5 {

    public static void main(String[] args) {
        try {
            // Ejecuta el comando
            Process process = Runtime.getRuntime().exec("cmd /c dir /-C /A:-D \\windows");
            // Lee la salida del comando
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            ArrayList<Long> sizes = new ArrayList<>();

            // Bucle que para analizar cada línea de la salida del comando
            while ((line = reader.readLine()) != null) {
                // Ignora las líneas vacías y las que no comienzan con un dígito
                if (line.trim().isEmpty() || !Character.isDigit(line.charAt(0))) continue;

                // Extrae los tamaños de los archivos de la línea de salida
                ArrayList<Long> currentSizes = Columna(line);

                // Agrega todos los tamaños de esta línea a la lista de tamaños
                sizes.addAll(currentSizes);
            }

            // Suma todos los tamaños de los archivos
            long totalSize = SumaMayor(sizes);

            // Encuentra el tamaño del archivo más grande
            long maxSize = NumeroMayor(sizes);

            // Imprime el tamaño total y el tamaño del archivo más grande
            System.out.println("Total size: " + totalSize + " bytes");
            System.out.println("Largest file size: " + maxSize + " bytes");

            // Mostrar tamaño en MB
            System.out.printf("\nTotal size (MB): %.2f MB%n", totalSize / 1048576.0);
            System.out.printf("Largest file size (MB): %.2f MB%n", maxSize / 1048576.0);

        } catch (IOException e) {
            // Imprime la traza de la excepción en caso de error
            e.printStackTrace();
        }
    }

    // Extrae los tamaños de los archivos de la línea de salida
    public static ArrayList<Long> Columna(String input) {
        ArrayList<Long> fileSizes = new ArrayList<>();
        String[] parts = input.trim().split("\\s+");
        try {
            // Asume que el tamaño está en la penúltima posición
            long size = Long.parseLong(parts[parts.length - 2]);
            fileSizes.add(size);
        } catch (NumberFormatException e) {
            // Imprime un mensaje de error si no puede parsear el tamaño del archivo
            System.out.println("Error parsing file size: " + e.getMessage());
        }
        return fileSizes;
    }

    // Suma todos los elementos de la lista
    public static long SumaMayor(ArrayList<Long> sizes) {
        long total = 0;
        for (long size : sizes) {
            total += size;
        }
        return total;
    }

    // Encuentra el número más grande en la lista
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
