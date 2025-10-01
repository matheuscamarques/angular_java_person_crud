package com.backend.persons.controllers;

import java.lang.reflect.Array;
import java.net.URI;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.backend.persons.dtos.PersonCreateDTO;
import com.backend.persons.dtos.PersonDTO;
import com.backend.persons.entities.Person;
import com.backend.persons.filters.PersonFilters;
import com.backend.persons.services.PersonService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/persons")
public class PersonsController {

    private final PersonService personService;

    @GetMapping
    public ResponseEntity<Page<PersonDTO>> find(PersonFilters personFilters, Pageable pageable) {
        Page<Person> personPage = personService.findAll(pageable, personFilters);
        Page<PersonDTO> personDtoPage = personPage.map(PersonDTO::fromEntity);
        return ResponseEntity.ok(personDtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDTO> findById(@PathVariable UUID id) {
        return personService.findById(id)
                .map(PersonDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); 
    }

    @PostMapping
    public ResponseEntity<PersonDTO> create(@RequestBody PersonCreateDTO createDto) {
        Person newPerson = personService.create(createDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newPerson.getId())
                .toUri();
        
        return ResponseEntity.created(location).body(PersonDTO.fromEntity(newPerson));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDTO> update(@PathVariable UUID id, @RequestBody PersonCreateDTO updateDto) {
        try {
            Person updatedPerson = personService.update(id, updateDto);
            return ResponseEntity.ok(PersonDTO.fromEntity(updatedPerson));
        } catch (RuntimeException e) { 
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        try {
            personService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}