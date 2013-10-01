package com.example.helloworld.health;

import com.codahale.metrics.health.HealthCheck;
import com.example.helloworld.resources.PatientsResource;

public class PatientsResourceHealthCheck extends HealthCheck {
    private PatientsResource patientsResource;

    public PatientsResourceHealthCheck(PatientsResource patientsResource) {
        super();
        this.patientsResource = patientsResource;
    }

    @Override
    protected Result check() throws Exception {
        if (patientsResource.getPatientCount() > 0) {
            return Result.healthy();
        } else {
            return Result.unhealthy("All patients deleted!");
        }
    }
}
