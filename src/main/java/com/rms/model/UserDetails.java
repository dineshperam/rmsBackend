package com.rms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Users")
@Builder
public class UserDetails {

    @Id
    @Column(name = "user_id")
    private int userid;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "mobile_no")
    private String mobileNo;

    @Column(name = "address")
    private String address;

    @Column(name = "role")
    private String role;

    @Column(name = "password_hash")
    private String passwordHash;
    
    @Column(name="password")
    private String password;
    
    @Column(name="manager_id")
    private int managerId;
    
    @Column(name="is_active")
    private boolean isActive;
    
    @Column(name = "first_login")
    private boolean firstLogin;
}
