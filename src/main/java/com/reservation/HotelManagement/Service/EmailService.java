package com.reservation.HotelManagement.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendConfirmationEmail(String to, String firstName, Long reservationId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Venue Reservation Confirmation");
        message.setText("Dear " + firstName + ",\n\n" +
                "Your reservation (ID: " + reservationId + ") has been confirmed.\n" +
                "Thank you for choosing us!\n\n" +
                "Best Regards,\n" +
                "The Hotel Management Team");
        emailSender.send(message);
    }
}