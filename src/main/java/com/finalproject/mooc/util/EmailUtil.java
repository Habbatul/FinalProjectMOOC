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
        String htmlContent = "<div style=\"font-family: Helvetica,Arial,sans-serif;min-width:1000px;overflow:auto;line-height:2\">" +
                "    <div style=\"margin:50px auto;width:80%%;padding:20px 0\">" +
                "        <div style=\"border-bottom:5px solid #eee\">" +
                "            <a href=\"\" style=\"font-size:30px;color: #a115b7;text-decoration:none;font-weight:600\">Binar Belajar</a>" +
                "        </div>" +
                "        <p style=\"font-size:15px\">Hallo <b>Binarian</b><span style=\"font-size:20px\"> 👋</span>,</p>" +
                "        <p>" +
                "            Terima kasih telah memilih Binar Belajar sebagai tempat eksplorasi ilmu dan kreativitas kamu!.<br>" +
                "            Gunakan OTP ini untuk menyelesaikan prosedur Pendaftaran kamu dan memverifikasi akun kamu di Binar Belajar." +
                "        </p>" +
                "        <p>Ingat, Jangan pernah membagikan OTP ini kepada siapa pun.</p>" +
                "        <h2 style=\"background: #00466a;margin: 0 auto;width: max-content;padding: 0 10px;color: #fff;border-radius: 4px;\">" +
                "            %s" +
                "        </h2>" +
                "        <p style=\"font-size:15px;\">Salam,<br />Team Binar Belajar</p>" +
                "        <hr style=\"border:none;border-top:5px solid #eee\" />" +
                "        <div style=\"float:right;padding:8px 0;color:#aaa;font-size:0.8em;line-height:1;font-weight:300\">" +
                "            <p>" +
                "                Binar Belajar,<br>" +
                "                by Binar Academy" +
                "            </p>" +
                "        </div>" +
                "    </div>" +
                "</div>";

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setTo(email);
        messageHelper.setSubject("Verify OTP");
        messageHelper.setText(String.format(htmlContent, otp), true);

        javaMailSender.send(mimeMessage);
    }

    public void sendOtpEmailResetPassword(String email, String token) throws MessagingException {
        // nantinya ganti url disini
        String resetPasswordUrl = "http://localhost:8080/coba.html?resetToken=" + token;
        String htmlContent =  "<div style=\"font-family: Helvetica,Arial,sans-serif;min-width:1000px;overflow:auto;line-height:2\">" +
                "    <div style=\"margin:50px auto;width:80%%;padding:20px 0\">" +
                "        <div style=\"border-bottom:5px solid #eee\">" +
                "            <a href=\"\" style=\"font-size:30px;color: #a115b7;text-decoration:none;font-weight:600\">Binar Belajar</a>" +
                "        </div>" +
                "        <p style=\"font-size:15px\">Hallo <b>Binarian</b><span style=\"font-size:20px\"> 👋</span>,</p>" +
                "        <p>" +
                "            Jangan khawatir jika kamu lupa password! Kami sudah menyediakan cara mudah untuk meresetnya.<br>" +
                "            Klik tombol di bawah untuk mengatur ulang kata sandi akun kamu:" +
                "        </p>" +
                "        <a href=\"" + resetPasswordUrl + "\" style=\"text-decoration: none; background: #00466a; margin: 20px auto; display: block; width: max-content; padding: 8px 20px; color: #fff; border-radius: 4px;\">" +
                "            Reset Password" +
                "        </a>" +
                "        <p style=\"font-size:15px;\">Salam,<br />Team Binar Belajar</p>" +
                "        <hr style=\"border:none;border-top:5px solid #eee\" />" +
                "        <div style=\"float:right;padding:8px 0;color:#aaa;font-size:0.8em;line-height:1;font-weight:300\">" +
                "            <p>" +
                "                Binar Belajar,<br>" +
                "                by Binar Academy" +
                "            </p>" +
                "        </div>" +
                "    </div>" +
                "</div>";

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setTo(email);
        messageHelper.setSubject("Tautan untuk halaman reset password");
        messageHelper.setText(String.format(htmlContent, token), true);

        javaMailSender.send(mimeMessage);
    }

}
