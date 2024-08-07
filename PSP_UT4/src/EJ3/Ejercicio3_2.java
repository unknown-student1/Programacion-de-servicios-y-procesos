package EJ3;

import java.io.*;
import java.net.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Ejercicio3_2 {

    public static void main(String[] args) {
        // Comprueba si se ha proporcionado un argumento. Si no, muestra un mensaje de uso y termina el programa.
        if (args.length != 1) {
            System.out.println("Ingresa el nombre del usuario para verificar su registro");
            System.out.println("[USO:] java Ejercicio3 (user name)");
            return;
        }

        // Guarda el nombre del usuario proporcionado como argumento.
        String nombreUsuario = args[0];

        try {
            // Crea una URL a partir de la cadena proporcionada.
            URL url = new URL("https://ieslamarisma.net/prof/santi/psput4_login/");

            // Abre una conexión HTTP a la URL.
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();

            // Obtiene el código de respuesta HTTP de la conexión.
            int codigoRespuesta = conexion.getResponseCode();

            // Si el código de respuesta es HTTP_OK (200), procesa la respuesta.
            if (codigoRespuesta == HttpURLConnection.HTTP_OK) {
                // Lee la respuesta de la conexión.
                InputStream respuesta = conexion.getInputStream();
                BufferedReader lector = new BufferedReader(new InputStreamReader(respuesta));

                // Almacena la respuesta en un StringBuilder.
                StringBuilder sb = new StringBuilder();
                String linea;
                while ((linea = lector.readLine()) != null) {
                    sb.append(linea);
                }
                lector.close();

                // Analiza el código HTML de la respuesta utilizando Jsoup.
                Document doc = Jsoup.parse(sb.toString());

                // Selecciona todos los usuarios en la tabla.
                Elements usuarios = doc.select("table.table tbody tr");

                // Busca el usuario proporcionado en la lista de usuarios.
                boolean encontrado = false;
                for (Element usuario : usuarios) {
                    Element nombreElement = usuario.selectFirst("td:nth-child(2)");
                    String nombre = nombreElement.text();
                    if (nombre.equals(nombreUsuario)) {
                        encontrado = true;
                        System.out.println("Usuario " + nombreUsuario + " encontrado.");
                        break;
                    }
                }

                // Si el usuario no se encontró, imprime un mensaje
                if (!encontrado) {
                    System.out.println("\u001B[33mInfo: Usuario " + nombreUsuario + " no registrado.\u001B[0m");
                }

            } else {
                // Si el código de respuesta no es HTTP_OK, imprime un mensaje de error.
                System.out.println("\u001B[31m[ERROR] No se ha podido descargar el contenido. Código HTTP de respuesta: "
                        + codigoRespuesta + "\u001B[0m");
            }
        } catch (MalformedURLException e) {
            // Imprime un mensaje de error si la URL es incorrecta.
            System.out.println("\u001B[31mError: La URL especificada es erronea.\u001B[0m");
        } catch (IOException e) {
            // Imprime un mensaje de error si ocurre un error de entrada/salida.
            System.out.println("\u001B[31mError: Ha ocurrido un error de entrada/salida.\u001B[0m");
        }
    }
}
