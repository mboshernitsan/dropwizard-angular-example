package com.example.helloworld;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import com.example.helloworld.health.PatientsResourceHealthCheck;
import com.example.helloworld.resources.PatientsResource;

public class PointyPatientApplication extends Application<PointyPatientConfiguration> {
    public static void main(String[] args) throws Exception {
        new PointyPatientApplication().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<PointyPatientConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets", "/assets", "index.html", "assets"));
        bootstrap.addBundle(new AssetsBundle("/app", "/app", "index.html", "app"));
        bootstrap.addBundle(new AssetsBundle("/META-INF/resources/webjars", "/webjars", null, "webjars"));
    }

    @Override
    public void run(PointyPatientConfiguration configuration,
                    Environment environment) throws ClassNotFoundException {
        PatientsResource pr = new PatientsResource(configuration.getDefaultPatient());

        environment.healthChecks().register("Patients Resource", new PatientsResourceHealthCheck(pr));
        environment.jersey().register(pr);
    }
}
