package com.backend.persons.repositories.specifications;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.backend.persons.entities.Person;
import com.backend.persons.filters.PersonFilters;

import java.util.ArrayList;
import java.util.List;

public class PersonSpecification {

    public static Specification<Person> build(PersonFilters filters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(filters.name())) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")), 
                    "%" + filters.name().toLowerCase() + "%"  
                ));
            }

           
            if (StringUtils.hasText(filters.email())) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("email")),
                    "%" + filters.email().toLowerCase() + "%"
                ));
            }
            
       
            if (filters.status() != null) {
                predicates.add(criteriaBuilder.equal(
                    root.get("status"), filters.status()
                ));
            }
            
         
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}