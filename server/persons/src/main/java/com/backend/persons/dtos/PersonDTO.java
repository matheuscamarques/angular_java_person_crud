package com.backend.persons.dtos;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.backend.persons.entities.Person;
import com.backend.persons.entities.PersonStatus;

public record PersonDTO(
    UUID id,
    String name,
    String email,
    String phone,
    LocalDate birthDate,
    PersonStatus status,
    BigDecimal balance,
    String currency,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static PersonDTO fromEntity(Person person) {
        return new PersonDTO(
            person.getId(),
            person.getName(),
            person.getEmail(),
            person.getPhone(),
            person.getBirthDate(),
            person.getStatus(),
            person.getBalance(),
            person.getCurrency(),
            person.getCreatedAt(),
            person.getUpdatedAt()
        );
    }
}