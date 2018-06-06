package com.jonahseguin.papaya.checks.nofall;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.concurrency.PacketTypeSet;
import com.comphenix.protocol.events.PacketEvent;
import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.check.Check;
import com.jonahseguin.papaya.check.CheckType;
import com.jonahseguin.papaya.check.type.BukkitCheck;
import com.jonahseguin.papaya.check.type.PacketCheck;
import com.jonahseguin.papaya.player.PapayaProfile;
import com.jonahseguin.papaya.util.BlockUtil;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by Jonah on 5/30/2018.
 * Project: papaya
 *
 * @ 6:01 PM
 */
public class CheckNoFall extends Check implements PacketCheck, BukkitCheck {

    public CheckNoFall(Papaya papaya) {
        super(papaya, CheckType.NO_FALL);
    }

    @Override
    public PacketTypeSet packets() {
        return new PacketTypeSet(Arrays.asList(PacketType.Play.Client.FLYING, PacketType.Play.Client.LOOK, PacketType.Play.Client.POSITION, PacketType.Play.Client.POSITION_LOOK));
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
        final boolean onGround = event.getPacket().getBooleans().readSafely(0);
        if (onGround && !profile.onCheckCooldown() && profile.shouldCheckMovement()) {
            if (!profile.getServerData().isOnGround() && profile.getClientData().isOnGround()) {
                if (profile.getServerData().getFallDistance(profile.getPlayer().getLocation()) > 2) {
                    if (!profile.getServerData().isNoFallWaitForDamage()) {
                        profile.getServerData().setNoFallWaitForDamage(true);
                        profile.getClientData().setOnGround(true);
                        return;
                    }
                }
            }
        }
        profile.getClientData().setOnGround(onGround);

    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMove(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final PapayaProfile profile = papaya.getProfileCache().get(player);
        if (profile != null && profile.shouldCheckMovement()) {
            boolean serverOnGround = BlockUtil.isOnGround(player);
            profile.getServerData().setOnGround(serverOnGround);
            boolean clientOnGround = profile.getClientData().isOnGround();
            if (profile.isDebug()) {
                player.sendMessage(Papaya.format("&fOn ground &bServer: {0}&7, Client: {1}", (serverOnGround ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"), (clientOnGround ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No")));
            }
            if (serverOnGround) {
                profile.getServerData().setLastGroundLocation(event.getTo());
            }
            if (serverOnGround) {
                if (profile.getServerData().isNoFallWaitForDamage()) {
                    papaya.getServer().getScheduler().runTaskLater(papaya, () -> {
                        if (!profile.getServerData().isTookFallDamage() && profile.getServerData().isNoFallWaitForDamage() && profile.shouldCheckMovement()) {
                            fail(profile);
                        }
                        profile.getServerData().setNoFallWaitForDamage(false);
                        profile.getServerData().setTookFallDamage(false);
                    }, 5L);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamage(final EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            final Player player = (Player) event.getEntity();
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                final PapayaProfile profile = papaya.getProfileCache().get(player);
                if (profile != null && !profile.onCheckCooldown() && profile.shouldCheckMovement()) {
                    if (profile.getServerData().isNoFallWaitForDamage()) {
                        profile.getServerData().setTookFallDamage(true);
                        profile.getServerData().setNoFallWaitForDamage(false);
                    }
                }
            }
        }
    }



    @Override
    public String description() {
        return "Checks for illegal use of non-vanilla flight.";
    }
}
