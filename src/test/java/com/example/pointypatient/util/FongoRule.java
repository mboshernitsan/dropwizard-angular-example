package com.example.pointypatient.util;

import org.junit.rules.ExternalResource;
import org.mongodb.morphia.Datastore;

import com.example.pointypatient.db.MongoUtils;
import com.example.pointypatient.db.PatientDAO;
import com.foursquare.fongo.Fongo;

public class FongoRule extends ExternalResource {
    private PatientDAO patientDAO;

    @Override
    protected void before() throws Throwable {
        Fongo fongo = new Fongo("inMemoryServer");
        Datastore ds = MongoUtils.createDatastore(fongo.getMongo(), "testDB");
        patientDAO = new PatientDAO(ds);
    }

    public PatientDAO getPatientDAO() {
        return patientDAO;
    }
}
