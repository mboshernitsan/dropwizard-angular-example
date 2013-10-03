package com.example.pointypatient.db;

import com.mongodb.Mongo;

import io.dropwizard.lifecycle.Managed;

public class ManagedMongo implements Managed {
    private Mongo mongo;

    public ManagedMongo(Mongo m) {
        this.mongo = m;
    }

    public void start() throws Exception {
    }

    public void stop() throws Exception {
        mongo.close();
    }
}