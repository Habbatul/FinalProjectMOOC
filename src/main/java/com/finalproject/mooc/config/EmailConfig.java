package com.finalproject.mooc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Value("${email.account}")
    private String emailAccount;

    @Value("${email.password}")
    private String emailPassword;

    @Value("${email.host}")
    private String host;

    @Value("${email.port}")
    private String port;

    @Value("${email.auth}")
    private String isAuth;

    @Value("${email.tls}")
    private String isTLS;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(Integer.parseInt(port));
        mailSender.setUsername(emailAccount);
        mailSender.setPassword(emailPassword);

        Properties prop = mailSender.getJavaMailProperties();
        prop.put("mail.smtp.auth", isAuth);
        prop.put("mail.smtp.starttls.enable", isTLS);

        return mailSender;
    }
}
