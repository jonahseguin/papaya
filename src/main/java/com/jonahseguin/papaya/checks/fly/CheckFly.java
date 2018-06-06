package com.jonahseguin.papaya.checks.fly;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.concurrency.PacketTypeSet;
import com.comphenix.protocol.events.PacketEvent;
import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.check.Check;
import com.jonahseguin.papaya.check.CheckType;
import com.jonahseguin.papaya.check.type.BukkitCheck;
import com.jonahseguin.papaya.check.type.PacketCheck;
import com.jonahseguin.papaya.player.PapayaProfile;

import java.util.Arrays;

/**
 * Created by Jonah on 6/5/2018.
 * Project: papaya
 *
 * @ 8:30 PM
 */
public class CheckFly extends Check implements PacketCheck, BukkitCheck {

    public CheckFly(Papaya papaya) {
        super(papaya, CheckType.FLY);
    }

    @Override
    public PacketTypeSet packets() {
        return new PacketTypeSet(Arrays.asList(PacketType.Play.Client.POSITION, PacketType.Play.Client.POSITION_LOOK, PacketType.Play.Client.FLYING, PacketType.Play.Client.LOOK));
    }

    @Override
    public boolean async() {
        return false;
    }

    @Override
    public void check(PapayaProfile profile, PacketEvent event) {
        final boolean onGround = event.getPacket().getBooleans().readSafely(0);

    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public void startup() {

    }

    @Override
    public void shutdown() {

    }
}
