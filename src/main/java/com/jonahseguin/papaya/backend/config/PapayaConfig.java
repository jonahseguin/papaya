package com.jonahseguin.papaya.backend.config;

import com.jonahseguin.papaya.backend.config.annotations.SerializeData;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.plugin.Plugin;


/**
 * Created by Jonah on 5/29/2018.
 * Project: papaya
 *
 * @ 8:02 PM
 */
@Getter
@Setter
public class PapayaConfig extends LocalConfig {

    public PapayaConfig(Plugin plugin) {
        super(plugin);
        loadDefaults();
        load();
        save();
    }

    @SerializeData("debug")
    private boolean debug = false;

    @SerializeData("maintenance-mode")
    private boolean maintenance = false;

    @SerializeData("cache.async")
    private boolean asyncCaching = false;

    @SerializeData("cache.retry-seconds")
    private int cacheRetrySeconds = 60;

    @SerializeData("cache.save-on-logout")
    private boolean cacheSaveOnLogout = false; // to database

    @SerializeData("cache.expiry-minutes")
    private int cacheExpiryMinutes = 60;

    @SerializeData("fix.async-hit-detection")
    private boolean asyncHitDetection = true;

    @SerializeData("check.lag-spike.ms-ignore")
    private long checkLagSpikeMsIgnore = 300; // if server hangs for > 300ms, failures within the last Y (see below) time will be ignored

    @SerializeData("check.lag-spike.pardon-period-ticks")
    private int checkLagSpikePardonPeriodTicks = 60; // Pardon failures that happened within the last 60 ticks (3 seconds) of a lag spike

    @SerializeData("check.fail.delay-ms")
    private long checkFailDelayMs = 500;

    @SerializeData("check.fail-amount-process-tick")
    private int checkFailProcessPerTick = 3;

    @SerializeData("check.fail.expiry-minutes")
    private int checkFailViolationExpiryMinutes = 30;

}
