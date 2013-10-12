package com.example.pointypatient.integration;

import static com.example.pointypatient.util.TestUtil.dbObjectFromJson;
import static com.example.pointypatient.util.TestUtil.expectUniformInterfaceException;
import static com.example.pointypatient.util.TestUtil.jsonFromFile;
import static com.example.pointypatient.util.TestUtil.listFromJson;
import static com.example.pointypatient.util.TestUtil.objectFromJson;
import static org.assertj.core.api.Assertions.assertThat;
import io.dropwizard.testing.junit.DropwizardAppRule;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

import com.example.pointypatient.PointyPatientApplication;
import com.example.pointypatient.PointyPatientConfiguration;
import com.example.pointypatient.core.Patient;
import com.example.pointypatient.db.MongoUtils;
import com.example.pointypatient.util.EmbeddedMongoRule;
import com.mongodb.DBCollection;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class PointyPatientApplicationTest {
    public static EmbeddedMongoRule mongo = new EmbeddedMongoRule();
    public static DropwizardAppRule<PointyPatientConfiguration> application = new DropwizardAppRule<PointyPatientConfiguration>(PointyPatientApplication.class,
                                                                                                                                "integration.yml");

    @ClassRule
    public static TestRule initialization = RuleChain.outerRule(mongo).around(application);

    private DBCollection patientsCollection;

    @Before
    public void setUp() throws Exception {
        patientsCollection = mongo.getDb().getCollection("Patient");
        patientsCollection.save(dbObjectFromJson(PATIENT1));
        patientsCollection.save(dbObjectFromJson(PATIENT2));
    }

    @After
    public void tearDown() throws Exception {
        patientsCollection.drop();
    }

    public WebResource app = new Client().resource("http://localhost:" + application.getLocalPort() + "/app");
    public WebResource webjars = new Client().resource("http://localhost:" + application.getLocalPort() + "/webjars");
    public WebResource patients = new Client().resource("http://localhost:" + application.getLocalPort() + "/patients");

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private static final String PATIENT1 = jsonFromFile("fixtures/patient1.json");
    private static final String PATIENT2 = jsonFromFile("fixtures/patient2.json");
    private static final String PATIENT3 = jsonFromFile("fixtures/patient3.json");

    @Test
    public void testGetIndex() {
        String content = app.path("index.html").get(String.class);
        assertThat(content).startsWith("<!doctype html>");
    }

    @Test
    public void testGet404() {
        expectUniformInterfaceException(thrown, ClientResponse.Status.NOT_FOUND);
        app.path("garbage").get(String.class);
    }

    @Test
    public void testGetPatientList() throws Exception {
        List<Patient> list = listFromJson(patients.get(String.class), Patient.class);
        assertThat(list).hasSize(2);
        assertThat(list).contains(objectFromJson(PATIENT1, Patient.class));
        assertThat(list).contains(objectFromJson(PATIENT2, Patient.class));
    }

    @Test
    public void testCreatedPatientAppearsOnTheList() throws Exception {
        patients.type(MediaType.APPLICATION_JSON_TYPE).post(PATIENT3);
        List<Patient> list = listFromJson(patients.get(String.class), Patient.class);
        assertThat(list).hasSize(3);
        assertThat(list).contains(objectFromJson(PATIENT3, Patient.class));        
    }

    @Test
    public void testDeletedPatientDisappearsFromTheList() throws Exception {
        List<Patient> list = listFromJson(patients.get(String.class), Patient.class);
        assertThat(list).hasSize(2);
        Patient toDelete = list.get(0);
        patients.path(MongoUtils.encodeObjectId(toDelete.getId())).delete();
        list = listFromJson(patients.get(String.class), Patient.class);
        assertThat(list).hasSize(1);
        assertThat(list).doesNotContain(toDelete);        
    }
}
