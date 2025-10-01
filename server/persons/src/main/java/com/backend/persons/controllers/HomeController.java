package com.backend.persons.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.persons.HealtCheck;

@RestController
public class HomeController {
    @GetMapping("/")
    public HealtCheck healtcheck() {
        return HealtCheck.call();
    }
}
