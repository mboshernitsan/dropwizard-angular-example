package com.example.helloworld.core;

import java.util.Date;

import org.hibernate.validator.constraints.NotEmpty;

public class Patient {
    public Long id = null;

    @NotEmpty
    public String firstName;
    @NotEmpty
    public String lastName;
    @NotEmpty
    public String address;
    @NotEmpty
    public String city;
    @NotEmpty
    public String state;

    public Date visitDate;

    public Patient() {
    }

    public Patient(Long id, String firstName, String lastName, Date visitDate, String address, String city, String state) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.visitDate = visitDate;
        this.address = address;
        this.city = city;
        this.state = state;
    }
}
