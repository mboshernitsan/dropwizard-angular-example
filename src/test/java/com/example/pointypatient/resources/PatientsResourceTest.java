package com.example.pointypatient.resources;

import static com.example.pointypatient.util.TestUtil.expectWebApplicationException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.example.pointypatient.core.Patient;
import com.example.pointypatient.db.MongoUtils;
import com.example.pointypatient.db.PatientDAO;

public class PatientsResourceTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private PatientDAO dao = mock(PatientDAO.class);
    private PatientsResource pr = new PatientsResource(dao);

    @Test
    public void testGetEmptyPatientList() {
        when(dao.getPatientList()).thenReturn(new ArrayList<Patient>());
        Collection<Patient> list = pr.getPatientList();
        assertThat(list).isEmpty();
    }

    @Test
    public void testGetNonEmptyPatientList() {
        Patient p1 = new Patient();
        Patient p2 = new Patient();
        when(dao.getPatientList()).thenReturn(Arrays.asList(p1, p2));
        Collection<Patient> list = pr.getPatientList();
        assertThat(list).hasSize(2);
        assertThat(list).contains(p1);
        assertThat(list).contains(p2);
    }

    @Test
    public void testGetPatientById() {
        Patient expected = new Patient(new ObjectId(), null, null, null, null, null, null);
        when(dao.get(expected.getId())).thenReturn(expected);
        Patient p = pr.getPatient(MongoUtils.encodeObjectId(expected.getId()));
        assertThat(p).isSameAs(expected);
    }

    @Test
    public void testGetPatientByUnknownId() {
        expectWebApplicationException(thrown, Response.Status.NOT_FOUND);
        ObjectId unknown = new ObjectId();
        when(dao.get(unknown)).thenReturn(null);
        pr.getPatient(MongoUtils.encodeObjectId(unknown));
    }

    @Test
    public void testGetPatientByIllFormedId() {
        expectWebApplicationException(thrown, Response.Status.BAD_REQUEST);
        pr.getPatient("garbage");
    }

    @Test
    public void testCreateOrUpdatePatient() {
        Patient p = new Patient();
        assertThat(pr.createOrUpdatePatient(p)).isSameAs(p);
        verify(dao).save(p);
    }

    @Test
    public void testDeletePatientById() {
        Patient expected = new Patient(new ObjectId(), null, null, null, null, null, null);
        when(dao.deletePatient(expected.getId())).thenReturn(true);
        pr.deletePatient(MongoUtils.encodeObjectId(expected.getId()));
    }

    @Test
    public void testDeletePatientPatientByUnknownId() {
        expectWebApplicationException(thrown, Response.Status.NOT_FOUND);
        ObjectId unknown = new ObjectId();
        when(dao.deletePatient(unknown)).thenReturn(false);
        pr.deletePatient(MongoUtils.encodeObjectId(unknown));
    }

    @Test
    public void testDeletePatientByIllFormedId() {
        expectWebApplicationException(thrown, Response.Status.BAD_REQUEST);
        pr.deletePatient("garbage");
    }
}
