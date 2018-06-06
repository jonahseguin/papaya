package com.jonahseguin.papaya.check;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.check.type.PacketCheck;
import com.jonahseguin.papaya.player.PapayaProfile;

import javax.annotation.Nonnull;

import org.bukkit.plugin.Plugin;

/**
 * Created by Jonah on 5/30/2018.
 * Project: papaya
 *
 * @ 6:36 PM
 */
public abstract class CheckPacketAdapter extends PacketAdapter {

    public CheckPacketAdapter(@Nonnull AdapterParameteters params) {
        super(params);
    }

    public CheckPacketAdapter(Plugin plugin, PacketType... types) {
        super(plugin, types);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        this.handlePacket(event);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        this.handlePacket(event);
    }

    public void handlePacket(final PacketEvent event) {
        final PapayaProfile profile = Papaya.getInstance().getProfileCache().get(event.getPlayer());
        if (profile == null) return;

        Papaya.getInstance().getCheckManager().getChecks().values().forEach(check -> {
            if (check instanceof PacketCheck) {
                final PacketCheck packetCheck = (PacketCheck) check;
                if (packetCheck.packets().contains(event.getPacketType())) {
                    packetCheck.check(profile, event);
                }
            }
        });
    }

}
