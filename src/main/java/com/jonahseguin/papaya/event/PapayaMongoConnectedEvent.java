package com.jonahseguin.papaya.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Jonah on 10/21/2017.
 * Project: purifiedCore
 *
 * @ 5:23 PM
 */
public class PapayaMongoConnectedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public PapayaMongoConnectedEvent() {

    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
