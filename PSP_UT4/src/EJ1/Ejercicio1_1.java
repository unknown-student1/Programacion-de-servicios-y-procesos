package EJ1;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class Ejercicio1_1 {
    public static void main(String[] args) throws IOException {
        String servidor = "ieslamarisma.net";
        String usuario = "examenut4";
        String contraseña = "exhausta";

        FTPClient ftpCliente = new FTPClient();
        try {
            ftpCliente.connect(servidor);
            ftpCliente.login(usuario, contraseña);
            ftpCliente.enterLocalPassiveMode();
            ftpCliente.setFileType(FTP.BINARY_FILE_TYPE);

            String directorioActual = "/instrucciones/";
            String Nombrefichero = "leeme.txt";
            OutputStream salida = new FileOutputStream("C:/Users/Luis/Downloads/leeme.txt");
            boolean descargado = ftpCliente.retrieveFile(directorioActual + Nombrefichero, salida);
            salida.close();

            if (descargado) {
                System.out.println("* Descargado el fichero en destino **");
            } else {
                System.out.println("* Error al descargar el fichero *");
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}

