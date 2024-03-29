package br.ucsal.certificateGenerator.domain;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.io.IOException;
import java.util.Properties;

public class EmailSender {

    private String host;
    private String username;
    private String password;

    public EmailSender(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
    }

    public void sendEmail(String to, EmailContent emailContent) throws IOException {

    	Properties properties = new Properties();
    	properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
    	
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        
        //session.setDebug(true);
        
        try {
            Message message = new MimeMessage(session);
            
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(emailContent.getSubject());
            
            MimeMultipart multipart = new MimeMultipart();
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(emailContent.getBody(), "text/html; charset=utf-8");
            multipart.addBodyPart(htmlPart);
            message.setContent(multipart);

            if (emailContent.getCertificate() != null) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                attachmentPart.attachFile(emailContent.getCertificate());
                multipart.addBodyPart(attachmentPart);
            }
            
            Transport.send(message);
            System.out.println("\u001B[32mE-mail enviado com sucesso para: " + to + "\u001B[0m");

        } catch (MessagingException e) {
            System.err.println("Erro ao enviar e-mail para: " + to);
            e.printStackTrace();
        }
    }
}