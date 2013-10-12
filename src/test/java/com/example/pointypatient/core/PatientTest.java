package com.example.pointypatient.core;

import static com.example.pointypatient.util.TestUtil.jsonFromObject;
import static com.example.pointypatient.util.TestUtil.objectFromJson;
import static com.example.pointypatient.util.TestUtil.jsonFromFile;
import static com.example.pointypatient.util.TestUtil.sameJsonAs;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.Test;

import com.example.pointypatient.db.MongoUtils;

public class PatientTest {
    @Test
    public void deserializesFromJson() throws Exception {
        final Patient patient = objectFromJson(jsonFromFile("fixtures/patient1.json"), Patient.class);
        
        assertThat(patient.getFirstName()).isEqualTo("Don");
        assertThat(patient.getLastName()).isEqualTo("Pedro");
        assertThat(patient.getVisitDate()).isEqualTo(new Date(1234567890000L));
    }

    @Test
    public void serializesToJson() throws Exception {
        Patient patient = new Patient(null,
                                      "Don",
                                      "Pedro",
                                      "123 Main St",
                                      "San Francisco",
                                      "CA",
                                      new Date(1234567890000L));

        assertThat(jsonFromObject(patient)).is(sameJsonAs(jsonFromFile("fixtures/patient1.json")));
    }
}
