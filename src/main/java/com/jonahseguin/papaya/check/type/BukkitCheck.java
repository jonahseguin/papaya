package com.jonahseguin.papaya.check.type;

import com.jonahseguin.papaya.Papaya;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

/**
 * Created by Jonah on 5/30/2018.
 * Project: papaya
 *
 * @ 5:56 PM
 */
public interface BukkitCheck extends Listener {

    default void registerListener() {
        Papaya.getInstance().getServer().getPluginManager().registerEvents(this, Papaya.getInstance());
    }

    default void unregisterListener() {
        HandlerList.unregisterAll(this);
    }

}
