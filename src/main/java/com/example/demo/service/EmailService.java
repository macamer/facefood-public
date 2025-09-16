package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Recuperación de contraseña - Facefood");
        message.setText("Hola!\n\nPara restablecer tu contraseña, haz clic en el siguiente enlace:\n\n" +
                        resetLink + "\n\nEste enlace expirará en 15 minutos.");

        mailSender.send(message);
    }
}
