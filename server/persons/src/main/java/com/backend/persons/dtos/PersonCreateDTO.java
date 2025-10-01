package com.backend.persons.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.backend.persons.entities.PersonStatus;

public record PersonCreateDTO(
    String name,
    String email,
    String phone,
    LocalDate birthDate,
    PersonStatus status,
    BigDecimal balance,
    String currency
) {}