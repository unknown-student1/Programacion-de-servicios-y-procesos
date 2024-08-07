package EJ1;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

public class Ejercicio1 {
    public static void main(String[] args) throws IOException {
        String servidor = "ftp.rediris.es";
        String usuario = "anonymous";
        String contraseña = "anonymous";

        FTPClient ftpCliente = new FTPClient();
        while (true) {
            try {
                ftpCliente.connect(servidor);
                ftpCliente.login(usuario, contraseña);
                ftpCliente.enterLocalPassiveMode();
                ftpCliente.setFileType(FTP.BINARY_FILE_TYPE);

                Scanner sc = new Scanner(System.in);
                String directorioActual = "/";
                while (true) {
                    FTPFile[] files = ftpCliente.listFiles(directorioActual);
                    System.out.println("Lista de archivos y carpetas de " + directorioActual);
                    for (FTPFile file : files) {
                        String type = file.isDirectory() ? "[carpeta]" : "[archivo]";
                        System.out.println("   - " + file.getName() + " " + type);
                    }

                    System.out.println("\nOperar:");
                    System.out.println(" 1 – Entrar en carpeta");
                    System.out.println(" 2 – Descargar archivo");
                    System.out.println(" 0 - Salir");
                    System.out.print("Seleccione opción: ");
                    int opcion = sc.nextInt();
                    sc.nextLine(); // consume newline

                    if (opcion == 0) {
                        System.exit(0);
                    } else if (opcion == 1) {
                        System.out.print("Nombre carpeta: ");
                        String nombre = sc.nextLine();
                        if (nombre.equals("..")) {
                            int lastSlashIndex = directorioActual.lastIndexOf("/", directorioActual.length() - 2);
                            if (lastSlashIndex > 0) {
                                directorioActual = directorioActual.substring(0, lastSlashIndex + 1);
                            } else {
                                directorioActual = "/";
                            }
                        } else {
                            if (!directorioActual.endsWith("/")) {
                                directorioActual += "/";
                            }
                            directorioActual += nombre + "/";
                        }
                    } else if (opcion == 2) {
                        System.out.print("Nombre de archivo: ");
                        String Nombrefichero = sc.nextLine();
                        if (Nombrefichero.startsWith("/")) {
                            Nombrefichero = Nombrefichero.substring(1);
                        }
                        System.out.print("Carpeta destino: ");
                        String carpetaDestino = sc.nextLine();
                        if (!carpetaDestino.endsWith("/")) {
                            carpetaDestino += "/";
                        }
                        String rutaArchivo = directorioActual + Nombrefichero;
                        System.out.println("Intentando descargar desde: " + rutaArchivo);
                        OutputStream salida = new FileOutputStream(carpetaDestino + Nombrefichero);
                        boolean descargado = ftpCliente.retrieveFile(rutaArchivo, salida);
                        salida.close();

                        if (descargado) {
                            System.out.println("* Descargado el fichero en " + carpetaDestino + " **");
                        } else {
                            System.out.println("* Error al descargar el fichero *");
                        }
                    }
                }
            } catch (IOException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }
}

