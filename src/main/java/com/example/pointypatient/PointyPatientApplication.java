package com.example.pointypatient;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.net.UnknownHostException;
import java.util.Collections;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.ValidationExtension;

import com.example.pointypatient.db.ManagedMongo;
import com.example.pointypatient.db.MongoHealthCheck;
import com.example.pointypatient.db.ObjectIdJSONUtils;
import com.example.pointypatient.db.PatientDAO;
import com.example.pointypatient.resources.PatientsResource;
import com.fasterxml.jackson.databind.module.SimpleModule;
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
        bootstrap.addBundle(new AssetsBundle("/assets", "/assets", "index.html", "assets"));
        bootstrap.addBundle(new AssetsBundle("/app", "/app", "index.html", "app"));
        bootstrap.addBundle(new AssetsBundle("/META-INF/resources/webjars", "/webjars", null, "webjars"));
    }

    @Override
    public void run(PointyPatientConfiguration configuration, Environment environment) throws ClassNotFoundException,
        UnknownHostException {
        initializeDb(configuration.getDbConfig(), environment);

        environment.jersey().register(new PatientsResource(patientDAO));
    }

    private void initializeDb(DbConfiguration dbConfig, Environment environment) throws UnknownHostException {
        Mongo mongo = new MongoClient(new ServerAddress(dbConfig.getHost(), dbConfig.getPort()),
                                      Collections.singletonList(MongoCredential.createMongoCRCredential(dbConfig
                                          .getUser(), dbConfig.getDBName(), dbConfig.getPassword().toCharArray())));
        environment.lifecycle().manage(new ManagedMongo(mongo));
        environment.healthChecks().register("MongoHealthCheck", new MongoHealthCheck(mongo));

        Morphia morphia = new Morphia();
        new ValidationExtension(morphia);
        Datastore ds = morphia.createDatastore(mongo, dbConfig.getDBName());

        patientDAO = new PatientDAO(ds);

        // call these after all DAOs are created
        ds.ensureIndexes();
        ds.ensureCaps();

        SimpleModule module = new SimpleModule();
        module.addSerializer(ObjectId.class, ObjectIdJSONUtils.serializer);
        module.addDeserializer(ObjectId.class, ObjectIdJSONUtils.deserializer);
        environment.getObjectMapper().registerModule(module);
    }
}
