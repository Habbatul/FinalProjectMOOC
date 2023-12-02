package com.finalproject.mooc.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class EmailUtil {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendOtpEmail(String email, String otp) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
        messageHelper.setTo(email);
        messageHelper.setSubject("Verify OTP");
        messageHelper.setText("Hello, your OTP is " + otp);

        javaMailSender.send(mimeMessage);
    }

    public void sendOtpEmailResetPassword(String email, String token) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
        messageHelper.setTo(email);
        messageHelper.setSubject("Tautan untuk halaman reset password");

        //nantinya ganti url disini
        messageHelper.setText("Hi, berikut ini adalah tautan untuk reset password anda : http://localhost:8080/coba.html?resetToken="+token);

        javaMailSender.send(mimeMessage);
    }

}
