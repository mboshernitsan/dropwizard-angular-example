package com.example.pointypatient.db;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.Date;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;

import com.example.pointypatient.core.Patient;
import com.example.pointypatient.util.FongoRule;

public class PatientDAOTest {
    private static final Patient p1 = new Patient(null,
                                                  "Don",
                                                  "Pedro",
                                                  "123 Main St",
                                                  "San Francisco",
                                                  "CA",
                                                  new Date(1234567890000L));
    private static final Patient p2 = new Patient(null,
                                                  "Joe",
                                                  "Shmoe",
                                                  "456 Middle Ave",
                                                  "San Jose",
                                                  "CA",
                                                  new Date(1234567890000L));
    private static final Patient p3 = new Patient(null,
                                                  "Holly",
                                                  "Molly",
                                                  "789 B Ave",
                                                  "Sacramento",
                                                  "CA",
                                                  new Date(1234567890000L));
    
    @ClassRule
    public static FongoRule fongo = new FongoRule();
    private PatientDAO dao;
    
    @Before
    public void setUp() throws Exception {
        dao = fongo.getPatientDAO();
        dao.save(p1);
        assertThat(p1.getId()).isNotNull();
        dao.save(p2);
        assertThat(p2.getId()).isNotNull();
        dao.save(p3);
        assertThat(p3.getId()).isNotNull();
        assertThat(dao.count()).isEqualTo(3);
    }
    
    @Test
    public void testDeleteExistingPatient() {
        boolean f = dao.deletePatient(p3.getId());
        assertThat(f).isTrue();
        assertThat(dao.count()).isEqualTo(2);
        assertThat(dao.getPatientList()).doesNotContain(p3);
    }

    @Test
    @Ignore // a bug with remove in Fongo prevents this test from succeeding
    public void testDeleteNonExistingPatient() {
        ObjectId unknown = new ObjectId();
        boolean f = dao.deletePatient(unknown);
        assertThat(f).isFalse();
        assertThat(dao.count()).isEqualTo(2);
    }

    @Test
    public void testGetExistingPatient() {
        Patient p;
        p = dao.get(p1.getId());
        assertThat(p).isEqualTo(p1);
        p = dao.get(p2.getId());
        assertThat(p).isEqualTo(p2);
    }

    @Test
    public void testGetNonExistingPatient() {
        ObjectId unknown = new ObjectId();
        Patient p = dao.get(unknown);
        assertThat(p).isNull();
    }

    @Test
    public void testGetPatientList() {
        Collection<Patient> patientList = dao.getPatientList();
        assertThat(patientList).hasSize(3);
        assertThat(patientList).contains(p1);
        assertThat(patientList).contains(p2);
        assertThat(patientList).contains(p3);
    }

    @Test
    public void testUpdateExistingPatient() {
        Patient p = new Patient(p1.getId(),
                                "Larry",
                                "Leisure-Suit",
                                p1.getAddress(),
                                p1.getCity(),
                                p1.getState(),
                                p1.getVisitDate());
        dao.save(p);
        assertThat(dao.count()).isEqualTo(3);
        Patient updated = dao.get(p1.getId());
        assertThat(updated.getFirstName()).isEqualTo("Larry");
        assertThat(updated.getLastName()).isEqualTo("Leisure-Suit");
    }

}
