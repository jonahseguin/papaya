package com.jonahseguin.papaya.player;

import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.payload.common.cache.CacheDatabase;
import com.jonahseguin.payload.profile.cache.PayloadProfileCache;
import com.jonahseguin.payload.profile.util.ProfileCacheBuilder;

import java.util.UUID;

import org.bukkit.entity.Player;

/**
 * Created by Jonah on 5/29/2018.
 * Project: papaya
 *
 * @ 8:06 PM
 */
public class PapayaProfileCache {

    private final Papaya instance;
    private final PayloadProfileCache<PapayaProfile> cache;

    public PapayaProfileCache(Papaya instance) {
        this.instance = instance;

        final CacheDatabase database = new CacheDatabase(instance.getMongoManager().getMongoClient(), instance.getMongoManager().getDb(), instance.getRedisManager().getJedisResource(), instance.getMongoManager().getMorphia(), instance.getMongoManager().getDataStore());

        this.cache = new ProfileCacheBuilder<PapayaProfile>(instance)
                .withDatabase(database)
                .withEnableAsyncCaching(instance.getPapayaConfig().isAsyncCaching())
                .withCacheFailRetryIntervalSeconds(instance.getPapayaConfig().getCacheRetrySeconds())
                .withHaltListenerEnabled(true)
                .withCacheLogoutSaveDatabase(instance.getPapayaConfig().isCacheSaveOnLogout())
                .withCacheRemoveOnLogout(false)
                .withCacheLocalExpiryMinutes(instance.getPapayaConfig().getCacheExpiryMinutes())
                .withProfileClass(PapayaProfile.class)
                .withDebugger(new PapayaCacheDebugger())
                .withInstantiator(PapayaProfile::new)
                .withRedisKey("papaya.profiles.")
                .build();
        this.cache.init();
    }

    public PayloadProfileCache<PapayaProfile> getCache() {
        return cache;
    }

    public PapayaProfile get(String name) {
        return cache.getSimpleCache().getProfileByUsername(name);
    }

    public PapayaProfile get(UUID uuid) {
        return cache.getProfile(uuid.toString());
    }

    public PapayaProfile get(Player player) {
        return cache.getProfile(player);
    }





}
