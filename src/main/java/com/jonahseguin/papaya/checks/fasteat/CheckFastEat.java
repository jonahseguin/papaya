package com.jonahseguin.papaya.checks.fasteat;

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

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.scheduler.BukkitTask;

/**
 * Created by Jonah on 5/31/2018.
 * Project: papaya
 *
 * @ 11:36 PM
 */
public class CheckFastEat extends Check implements PacketCheck, BukkitCheck {

    private static BukkitTask bukkitTask = null;

    public CheckFastEat(Papaya papaya) {
        super(papaya, CheckType.FAST_EAT);
    }

    @Override
    public PacketTypeSet packets() {
        return new PacketTypeSet(Collections.singleton(PacketType.Play.Client.FLYING));
    }

    @Override
    public boolean async() {
        return false;
    }

    @Override
    public void check(PapayaProfile profile, PacketEvent event) {
        profile.getClientData().setOnGroundPacketsLastSecond(profile.getClientData().getOnGroundPacketsLastSecond() + 1);
        if (profile.getServerData().isEating()) {
            if (profile.getClientData().getOnGroundPacketsLastSecond() > 25) {
                if (fail(profile)) {
                    event.setCancelled(true);
                    profile.getClientData().setOnGroundPacketsLastSecond(0); // prevent spam
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (event.getItem() != null && event.getItem().getType().isEdible() && player.getFoodLevel() < 20) {
                final PapayaProfile profile = papaya.getProfileCache().get(player);
                if (profile != null && !profile.onCheckCooldown()) {
                    profile.getServerData().setEating(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFoodEat(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        if (event.getItem().getType().isEdible()) {
            final PapayaProfile profile = papaya.getProfileCache().get(player);
            if (profile != null && !profile.onCheckCooldown()) {
                profile.getServerData().setEating(false); // Finished eating
            }
        }
    }

    @Override
    public String description() {
        return "Monitors for players eating faster than possible";
    }

    @Override
    public void startup() {
        if (bukkitTask == null) {
            bukkitTask = papaya.getServer().getScheduler().runTaskTimerAsynchronously(papaya, () -> {
                for (PapayaProfile profile : papaya.getProfileCache().getCache().getOnlineProfiles()) {
                    if (profile != null && profile.isOnline()) {
                        profile.getClientData().setOnGroundPacketsLastSecond(0);
                    }
                }
            }, 20L, 20L);
        }
    }

    @Override
    public void shutdown() {

    }
}
