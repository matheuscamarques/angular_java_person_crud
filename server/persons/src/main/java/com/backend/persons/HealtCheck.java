package com.backend.persons;

import lombok.Getter;

@Getter
public class HealtCheck {
    public  Integer version = 1;
    private String applicationName = "persons";
    private String status = "UP";

    public static HealtCheck call() {
       return new HealtCheck();
    }
}
