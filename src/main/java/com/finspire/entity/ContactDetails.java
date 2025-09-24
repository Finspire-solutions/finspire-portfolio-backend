package com.finspire.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "contact_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "phone_no")
    private String phoneNo;
    @Column(name = "email")
    private String email;
    @Column(name = "message")
    private String message;
    @Column(name = "interested_service")
    private String interestedService;
    @Column(name = "created_at")
    private LocalDate createdAt;

    @PrePersist()
    public void onCreate(){
        this.createdAt = LocalDate.now();
    }
}
