package com.jonahseguin.papaya.backend.database.mongo;

import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.backend.config.LocalConfig;
import com.jonahseguin.papaya.backend.config.annotations.SerializeData;
import com.jonahseguin.papaya.player.PapayaProfile;
import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import com.mongodb.event.*;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.DefaultCreator;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * Created by jonahseguin on 2017-08-12.
 */
public class MongoManager extends LocalConfig implements ServerMonitorListener {

    @SerializeData("database.mongo.name")
    private static String mongoDatabaseName = "minecraft";
    @SerializeData("database.mongo.auth-name")
    private static String mongoAuthDatabaseName = "admin";
    @SerializeData("database.mongo.host")
    private static String mongoHost = "localhost";
    @SerializeData("database.mongo.port")
    private static int mongoPort = 27017;
    @SerializeData("database.mongo.username")
    private static String mongoUsername = "username";
    @SerializeData("database.mongo.password")
    private static String mongoPassword = "password";
    private final Plugin plugin;
    private MongoClient mongoClient;
    private MongoDatabase db;
    private Morphia morphia;
    private Datastore datastore;
    @SerializeData("database.mongo.use-auth")
    private boolean useMongoAuth = false;
    private MongoTick mongoTick = null;

    private boolean failed = false;

    public MongoManager(Papaya instance) {
        super(instance, "mongo.yml");
        this.plugin = instance;
        load();
        save();

        try {
            if (useMongoAuth) {
                MongoCredential credential = MongoCredential.createCredential(mongoUsername, mongoAuthDatabaseName, mongoPassword.toCharArray());
                MongoClientOptions options = MongoClientOptions.builder()
                        .addServerMonitorListener(this)
                        .heartbeatFrequency(500)
                        .connectTimeout(2000)
                        .socketTimeout(2000)
                        .heartbeatConnectTimeout(2000)
                        .heartbeatSocketTimeout(2000)
                        .serverSelectionTimeout(2000)
                        .addClusterListener(new ClusterListener() {
                            @Override
                            public void clusterOpening(ClusterOpeningEvent clusterOpeningEvent) {

                            }

                            @Override
                            public void clusterClosed(ClusterClosedEvent clusterClosedEvent) {

                            }

                            @Override
                            public void clusterDescriptionChanged(ClusterDescriptionChangedEvent clusterDescriptionChangedEvent) {

                            }
                        })
                        .addServerListener(new ServerListener() {
                            @Override
                            public void serverOpening(ServerOpeningEvent serverOpeningEvent) {

                            }

                            @Override
                            public void serverClosed(ServerClosedEvent serverClosedEvent) {

                            }

                            @Override
                            public void serverDescriptionChanged(ServerDescriptionChangedEvent serverDescriptionChangedEvent) {

                            }
                        })
                        .build();
                mongoClient = new MongoClient(new ServerAddress(mongoHost, mongoPort), Arrays.asList(credential), options);
                db = mongoClient.getDatabase(mongoDatabaseName);
            } else {
                mongoClient = new MongoClient(new ServerAddress(mongoHost, mongoPort));
                db = mongoClient.getDatabase(mongoDatabaseName);
            }

            morphia = new Morphia();
            morphia.getMapper().getOptions().setObjectFactory(new DefaultCreator() {
                @Override
                protected ClassLoader getClassLoaderForClass() {
                    return instance.getPapayaClassLoader();
                }
            });
            morphia.map(PapayaProfile.class);
            morphia.mapPackage("com.jonahseguin.papaya");
            datastore = morphia.createDatastore(mongoClient, mongoDatabaseName);
            datastore.ensureIndexes();
            mongoTick = new MongoTick(this);
        } catch (MongoException ex) {
            Papaya.getInstance().getErrorHandler().addRecord(ex, "Could not connect to MongoDB database");
        }
    }

    public MongoDatabase getDb() {
        return db;
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public Morphia getMorphia() {
        return morphia;
    }

    public Datastore getDataStore() {
        return datastore;
    }

    public boolean isConnected() {
        try {
            mongoClient.getAddress();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public void serverHearbeatStarted(ServerHeartbeatStartedEvent serverHeartbeatStartedEvent) {
        Papaya.log("[Database] Attemping to connect to MongoDB...");
    }

    @Override
    public void serverHeartbeatSucceeded(ServerHeartbeatSucceededEvent serverHeartbeatSucceededEvent) {
        Papaya.log("[Database] Successfully connected to MongoDB.");
        if (this.failed) {
            Papaya.getInstance().getPapayaConfig().setMaintenance(false);
            Bukkit.broadcastMessage(Papaya.format("&a&lThe database connection has been restored."));
            Bukkit.broadcastMessage(Papaya.format("&aAttempting to save all online player data..."));
            Papaya.getInstance().getProfileCache().getCache().saveAll((entry) -> {
                int count = entry.getKey();
                int failed = entry.getValue();
                Bukkit.broadcastMessage(Papaya.format("&a&lSuccessfully saved {0} profiles.", count + ""));
                if (failed > 0) {
                    Bukkit.broadcastMessage(Papaya.format("&c&l{0} profiles failed to load.  If you received an error message, contact an administrator immediately.",
                            failed + ""));
                }
            });
        }
        this.failed = false;
    }

    @Override
    public void serverHeartbeatFailed(ServerHeartbeatFailedEvent serverHeartbeatFailedEvent) {
        Papaya.log("[Database] CONNECTION TO MONGODB FAILED.");
        Papaya.log("[Database] Entering maintenance mode.  ");
        this.failed = true;
        Papaya.getInstance().getPapayaConfig().setMaintenance(true);
        Bukkit.broadcastMessage(Papaya.format("&c&lFATAL ERROR:  The server could not connect to the database."));
        Bukkit.broadcastMessage(Papaya.format("&cMaintenance mode has been enabled."));
        Bukkit.broadcastMessage(Papaya.format("&c&lLogging out could result in a potential loss of data."));
        Bukkit.broadcastMessage(Papaya.format("&7All data will be saved once the connection is restored.  Local data is still present."));
    }
}



