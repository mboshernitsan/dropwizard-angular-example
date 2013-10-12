package com.example.pointypatient.db;

import java.util.Collection;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import com.example.pointypatient.core.Patient;
import com.mongodb.WriteResult;

public class PatientDAO extends BasicDAO<Patient, ObjectId> {
    public PatientDAO(Datastore ds) {
        super(ds);
    }

    public Collection<Patient> getPatientList() {
        return find().asList();
    }

    public boolean deletePatient(ObjectId id) {
        WriteResult result = deleteById(id);
        return result.getN() == 1;
    }
}
