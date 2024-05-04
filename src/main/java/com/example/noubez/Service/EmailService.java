package com.example.noubez.Service;

public class EmailService {
    public void sendEmail(String recipient, String subject, String body) {

        System.out.println("E-mail envoyé à " + recipient);
        System.out.println("Objet : " + subject);
        System.out.println("Corps de l'e-mail : " + body);
    }



}
