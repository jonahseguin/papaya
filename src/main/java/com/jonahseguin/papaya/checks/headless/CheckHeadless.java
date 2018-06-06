package com.jonahseguin.papaya.checks.headless;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.concurrency.PacketTypeSet;
import com.comphenix.protocol.events.PacketEvent;
import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.check.Check;
import com.jonahseguin.papaya.check.CheckType;
import com.jonahseguin.papaya.check.type.PacketCheck;
import com.jonahseguin.papaya.player.PapayaProfile;

import java.util.Arrays;

/**
 * Created by Jonah on 5/31/2018.
 * Project: papaya
 *
 * @ 11:22 PM
 */
public class CheckHeadless extends Check implements PacketCheck{

    public CheckHeadless(Papaya papaya) {
        super(papaya, CheckType.HEADLESS);
    }

    @Override
    public PacketTypeSet packets() {
        return new PacketTypeSet(Arrays.asList(PacketType.Play.Client.POSITION_LOOK, PacketType.Play.Client.LOOK));
    }

    @Override
    public boolean async() {
        return false;
    }

    @Override
    public void check(PapayaProfile profile, PacketEvent event) {
        final float pitch = event.getPacket().getFloat().readSafely(1);
        if (pitch > 90.1F || pitch < -90.1F) {
            if (fail(profile, "Pitch: " + Math.round(pitch))) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public String description() {
        return "Detects when a player's head moves illegally.";
    }

    @Override
    public void startup() {

    }

    @Override
    public void shutdown() {

    }
}
