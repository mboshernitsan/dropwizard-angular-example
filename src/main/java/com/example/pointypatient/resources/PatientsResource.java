package com.example.pointypatient.resources;

import java.util.Collection;

import javax.validation.Valid;
import javax.validation.ValidationException;
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

import com.example.pointypatient.core.Patient;
import com.example.pointypatient.db.ObjectIdJSONUtils;
import com.example.pointypatient.db.PatientDAO;
import com.mongodb.WriteResult;

/**
 * A simple JAX-RS service for accessing patients information. Note that @Consumes
 * and @Produces annotations are specified on individual methods, rather than on
 * the whole class. This makes the desired interface more explicit and works
 * around AngularJS idiosyncrasy of not specifying Content-Type: header when
 * request body is absent (e.g., on DELETE operation).
 * 
 * @author mboshernitsan
 * 
 */
@Path("/patients")
public class PatientsResource {
    PatientDAO patientDAO;

    public PatientsResource(PatientDAO patientDAO) {
        this.patientDAO = patientDAO;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Patient> getPatientList() {
        return patientDAO.find().asList();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Patient getPatient(@PathParam("id") String id) {
        Patient p = patientDAO.get(ObjectIdJSONUtils.decode(id));
        if (p == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return p;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Patient createOrUpdatePatient(@Valid Patient patient) {
        patientDAO.save(patient);
        return patient;
    }

    @DELETE
    @Path("/{id}")
    public void deletePatient(@PathParam("id") String id) {
        WriteResult result = patientDAO.deleteById(ObjectIdJSONUtils.decode(id));
        if (result.getN() == 0) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }
}
