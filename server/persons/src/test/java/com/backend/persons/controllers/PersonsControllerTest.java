package com.backend.persons.controllers;

import com.backend.persons.dtos.PersonCreateDTO;
import com.backend.persons.entities.Person;
import com.backend.persons.entities.PersonStatus;
import com.backend.persons.services.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;

// Usando @WebMvcTest para focar nos testes da camada web (controller)
// e desabilitar a configuração automática completa do Spring Boot.
@WebMvcTest(PersonsController.class)
class PersonsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // @MockBean cria um mock do PersonService e o injeta no contexto do Spring
    @MockBean
    private PersonService personService;

    private Person person;
    private PersonCreateDTO personCreateDTO;
    private UUID personId;

    @BeforeEach
    void setUp() {
        personId = UUID.randomUUID();
        
        person = new Person();
        person.setId(personId);
        person.setName("Matheus");
        person.setEmail("matheus@test.com");
        person.setStatus(PersonStatus.ACTIVE);
        person.setBalance(BigDecimal.TEN);
        person.setCurrency("BRL");
        person.setCreatedAt(LocalDateTime.now());
        person.setUpdatedAt(LocalDateTime.now());
        
        personCreateDTO = new PersonCreateDTO("Matheus", "matheus@test.com", null, null, PersonStatus.ACTIVE, BigDecimal.TEN, "BRL");
    }

    @Test
    @DisplayName("Deve criar uma pessoa com dados válidos e retornar status 201 Created")
    void whenCreatePerson_withValidData_shouldReturnCreated() throws Exception {
        given(personService.create(any(PersonCreateDTO.class))).willReturn(person);

        mockMvc.perform(post("/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(person.getName())))
                .andExpect(jsonPath("$.email", is(person.getEmail())))
                .andExpect(header().string("Location", "http://localhost/persons/" + personId));
    }

    @Test
    @DisplayName("Deve encontrar uma pessoa por um ID existente e retornar status 200 OK")
    void whenFindById_withExistingId_shouldReturnPerson() throws Exception {
        given(personService.findById(personId)).willReturn(Optional.of(person));

        mockMvc.perform(get("/persons/{id}", personId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(personId.toString())))
                .andExpect(jsonPath("$.name", is(person.getName())));
    }

    @Test
    @DisplayName("Deve retornar status 404 Not Found ao buscar por um ID inexistente")
    void whenFindById_withNonExistingId_shouldReturnNotFound() throws Exception {
        given(personService.findById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get("/persons/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @DisplayName("Deve listar pessoas de forma paginada e retornar status 200 OK")
    void whenFindAll_shouldReturnPersonPage() throws Exception {
        Page<Person> personPage = new PageImpl<>(Collections.singletonList(person), PageRequest.of(0, 10), 1);
        given(personService.findAll(any(), any())).willReturn(personPage);

        mockMvc.perform(get("/persons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", is(personId.toString())))
                .andExpect(jsonPath("$.totalElements", is(1)));
    }
    
    @Test
    @DisplayName("Deve atualizar uma pessoa com um ID existente e retornar status 200 OK")
    void whenUpdate_withExistingId_shouldReturnUpdatedPerson() throws Exception {
        given(personService.update(any(UUID.class), any(PersonCreateDTO.class))).willReturn(person);

        mockMvc.perform(put("/persons/{id}", personId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(personId.toString())))
                .andExpect(jsonPath("$.name", is(person.getName())));
    }
    
    @Test
    @DisplayName("Deve retornar status 404 Not Found ao tentar atualizar uma pessoa com ID inexistente")
    void whenUpdate_withNonExistingId_shouldReturnNotFound() throws Exception {
        // Simulando a exceção que o serviço lançaria, que o controller traduz para 404
        given(personService.update(any(UUID.class), any(PersonCreateDTO.class)))
            .willThrow(new RuntimeException("Person not found"));

        mockMvc.perform(put("/persons/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personCreateDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve deletar uma pessoa com um ID existente e retornar status 204 No Content")
    void whenDelete_withExistingId_shouldReturnNoContent() throws Exception {
        // Para métodos void, usamos doNothing()
        doNothing().when(personService).deleteById(personId);

        mockMvc.perform(delete("/persons/{id}", personId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar status 404 Not Found ao tentar deletar uma pessoa com ID inexistente")
    void whenDelete_withNonExistingId_shouldReturnNotFound() throws Exception {
        // Simulando a exceção que o serviço lançaria
        doThrow(new RuntimeException("Person not found")).when(personService).deleteById(any(UUID.class));

        mockMvc.perform(delete("/persons/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}
