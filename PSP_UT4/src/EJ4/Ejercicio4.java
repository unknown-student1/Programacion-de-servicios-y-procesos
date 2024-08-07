package EJ4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Ejercicio4 {

    public static void main(String[] args) throws IOException {
        // Registrar automáticamente a 20 empleados
        //registrarEmpleadosAutomaticamente();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            // Mostrar el menú
            System.out.println("Menú:");
            System.out.println("1. Registrar usuario");
            System.out.println("2. Iniciar sesión");
            System.out.println("3. Ver lista de usuarios");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");
            // Leer la opción del usuario
            String opcion = reader.readLine();
            // Ejecutar la opción seleccionada
            switch (opcion) {
                case "1":
                    registrarEmpleado();
                    break;
                case "2":
                    System.out.print("Correo: ");
                    String email = reader.readLine();
                    System.out.print("Clave: ");
                    String password = reader.readLine();
                    login(email, password);
                    break;
                case "3":
                    ListarEmpleados();
                    break;
                case "4":
                    System.out.println("Saliendo...");
                    return;
                default:
                    System.out.println("Opción no válida. Por favor, seleccione una opción válida.");
            }
        }
    }

    // Método para registrar un empleado manualmente
    public static void registrarEmpleado() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Nombre: ");
        String nombre = reader.readLine();
        System.out.print("Correo: ");
        String correo = reader.readLine();
        System.out.print("Clave (mínimo 6 caracteres): ");
        String clave = reader.readLine();

        // Validación de la longitud de la contraseña
        if (clave.length() < 6) {
            System.out.println("Error: La clave debe tener al menos 6 caracteres.");
            return;
        }

        // Construcción de la cadena de datos a enviar para el registro
        String info = "";
        info += URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(nombre, "UTF-8");
        info += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(correo, "UTF-8");
        info += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(clave, "UTF-8");
        info += "&" + URLEncoder.encode("cpassword", "UTF-8") + "=" + URLEncoder.encode(clave, "UTF-8");
        info += "&" + URLEncoder.encode("terminosycond", "UTF-8") + "=" + URLEncoder.encode("on", "UTF-8");
        info += "&" + URLEncoder.encode("signup", "UTF-8") + "=" + URLEncoder.encode("Registrar", "UTF-8");

        // URL hacia el registro
        URL url = new URL("https://ieslamarisma.net/prof/santi/psput4_login/register.php");
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setDoOutput(true); // Permitir enviar datos a través de la conexión
        conexion.setRequestMethod("POST");

        // Enviar datos a la conexión
        OutputStreamWriter escritor = new OutputStreamWriter(conexion.getOutputStream());
        escritor.write(info);
        escritor.flush();

        // Recibir la respuesta del servidor
        BufferedReader lector = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = lector.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append("\n");
        }
        String contenido = stringBuilder.toString();

        // Comprobar si hubo error en el registro
        if (contenido.contains("EXITO.! ¡Registrado exitosamente!")) {
            System.out.println("Usuario registrado con éxito.");
        } else {
            System.out.println("Error al registrar el usuario.");
        }

        escritor.close();
        lector.close();
    }

    // Método para registrar automáticamente 20 empleados
    public static void registrarEmpleadosAutomaticamente() throws IOException {
        int numeroEmpleado = 0;
        int letra = 1;

        while (numeroEmpleado < 20) {
            String info = "";
            String car = LetraEmpleado(letra); // Generar la letra correspondiente
            letra++;

            // Construir datos del empleado
            String nombre = "Luis" + car;
            String email = "luis" + car + "@dam2.ieslamarisma.net";
            String password = ".mkHF#l" + car;

            // Validación de la longitud de la contraseña
            if (password.length() < 6) {
                System.out.println("Error: La clave debe tener al menos 6 caracteres.");
                continue;
            }

            info += URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(nombre, "UTF-8");
            info += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
            info += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
            info += "&" + URLEncoder.encode("cpassword", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
            info += "&" + URLEncoder.encode("terminosycond", "UTF-8") + "=" + URLEncoder.encode("on", "UTF-8");
            info += "&" + URLEncoder.encode("signup", "UTF-8") + "=" + URLEncoder.encode("Registrar", "UTF-8");

            // URL hacia el registro
            URL url = new URL("https://ieslamarisma.net/prof/santi/psput4_login/register.php");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setDoOutput(true); // Permitir enviar datos a través de la conexión
            conexion.setRequestMethod("POST");

            // Enviar datos a la conexión
            OutputStreamWriter escritor = new OutputStreamWriter(conexion.getOutputStream());
            escritor.write(info);
            escritor.flush();

            // Recibir la respuesta del servidor
            BufferedReader lector = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = lector.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
            String contenido = stringBuilder.toString();

            // Comprobar si hubo error en el registro
            if (contenido.contains("EXITO.! ¡Registrado exitosamente!")) {
                System.out.println("Usuario registrado con éxito: " + nombre);
            } else {
                System.out.println("Error al registrar el usuario nº + " + (numeroEmpleado+1)
                        + " con nombre " + nombre);
            }

            // Incrementar el contador de empleados
            numeroEmpleado++;

            escritor.close();
            lector.close();
        }
    }

    // Método para listar empleados registrados
    private static void ListarEmpleados() throws IOException {
        URL url = new URL("https://ieslamarisma.net/prof/santi/psput4_login/index.php");
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String linea;
        while ((linea = br.readLine()) != null) {
            sb.append(linea);
        }
        br.close();

        // Analizar el código HTML utilizando Jsoup
        Document doc = Jsoup.parse(sb.toString());
        Elements usuarios = doc.select("table.table tbody tr");

        // Recorrer la tabla de usuarios para extraer nombre y correo
        for (Element usuario : usuarios) {
            Element nombreElement = usuario.selectFirst("td:nth-child(2)");
            Element emailElement = usuario.selectFirst("td:nth-child(3)");
            String nombre = nombreElement.text();
            String email = emailElement.text();
            System.out.println("[Nombre] " + nombre + " | [Email] " + email);
        }
    }

    // Método para iniciar sesión
    public static void login(String email, String password) throws IOException {
        String datos = "";

        datos += URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
        datos += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
        datos += "&" + URLEncoder.encode("login", "UTF-8") + "=" + URLEncoder.encode("Iniciar Sesión", "UTF-8");

        // URL para iniciar sesión
        URL url = new URL("https://ieslamarisma.net/prof/santi/psput4_login/login.php");
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setDoOutput(true); // Permitir enviar datos a través de la conexión
        conexion.setRequestMethod("POST");

        // Enviar datos a la conexión
        OutputStreamWriter escritor = new OutputStreamWriter(conexion.getOutputStream());
        escritor.write(datos);
        escritor.flush();

        // Recibir la respuesta del servidor
        BufferedReader lector = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
        StringBuilder sB = new StringBuilder();
        String line;

        while ((line = lector.readLine()) != null) {
            sB.append(line);
            sB.append("\n");
        }
        String contenido = sB.toString();

        escritor.close();
        lector.close();

        // Verificar el contenido de la respuesta para determinar si el login fue exitoso o no
        if (contenido.contains("Revisa los datos!!!")) {
            System.out.println("Error en el inicio de sesión. Verifique sus credenciales.");
        } else if (contenido.contains("Esta cuenta esta desactivada")) {
            System.out.println("Error en el inicio de sesión. La cuenta está desactivada.");
        } else if (contenido.contains("index.php")) {
            System.out.println("Inicio de sesión exitoso. Redirigiendo a index.php...");
        } else {
            System.out.println("Error en el inicio de sesión. Verifique sus credenciales.");
        }
    }

    // Método para generar las letras de los empleados secuencialmente (A, B, ..., Z, AA, AB, ...)
    public static String LetraEmpleado(int letra) {
        StringBuilder builder = new StringBuilder();
        while (letra > 0) {
            int remainder = (letra - 1) % 26;
            char c = (char) (remainder + 'A');
            builder.insert(0, c);
            letra = (letra - 1) / 26;
        }
        return builder.toString();
    }
}