package com.example.pointypatient;

import io.dropwizard.Configuration;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Configuration data, initialized from the .yml config file on startup. Note
 * the absence of setters: private fields are initialized by Jackson (as
 * signified by the @JsonProperty annotation), but cannot be modified by the
 * application. I like this pattern because it expresses the intent (initialize
 * once, read many).
 * 
 * @author mboshernitsan
 * 
 */
public class PointyPatientConfiguration extends Configuration {
    @Valid
    @JsonProperty
    private DbConfiguration dbConfig;

    public DbConfiguration getDbConfig() {
        return dbConfig;
    }
}
