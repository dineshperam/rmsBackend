package com.rms.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contactform")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;
    private String lastname;
    private String email;
    private String mobileno;
    private String query;
    private String role;

    @Column(columnDefinition = "VARCHAR(50) DEFAULT 'Pending'")
    private String status;
}
