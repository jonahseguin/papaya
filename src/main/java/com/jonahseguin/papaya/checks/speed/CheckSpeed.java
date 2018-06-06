package com.jonahseguin.papaya.checks.speed;

import com.comphenix.protocol.concurrency.PacketTypeSet;
import com.comphenix.protocol.events.PacketEvent;
import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.check.Check;
import com.jonahseguin.papaya.check.CheckType;
import com.jonahseguin.papaya.check.type.BukkitCheck;
import com.jonahseguin.papaya.check.type.PacketCheck;
import com.jonahseguin.papaya.player.PapayaProfile;

/**
 * Created by Jonah on 6/3/2018.
 * Project: papaya
 *
 * @ 7:25 PM
 */
public class CheckSpeed extends Check implements PacketCheck, BukkitCheck {

    public CheckSpeed(Papaya papaya) {
        super(papaya, CheckType.SPEED);
    }

    @Override
    public PacketTypeSet packets() {
        return null;
    }

    @Override
    public boolean async() {
        return false;
    }

    @Override
    public void check(PapayaProfile profile, PacketEvent event) {

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
