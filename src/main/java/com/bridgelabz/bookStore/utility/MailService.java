package com.bridgelabz.bookStore.utility;

import com.bridgelabz.bookStore.customer.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendSimpleMessage(UserDTO userDTO, String message) throws MailException {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("nnalin04@gmail.com");
        mail.setTo(userDTO.getEmail());
        mail.setSubject("URL for reset password");
        mail.setText(getResetURL(message));
        javaMailSender.send(mail);
    }

    public void sendMailWithTokenURL(String email, String message) throws MailException {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("nnalin04@gmail.com");
        mail.setTo(email);
        mail.setSubject("Registration Verification");
        mail.setText(getVerifiedURL(message));
        javaMailSender.send(mail);
    }

    private String getVerifiedURL(String message) {
        return "To verify your BookStore Account please click the link given billow \n"
                +"http://localhost:8080/parkinglotuser/"+message;
    }

    public static String getResetURL(String token) {
        return "To reset your password please click on the link given billow \n" +
                "http://localhost:3000/resetpassword/"+token;
    }
}
