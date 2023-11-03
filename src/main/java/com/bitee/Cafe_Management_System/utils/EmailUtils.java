package com.bitee.Cafe_Management_System.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailUtils {

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(String to,String subject, String text, List<String> list){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("calebduniya45@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        if(list !=null && list.size() >0) {
            message.setCc(getCcArray(list));
            emailSender.send(message);
        }
    }
    public void forgetMail(String to,String subject,String password) throws MessagingException{
        String htmlMessage = "<p>Your login details for Cafe Management are </b><br></br>Email: "+ to+"</b><br></br> " +
                "Password: " + password+" </b><br></br> <a href=\"http://localhost:4200/\">Click here to  login</a></p>"+
                " </p>";
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);
        helper.setFrom("calebduniya45@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        message.setContent(htmlMessage,"text/html");
        emailSender.send(message);


    }

    private String[] getCcArray(List<String> ccList){
        String[] cc = new String[ccList.size()];
        for (int i=0; i< ccList.size();i++){
            cc[i] = ccList.get(i);
        }
        return cc;
    }
}
