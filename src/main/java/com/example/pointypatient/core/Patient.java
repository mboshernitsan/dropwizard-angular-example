package com.example.pointypatient.core;

import static com.google.common.base.Objects.equal;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Length;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import com.example.pointypatient.core.utils.Utils;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

/**
 * A simple Patient value object. Note the absence of setters: private fields
 * are initialized by Jackson when the object is tranferred from a client (using
 * REST) or by Morphia when the object is transferred from the database. As far
 * as the application logic concerned, the object is immutable, so the setters
 * are not necessary.
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

    public Patient() {
    }

    public Patient(ObjectId id,
                   String firstName,
                   String lastName,
                   String address,
                   String city,
                   String state,
                   Date visitDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.state = state;
        this.visitDate = visitDate;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Patient) {
            Patient other = (Patient) obj;
            return equal(firstName, other.firstName)
                   && equal(lastName, other.lastName)
                   && equal(address, other.address)
                   && equal(city, other.city)
                   && equal(state, other.state)
                   && equal(visitDate, other.visitDate);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(firstName, lastName, address, city, state, visitDate);
    }
    
    @Override
    public String toString() {        
        return Utils.toString(this);
    }
}
