package com.example.helloworld.resources;

import io.dropwizard.jersey.params.LongParam;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.example.helloworld.core.Patient;

@Path("/patients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PatientsResource {
    private long lastId = 1;
    private Map<Long, Patient> patients = new TreeMap<Long, Patient>();

    public PatientsResource(Patient defaultPatient) {
        defaultPatient.id = lastId++;
        defaultPatient.visitDate = new Date(0);
        patients.put(defaultPatient.id, defaultPatient);
    }

    @GET
    public Collection<Patient> getPatientList() {
        return patients.values();
    }

    @GET
    @Path("/{id}")
    public Patient getPatient(@PathParam("id") LongParam id) {
        Patient p = patients.get(id.get());
        if (p == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return p;
    }

    @POST
    public Patient createOrUpdatePatient(Patient patient) {
        if (patient.id == null) {
            // creating
            patient.id = lastId++;
        } else {
            // updating
        }
        patients.put(patient.id, patient);
        return patient;
    }

    @DELETE
    @Path("/{id}")
    public void deletePatient(@PathParam("id") LongParam id) {
        Patient p = patients.remove(id.get());
        if (p == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }

    public int getPatientCount() {
        return patients.size();
    }
}
