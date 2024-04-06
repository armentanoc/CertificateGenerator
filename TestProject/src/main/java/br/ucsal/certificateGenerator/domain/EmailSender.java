package br.ucsal.certificateGenerator.domain;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.IOException;
import java.util.Properties;

public class EmailSender {

    private String host;
    private String username;
    private String password;
    private String returnMessage;

    public EmailSender(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
    }

    public String sendEmail(String to, EmailContent emailContent) throws IOException {

    	Properties properties = new Properties();
    	properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
    	
        Session session = Session.getInstance(properties, new jakarta.mail.Authenticator() {
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
            returnMessage = "E-mail enviado com sucesso para: " + to;
            System.out.println("\u001B[32m" + "[EMAIL SENDER] " + returnMessage + "\u001B[0m");
            return returnMessage;

        } catch (MessagingException e) {
        	returnMessage = "[EMAIL SENDER] Erro ao enviar e-mail para: " + to;
        	System.err.print(returnMessage);
        	e.printStackTrace();
        	return returnMessage;
        }
    }
}