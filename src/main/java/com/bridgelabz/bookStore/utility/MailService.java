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

    public void sendSimpleMessage(String email, String messageSub, String messageBody) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("nnalin04@gmail.com");
        mail.setTo(email);
        mail.setSubject(messageSub);
        mail.setText(messageBody);
        javaMailSender.send(mail);
    }

    public void sendResetMessage(String email, String message) throws MailException {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("nnalin04@gmail.com");
        mail.setTo(email);
        mail.setSubject("URL for reset password");
        mail.setText(getResetURL(message));
        javaMailSender.send(mail);
    }

    public void sendCustomerMailWithTokenURL(String email, String message) throws MailException {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("nnalin04@gmail.com");
        mail.setTo(email);
        mail.setSubject("Registration Verification");
        mail.setText(getCustomerVerifiedURL(message));
        javaMailSender.send(mail);
    }

    public void sendSellerMailWithTokenURL(String email, String message) throws MailException {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("nnalin04@gmail.com");
        mail.setTo(email);
        mail.setSubject("Registration Verification");
        mail.setText(getVerifiedURL(message));
        javaMailSender.send(mail);
    }

    private String getCustomerVerifiedURL(String message) {
        return "To verify your BookStore Account please click the link given billow \n"
                +"http://localhost:8080/bookstoreCustomer/verify/"+message;
    }

    private String getVerifiedURL(String message) {
        return "To verify your BookStore Account please click the link given billow \n"
                +"http://localhost:8080/bookstoreSeller/verify/"+message;
    }

    public static String getResetURL(String token) {
        return "To reset your password please click on the link given billow \n" +
                "http://localhost:3000/resetPassword/"+token;
    }
}
