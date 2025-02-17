package com.rms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset OTP");
        message.setText("Your OTP for password reset is: " + otp + ". It is valid for 5 minutes.");

        mailSender.send(message);
    }
    
    public void sendContactFormConfirmation(String to, String firstname) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Thank You for Contacting Royal Mint");
        message.setText("Dear " + firstname + ",\n\n"
                + "Thank you for reaching out to Royal Mint. We've received your information and will contact you shortly.\n\n"
                + "Best Regards,\n"
                + "Royal Mint Team");

        mailSender.send(message);
    }
    
    public void sendWelcomeEmail(String to, String firstname, String role, String email, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Welcome to Royal Mint - Your Account Details");
        
        String roleText = role.equalsIgnoreCase("Artist") ? "an Artist" : "a Manager";

        String emailBody = "Dear " + firstname + ",\n\n"
                + "🎉 Welcome to Royal Mint! We are absolutely thrilled to have you onboard as " + roleText + ". 🎶\n\n"
                + "Your account has been successfully created, and you can now log in using the credentials below:\n\n"
                + "🔹 **Email:** " + email + "\n"
                + "🔹 **Password:** " + password + "\n\n"
                + "**Next Steps:**\n"
                + "✔️ Please log in at [Royal Mint Portal](http://your-app-url.com) and change your password for security.\n"
                + "✔️ After logging in, we encourage you to update your profile details if needed.\n\n"
                + "We look forward to seeing your contributions and making great music together!\n\n"
                + "Best Regards,\n"
                + "🎵 Royal Mint Team 🎵";

        message.setText(emailBody);
        mailSender.send(message);
    }
    
    // Email to RECEIVER when they receive a payment
    public void sendPaymentReceivedEmail(String to, String name, double amount) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Royalty Payment Received");
        
        String emailBody = "Dear " + name + ",\n\n"
                + "🎉 You have received a royalty payment!\n\n"
                + "💰 **Amount Received:** $" + amount + "\n"
                + "📅 **Transaction Date:** " + new java.util.Date() + "\n\n"
                + "Please check your account for further details.\n\n"
                + "Best Regards,\n"
                + "🎵 Royal Mint Team 🎵";

        message.setText(emailBody);
        mailSender.send(message);
    }

    // Email to SENDER (Admin) when they send a payment
    public void sendPaymentSentEmail(String to, String name, double amount, String recipientName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Royalty Payment Sent");
        
        String emailBody = "Dear " + name + ",\n\n"
                + "📢 A royalty payment has been successfully processed.\n\n"
                + "💰 **Amount Sent:** $" + amount + "\n"
                + "🎤 **Recipient:** " + recipientName + "\n"
                + "📅 **Transaction Date:** " + new java.util.Date() + "\n\n"
                + "Best Regards,\n"
                + "🎵 Royal Mint Team 🎵";

        message.setText(emailBody);
        mailSender.send(message);
    }
}
