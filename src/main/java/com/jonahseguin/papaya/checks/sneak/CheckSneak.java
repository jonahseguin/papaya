package com.jonahseguin.papaya.checks.sneak;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.concurrency.PacketTypeSet;
import com.comphenix.protocol.events.PacketEvent;
import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.check.Check;
import com.jonahseguin.papaya.check.CheckType;
import com.jonahseguin.papaya.check.type.BukkitCheck;
import com.jonahseguin.papaya.check.type.PacketCheck;
import com.jonahseguin.papaya.player.PapayaProfile;

import java.util.Collections;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleSneakEvent;

/**
 * Created by Jonah on 5/31/2018.
 * Project: papaya
 *
 * @ 9:06 PM
 */
public class CheckSneak extends Check implements PacketCheck, BukkitCheck {

    public CheckSneak(Papaya papaya) {
        super(papaya, CheckType.SNEAK);
    }

    @Override
    public PacketTypeSet packets() {
        return new PacketTypeSet(Collections.singleton(PacketType.Play.Client.ENTITY_ACTION));
    }

    @Override
    public boolean async() {
        return false;
    }

    @Override
    public void check(PapayaProfile profile, PacketEvent event) {
        if (profile.shouldCheckMovement() && !profile.onCheckCooldown()) {
            final int actionID = event.getPacket().getIntegers().readSafely(1);

            if (actionID == 1) {
                // Crouch (start sneaking)

            }
            else if (actionID == 2) {
                // Uncrouch (stop sneaking)

            }

        }
    }

    @EventHandler
    public void onToggleSneak(PlayerToggleSneakEvent event) {
        boolean sneaking = event.isSneaking();
        if (sneaking) {

        }
        else {

        }
    }

    @Override
    public String description() {
        return "Monitors for 'fake' sneaking packets.";
    }

    @Override
    public void startup() {

    }

    @Override
    public void shutdown() {

    }
}
