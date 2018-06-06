package com.jonahseguin.papaya.sniffing;

import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketEvent;
import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.backend.permission.PapayaPerm;
import com.jonahseguin.papaya.player.PapayaProfile;
import com.jonahseguin.papaya.sniffing.gui.SniffersGUI;
import com.jonahseguin.papaya.sniffing.listener.client.*;
import com.jonahseguin.papaya.sniffing.listener.server.SniffListenerServerKeepAlive;
import com.jonahseguin.papaya.sniffing.sniffers.*;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;

/**
 * Created by Jonah on 5/29/2018.
 * Project: papaya
 *
 * @ 10:22 PM
 */
@Getter
public class PacketSniffManager {

    private final Papaya papaya;
    private final Map<PacketSniffType, PacketSniffer> packetSniffers = new HashMap<>();

    private final SniffersGUI gui;

    public PacketSniffManager(Papaya papaya) {
        this.papaya = papaya;

        ProtocolManager manager = papaya.getProtocolManager();

        manager.addPacketListener(new SniffListenerClientKeepAlive(this));
        manager.addPacketListener(new SniffListenerServerKeepAlive(this));
        manager.addPacketListener(new SniffListenerClientUseEntity(this));
        manager.addPacketListener(new SniffListenerClientOnGround(this));
        manager.addPacketListener(new SniffListenerClientEntityAction(this));
        manager.addPacketListener(new SniffListenerClientLook(this));
        manager.addPacketListener(new SniffListenerClientPosition(this));
        manager.addPacketListener(new SniffListenerClientPositionLook(this));
        manager.addPacketListener(new SniffListenerClientArmAnimation(this));

        packetSniffers.put(PacketSniffType.KEEP_ALIVE, new SnifferKeepAlive(this));
        packetSniffers.put(PacketSniffType.USE_ENTITY, new SnifferUseEntity(this));
        packetSniffers.put(PacketSniffType.PLAYER_ON_GROUND, new SnifferOnGround(this));
        packetSniffers.put(PacketSniffType.ENTITY_ACTION, new SnifferEntityAction(this));
        packetSniffers.put(PacketSniffType.PLAYER_LOOK, new SnifferLook(this));
        packetSniffers.put(PacketSniffType.PLAYER_POSITION, new SnifferPosition(this));
        packetSniffers.put(PacketSniffType.PLAYER_POSITION_AND_LOOK, new SnifferPositionLook(this));
        packetSniffers.put(PacketSniffType.ARM_ANIMATION, new SnifferArmAnimation(this));

        this.gui = new SniffersGUI(this);
    }

    public Optional<PacketSniffer> getSnifferIfApplicable(PacketEvent event) {
        for (PacketSniffer sniffer : packetSniffers.values()) {
            if (sniffer.shouldSniff(event.getPacketType())) {
                return Optional.of(sniffer);
            }
        }
        return Optional.empty();
    }

    public Set<PapayaProfile> getSniffing(PacketSniffType type) {
        return papaya.getProfileCache().getCache().getOnlineProfiles().stream().filter(profile -> profile.isSniffing(type)).collect(Collectors.toSet());
    }

    public boolean isSniffing(Player player, PacketSniffType type) {
        PapayaProfile papayaProfile = papaya.getProfileCache().get(player);
        return papayaProfile.isSniffing(type);
    }

    public void sniffMessage(String message) {
        for (Player player : papaya.getServer().getOnlinePlayers()) {
            if (PapayaPerm.SNIFF.has(player)) {
                player.sendMessage(message);
            }
        }
        Papaya.log(ChatColor.stripColor(message));
    }

}
