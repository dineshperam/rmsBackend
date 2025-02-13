package com.rms.controller;



import com.rms.model.ContactForm;
import com.rms.service.ContactFormService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "*")
public class ContactFormController {

    @Autowired
    private ContactFormService contactFormService;

    @PostMapping("/submit")
    public ContactForm submitContactForm(@RequestBody ContactForm contactForm) {
        return contactFormService.saveContactForm(contactForm);
    }
    
    @GetMapping("/showContacts")
    public List<ContactForm> showContacts(){
    	return contactFormService.showContacts();
    }
    
    @PutMapping("/accept/{contactId}/{adminId}")
    public ResponseEntity<String> acceptContactRequest(
            @PathVariable Long contactId, 
            @PathVariable int adminId) {
        
        String result = contactFormService.acceptContactRequest(contactId, adminId);
        return ResponseEntity.ok(result);
    }
    
    @PutMapping("/reject/{id}")
    public ResponseEntity<String> rejectContact(@PathVariable Long id) {
        boolean isRejected = contactFormService.rejectContact(id);

        if (isRejected) {
            return ResponseEntity.ok("Contact request rejected successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contact request not found");
        }
    }
}
