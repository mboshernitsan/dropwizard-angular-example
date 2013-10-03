package com.example.pointypatient.db;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import com.example.pointypatient.core.Patient;

public class PatientDAO extends BasicDAO<Patient, ObjectId> {
    public PatientDAO(Datastore ds) {
        super(ds);
    }
}
