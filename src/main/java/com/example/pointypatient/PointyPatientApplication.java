package com.example.pointypatient;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.lifecycle.ServerLifecycleListener;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.net.UnknownHostException;
import java.util.Collections;

import org.eclipse.jetty.server.Server;
import org.mongodb.morphia.Datastore;

import com.example.pointypatient.db.ManagedMongo;
import com.example.pointypatient.db.MongoHealthCheck;
import com.example.pointypatient.db.MongoUtils;
import com.example.pointypatient.db.PatientDAO;
import com.example.pointypatient.resources.PatientsResource;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class PointyPatientApplication extends Application<PointyPatientConfiguration> {
    private PatientDAO patientDAO;

    public static void main(String[] args) throws Exception {
        new PointyPatientApplication().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<PointyPatientConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/app", "/app", "index.html", "app"));
        bootstrap.addBundle(new AssetsBundle("/META-INF/resources/webjars", "/webjars", null, "webjars"));
        bootstrap.setConfigurationSourceProvider(new ClasspathConfigurationSourceProvider());
    }

    @Override
    public void run(PointyPatientConfiguration configuration, Environment environment) throws ClassNotFoundException,
        UnknownHostException {
        initializeDb(configuration.getDbConfig(), environment);

        environment.jersey().register(new PatientsResource(patientDAO));
        environment.lifecycle().addServerLifecycleListener(new ServerLifecycleListener() {
            @Override
            public void serverStarted(Server server) {
                server.setStopTimeout(0);
            }
        });
    }

    private void initializeDb(DbConfiguration dbConfig, Environment environment) throws UnknownHostException {
        Mongo mongo;
        if (dbConfig.getUser() != null) {
            // create authenticated connection
            mongo = new MongoClient(new ServerAddress(dbConfig.getHost(), dbConfig.getPort()),
                                    Collections.singletonList(MongoCredential.createMongoCRCredential(dbConfig
                                        .getUser(), dbConfig.getDBName(), dbConfig.getPassword().toCharArray())));
        } else {
            // create unauthenticated connection
            mongo = new MongoClient(new ServerAddress(dbConfig.getHost(), dbConfig.getPort()));
        }
        environment.lifecycle().manage(new ManagedMongo(mongo));
        environment.healthChecks().register("MongoHealthCheck", new MongoHealthCheck(mongo));

        Datastore ds = MongoUtils.createDatastore(mongo, dbConfig.getDBName());

        patientDAO = new PatientDAO(ds);

        // call these after all DAOs are created
        ds.ensureIndexes();
        ds.ensureCaps();

        MongoUtils.configureObjectMapper(environment.getObjectMapper());
    }
}
