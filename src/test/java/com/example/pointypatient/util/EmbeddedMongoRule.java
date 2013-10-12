package com.example.pointypatient.util;

import org.junit.rules.ExternalResource;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

public class EmbeddedMongoRule extends ExternalResource {
    private MongodProcess mongoProcess;
    private Mongo mongo;
    private DB db;

    @Override
    protected void before() throws Throwable {
        int port = 12345; // Network.getFreeServerPort();
        IMongodConfig mongodConfig = new MongodConfigBuilder().version(Version.Main.PRODUCTION)
            .net(new Net(port, Network.localhostIsIPv6())).build();

        MongodStarter runtime = MongodStarter.getDefaultInstance();
        MongodExecutable mongodExecutable = null;
        mongodExecutable = runtime.prepare(mongodConfig);
        mongoProcess = mongodExecutable.start();

        mongo = new MongoClient(Network.getLocalHost().getHostName(), port);
        db = mongo.getDB("test");
    }

    @Override
    protected void after() {
        mongo.close();
        mongoProcess.stop();
    }

    public DB getDb() {
        return db;
    }
}
