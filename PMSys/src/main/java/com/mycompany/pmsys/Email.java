/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.pmsys;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
//import javax.activation.*;  

/**
 *
 * @author Henrique
 */
public class Email {

    public void sendEmail(String email, String senha) {

        String to = email;//change accordingly  
        String from = "dotsys.company@gmail.com";//change accordingly  

        //Get the session object  
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "587");
        properties.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        

        Session session = Session.getDefaultInstance(properties,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("dotsys.company@gmail.com",
                        "#Gfgrupo6");
            }
        });

        session.setDebug(true);

        //compose the message  
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Enviando email de recuperação de senha");
            message.setText("Olá " + email + ", este é um email de recuperação de senha"
                    + "\n sua senha é: " + senha);

            // Send message  
            Transport.send(message);
            System.out.println("Mensagem enviada com sucesso....");

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
