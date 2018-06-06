package com.jonahseguin.papaya.checks.timer;

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

import org.bukkit.scheduler.BukkitTask;

/**
 * Created by Jonah on 5/31/2018.
 * Project: papaya
 *
 * @ 8:03 PM
 */
public class CheckTimer extends Check implements PacketCheck {

    private static BukkitTask task = null;

    private int maxMovementPacketsPerSecond = 30; // extra for lag.

    public CheckTimer(Papaya papaya) {
        super(papaya, CheckType.TIMER);
    }

    @Override
    public PacketTypeSet packets() {
        return new PacketTypeSet(Arrays.asList(PacketType.Play.Client.LOOK, PacketType.Play.Client.POSITION, PacketType.Play.Client.POSITION_LOOK));
    }

    @Override
    public boolean async() {
        return false;
    }

    @Override
    public void check(PapayaProfile profile, PacketEvent event) {
        if (profile.shouldCheckMovement() && !profile.onCheckCooldown() && !event.isCancelled()) {
            if (!profile.onCheckCooldown() && profile.shouldCheckMovement()) {
                profile.getClientData().setMovementPacketsLastSecond(profile.getClientData().getMovementPacketsLastSecond() + 1);
            }
            if (profile.getClientData().getMovementPacketsLastSecond() > maxMovementPacketsPerSecond) {
                if (fail(profile, profile.getClientData().getMovementPacketsLastSecond())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @Override
    public String description() {
        return "Monitors for excess movement packets that allows hacked clients to 'blink' or increase their game speed.";
    }

    @Override
    public void startup() {
        if (task == null) {
            task = papaya.getServer().getScheduler().runTaskTimerAsynchronously(papaya, () -> {
                for (PapayaProfile profile : papaya.getProfileCache().getCache().getOnlineProfiles()) {
                    if (profile != null && profile.isOnline()) {
                        profile.getClientData().setMovementPacketsLastSecond(0);
                    }
                }
            }, 20L, 20L);
        }
    }

    @Override
    public void shutdown() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }
}
