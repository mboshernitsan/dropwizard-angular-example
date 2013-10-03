package com.example.pointypatient.core;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Length;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A simple Patient value object. Note the absence of setters: private fields
 * are initialized by Jackson when the object is tranferred from a client (using
 * REST) or by Morphia when the object is transferred from the database. As far
 * as the application concerned, the object is immutable, so the setters are not
 * necessary.
 * 
 * @author mboshernitsan
 * 
 */
@Entity
public class Patient {
    @Id
    @JsonProperty
    private ObjectId id;

    @Length(min = 1, max = 256)
    @JsonProperty
    private String firstName;

    @Length(min = 1, max = 256)
    @JsonProperty
    private String lastName;

    @Length(min = 1, max = 256)
    @JsonProperty
    private String address;

    @Length(min = 1, max = 256)
    @JsonProperty
    private String city;

    @Length(min = 1, max = 2)
    @JsonProperty
    private String state;

    @NotNull
    @JsonProperty
    private Date visitDate;

    public ObjectId getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public Date getVisitDate() {
        return visitDate;
    }
}
