package com.backend.persons.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity 
@Getter @Setter 
@NoArgsConstructor 
public class Person {

    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO) 
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true) 
    private String email;

    private String phone;

    private LocalDate birthDate;

    @Enumerated(EnumType.STRING) 
    @Column(nullable = false)
    private PersonStatus status;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private String currency;

    @CreationTimestamp 
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp 
    @Column(nullable = false)
    private LocalDateTime updatedAt;


    public Person(String name, String email, String phone, LocalDate birthDate, PersonStatus status, BigDecimal balance, String currency) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.status = status;
        this.balance = balance;
        this.currency = currency;
    }
}