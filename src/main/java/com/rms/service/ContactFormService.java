package com.rms.service;

import com.rms.model.ContactForm;
import com.rms.model.UserDetails;
import com.rms.repository.ContactFormRepository;
import com.rms.repository.UserDetailsRepository;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ContactFormService {

    @Autowired
    private ContactFormRepository contactFormRepository;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private UserDetailsRepository userDetailsRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public ContactForm saveContactForm(ContactForm contactForm) {
        contactForm.setStatus("Pending");
        ContactForm savedForm = contactFormRepository.save(contactForm);

        // Send confirmation email
        emailService.sendContactFormConfirmation(contactForm.getEmail(), contactForm.getFirstname());

        return savedForm;
    }
    
    public List<ContactForm> showContacts() {
    	return contactFormRepository.findAll();
    }
    
    @Transactional
    public String acceptContactRequest(Long contactId, int adminId) {
        Optional<ContactForm> contactOpt = contactFormRepository.findById(contactId);

        if (contactOpt.isPresent()) {
            ContactForm contact = contactOpt.get();
            
            // Check if email already exists in UserDetails
            Optional<UserDetails> existingUser = userDetailsRepository.findByEmail(contact.getEmail());
            if (existingUser.isPresent()) {
                return "Email already exists. Cannot create a new user.";
            }
            
            contact.setStatus("Accepted");
            contactFormRepository.save(contact);

            // Create a new UserDetails entry
            UserDetails newUser = new UserDetails();
            newUser.setFirstName(contact.getFirstname());
            newUser.setLastName(contact.getLastname());
            newUser.setEmail(contact.getEmail());
            newUser.setMobileNo(contact.getMobileno());
            newUser.setRole(contact.getRole());

            // Generate username and dummy password
            String username = contact.getFirstname().toLowerCase() + contact.getLastname().toLowerCase();
            String email = contact.getEmail();
            String dummyPassword = generateDummyPassword(10);
            newUser.setEmail(email);
            newUser.setUsername(username);
            newUser.setPassword(passwordEncoder.encode(dummyPassword));
            newUser.setPasswordHash(dummyPassword);
            newUser.setFirstLogin(true);
            newUser.setActive(true);

            // Set manager ID as admin ID
            newUser.setManagerId(0);

            // Save new user
            userDetailsRepository.save(newUser);
            
            emailService.sendWelcomeEmail(contact.getEmail(), contact.getFirstname(),username, contact.getRole(),email, dummyPassword);

            return "Contact request accepted, user created, and email sent.";
        } else {
            return "Contact request not found.";
        }

    }
    
    private String generateDummyPassword(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }
        return password.toString();
    }
    
    public boolean rejectContact(Long id) {
        Optional<ContactForm> contactRequest = contactFormRepository.findById(id);

        if (contactRequest.isPresent()) {
            ContactForm contact = contactRequest.get();
            contact.setStatus("Rejected");
            contactFormRepository.save(contact);
            return true; // Successfully updated
        }
        return false; // Contact request not found
    }
}