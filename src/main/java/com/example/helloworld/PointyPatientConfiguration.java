package com.example.helloworld;

import io.dropwizard.Configuration;

import javax.validation.Valid;

import com.example.helloworld.core.Patient;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PointyPatientConfiguration extends Configuration {
    @Valid
    private Patient defaultPatient;

    @JsonProperty
    public Patient getDefaultPatient() {
        return defaultPatient;
    }

    @JsonProperty
    public void setDefaultPatient(Patient defaultPatient) {
        this.defaultPatient = defaultPatient;
    }
}
