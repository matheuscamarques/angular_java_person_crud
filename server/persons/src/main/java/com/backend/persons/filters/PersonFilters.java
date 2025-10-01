package com.backend.persons.filters;

import com.backend.persons.entities.PersonStatus;

public record PersonFilters(String name, String email, PersonStatus status) {
}