package email;
import javax.mail.*;
import javax.mail.internet.*;

import java.util.Properties;
/*
    public class EmailSender {
        private static Session session;

        public static void initializeEmailSession() {
            final String username = "rwafinouba@gmail.com";
            final String password = "12345678";

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });
        }

        public static void sendEmail(String recipientEmail, String subject, String content) throws MessagingException {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("rwafinouba@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);

            System.out.println("E-mail envoyé à " + "nouba.raouafi@esprit.tn");
        }
    }
*/

