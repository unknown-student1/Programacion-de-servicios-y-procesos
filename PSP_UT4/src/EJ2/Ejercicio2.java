package EJ2;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.util.Properties;

public class Ejercicio2 {

    // Indicamos el correo y la contraseña de la aplicación para que funcione en el correo.
    public static final String SENDER_EMAIL = "examplemail@gmail.com";
    public static final String PASSWORD = "t6@88Y";

    public static void main(String[] args) {
        // Configuramos el servidor del correo saliente (SMTP)
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com"); // Establecemos el servidor SMTP de GMAIL
        properties.put("mail.smtp.port", "587"); // Establecemos el puerto SMTP
        properties.put("mail.smtp.auth", "true"); // Habilitamos la autenticación
        properties.put("mail.smtp.starttls.enable", "true"); // Habilitamos el protocolo TLS

        // Autenticación
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, PASSWORD);
            }
        });

        try {
            // Creamos un mensaje de correo
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL)); // Establece el remitente del mensaje
            message.setReplyTo(InternetAddress.parse(SENDER_EMAIL)); // Establece la dirección de respuesta
            message.setSubject("- Mensaje de Spam&Arrea -"); // Establece el asunto del mensaje

            // Leemos el contenido del mensaje desde el archivo Mensaje.txt
            StringBuilder messageContent = new StringBuilder();
            try (BufferedReader messageReader = new BufferedReader(new FileReader("mensaje.txt"))) {
                String line;
                while ((line = messageReader.readLine()) != null) {
                    messageContent.append(line).append("\n");
                }
            }
            message.setText(messageContent.toString()); // Establece el contenido del mensaje

            // Agregamos destinatarios desde el archivo clientes.txt
            try (BufferedReader reader = new BufferedReader(new FileReader("clientes.txt"))) {
                String recipientEmail;
                while ((recipientEmail = reader.readLine()) != null) {
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail.trim()));
                }
            }

            // Enviamos el mensaje
            Transport.send(message);

            System.out.println("Mensaje enviado correctamente!.");

        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }
}
