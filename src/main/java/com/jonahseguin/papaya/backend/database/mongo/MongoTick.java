package com.jonahseguin.papaya.backend.database.mongo;

import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.event.PapayaMongoConnectedEvent;
import com.jonahseguin.papaya.event.PapayaMongoDisconnectedEvent;

import org.bukkit.Bukkit;

/**
 * Created by Jonah on 10/21/2017.
 * Project: purifiedCore
 *
 * @ 6:13 PM
 */
public class MongoTick implements Runnable {

    private final MongoManager mongoManager;
    private boolean connected = true;

    public MongoTick(MongoManager mongoManager) {
        this.mongoManager = mongoManager;
        this.connected = mongoManager.isConnected();
        Papaya.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(Papaya.getInstance(),
                this, 100L, 100L);
    }

    @Override
    public void run() {
        boolean mongoManagerConnected = mongoManager.isConnected();
        if (connected && !mongoManagerConnected) {
            connected = false;
            Bukkit.getServer().getPluginManager().callEvent(new PapayaMongoDisconnectedEvent());
            Papaya.log("[Papaya Database] CONNECTION TO MONGODB FAILED.");
            Papaya.log("[Papaya Database] Entering maintenance mode.  ");
            Papaya.getInstance().getPapayaConfig().setMaintenance(true);
            Bukkit.broadcastMessage(Papaya.format("&c&lFATAL ERROR:  Papaya could not connect to the database."));
            Bukkit.broadcastMessage(Papaya.format("&cMaintenance mode has been enabled."));
            Bukkit.broadcastMessage(Papaya.format("&c&lLogging out could result in a potential loss of data."));
            Bukkit.broadcastMessage(Papaya.format("&7All data will be saved once the connection is restored.  Local data is still present."));
        } else if (!connected && mongoManagerConnected) {
            connected = true;
            Bukkit.getServer().getPluginManager().callEvent(new PapayaMongoConnectedEvent());
            Papaya.log("[Papaya Database] Successfully connected to MongoDB.");
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
    }
}
