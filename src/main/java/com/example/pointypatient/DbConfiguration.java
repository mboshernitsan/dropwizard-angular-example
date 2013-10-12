package com.example.pointypatient;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Database configuration data. Note the absence of setters; see
 * {@link PointyPatientConfiguration} for details.
 * 
 * @author mboshernitsan
 * 
 */
public class DbConfiguration {
    @NotEmpty
    @JsonProperty
    private String dbName;

    @Min(1)
    @Max(65535)
    @JsonProperty
    private int port;

    @NotEmpty
    @JsonProperty
    private String host;

    @JsonProperty
    private String user;

    @JsonProperty
    private String password;


    public String getPassword() {
        return password;
    }

    public String getDBName() {
        return dbName;
    }

    public String getUser() {
        return user;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

}
