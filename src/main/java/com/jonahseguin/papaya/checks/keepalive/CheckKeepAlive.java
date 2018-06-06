package com.jonahseguin.papaya.checks.keepalive;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.concurrency.PacketTypeSet;
import com.comphenix.protocol.events.PacketEvent;
import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.check.Check;
import com.jonahseguin.papaya.check.CheckType;
import com.jonahseguin.papaya.check.type.PacketCheck;
import com.jonahseguin.papaya.player.PapayaProfile;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Jonah on 5/30/2018.
 * Project: papaya
 *
 * @ 6:01 PM
 */
public class CheckKeepAlive extends Check implements PacketCheck {

    public CheckKeepAlive(Papaya papaya) {
        super(papaya, CheckType.KEEP_ALIVE);
    }

    @Override
    public PacketTypeSet packets() {
        return new PacketTypeSet(Arrays.asList(PacketType.Play.Client.KEEP_ALIVE, PacketType.Play.Server.KEEP_ALIVE));
    }

    @Override
    public boolean async() {
        return false;
    }

    @Override
    public void startup() {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public void check(PapayaProfile profile, PacketEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.KEEP_ALIVE) {
            // Client --> Server (response)
            final int id = event.getPacket().getIntegers().readSafely(0);
            if (profile.getServerData().isKeepAliveValid(id)) {
                profile.getServerData().removeKeepAliveID(id);
            }
            else {
                if (fail(profile)) {
                    event.setCancelled(true);
                }
            }
        }
        else if (event.getPacketType() == PacketType.Play.Server.KEEP_ALIVE) {
            // Server --> Client (ping)
            final int id = event.getPacket().getIntegers().readSafely(0);
            profile.getServerData().addKeepAliveID(id);
        }
    }

    @Override
    public String description() {
        return "Monitors for invalid keep-alive packets preventing 'god-mode'.";
    }
}
