package com.jonahseguin.papaya.checks.fly;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.concurrency.PacketTypeSet;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.check.Check;
import com.jonahseguin.papaya.check.CheckType;
import com.jonahseguin.papaya.check.type.PacketCheck;
import com.jonahseguin.papaya.player.PapayaProfile;
import com.jonahseguin.papaya.util.BlockUtil;

import java.util.Collections;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * Created by Jonah on 5/31/2018.
 * Project: papaya
 *
 * @ 8:54 PM
 */
public class CheckBedFly extends Check implements PacketCheck {

    private int bedBlockRadius = 10;

    public CheckBedFly(Papaya papaya) {
        super(papaya, CheckType.BED_FLY);
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
        if (!profile.onCheckCooldown()) {
            final int actionID = event.getPacket().getIntegers().readSafely(1);
            if (actionID == 3) {
                // Leave bed (LEAVE_BED)
                final Player player = event.getPlayer();
                final Location location = player.getLocation();
                for (Block block : BlockUtil.getBlocksAround(location, bedBlockRadius)) {
                    if (block.getType() == Material.BED || block.getType() == Material.BED_BLOCK) {
                        return;
                    }
                }
                // We only get here if no bed is found:
                if (fail(profile)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @Override
    public String description() {
        return "Monitors for clients sending illegal 'bed leave' packets to create a flying effect.";
    }

    @Override
    public void startup() {

    }

    @Override
    public void shutdown() {

    }
}
