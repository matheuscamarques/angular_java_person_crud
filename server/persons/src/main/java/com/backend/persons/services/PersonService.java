package com.backend.persons.services;

import com.backend.persons.dtos.PersonCreateDTO;
import com.backend.persons.entities.Person;
import com.backend.persons.repositories.PersonRepository;
import com.backend.persons.repositories.specifications.PersonSpecification; 
import com.backend.persons.filters.PersonFilters; 
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;

    public Page<Person> findAll(Pageable pageable, PersonFilters personFilters) {
        Specification<Person> spec = PersonSpecification.build(personFilters);
        return personRepository.findAll(spec, pageable);
    }

     public Optional<Person> findById(UUID id) {
        return personRepository.findById(id);
    }

    public Person create(PersonCreateDTO dto) {
        Person newPerson = new Person(dto.name(), dto.email(), dto.phone(), dto.birthDate(), dto.status(), dto.balance(), dto.currency());
        return personRepository.save(newPerson);
    }

    public Person update(UUID id, PersonCreateDTO dto) {
        Person existingPerson = personRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Person not found with id: " + id)); 

        existingPerson.setName(dto.name());
        existingPerson.setEmail(dto.email());
        
        return personRepository.save(existingPerson);
    }

    public void deleteById(UUID id) {
        if (!personRepository.existsById(id)) {
            throw new RuntimeException("Person not found with id: " + id);
        }
        personRepository.deleteById(id);
    }

}