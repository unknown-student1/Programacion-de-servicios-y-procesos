package EJ3;

import java.io.*;
import java.net.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Ejercicio3_1 {
    public static void main(String[] args) throws MalformedURLException {
        try {
            // Crear un objeto URL a partir de la dirección web
            URL url = new URL("https://ieslamarisma.net/prof/santi/psput4_login/index.php");

            // Abrir una conexión HTTP a la URL
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();

            // Establecer el método de solicitud a GET
            conexion.setRequestMethod("GET");

            // Nombre del usuario a buscar
            String usuarioComprobar = "LuisA";

            // Crear un BufferedReader para leer la respuesta de la conexión
            BufferedReader br = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String linea;

            // Leer la respuesta línea por línea y agregarla a StringBuilder
            while ((linea = br.readLine()) != null) {
                sb.append(linea);
            }
            br.close();

            // Analizar el código HTML de la respuesta utilizando Jsoup
            Document doc = Jsoup.parse(sb.toString());

            // Seleccionar todos los usuarios en la tabla
            Elements usuarios = doc.select("table.table tbody tr");

            // Buscar un usuario específico en la lista de usuarios
            boolean encontrado = false;
            for (Element usuario : usuarios) {
                Element nombreElement = usuario.selectFirst("td:nth-child(2)");
                String nombre = nombreElement.text();

                // Si el nombre del usuario coincide con el nombre a comprobar, marcar como encontrado
                if (nombre.equals(usuarioComprobar)) {
                    encontrado = true;
                    System.out.println("Usuario " + usuarioComprobar + " registrado.");
                    break;
                }
            }

            // Si el usuario no se encontró, imprimir un mensaje
            if (!encontrado) {
                System.out.println("\u001B[33mInfo: Usuario " + usuarioComprobar + " no registrado.\u001B[0m");
            }

        } catch (MalformedURLException e) {
            // Imprimir un mensaje de error si la URL es incorrecta
            System.out.println("\u001B[31mError: La URL especificada es erronea.\u001B[0m");
            throw new RuntimeException(e);
        } catch (IOException e) {
            // Imprimir un mensaje de error si ocurre un error de entrada/salida
            System.out.println("\u001B[31mError: Ha ocurrido un error de entrada/salida.\u001B[0m");
            throw new RuntimeException(e);
        }
    }
}