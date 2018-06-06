package com.jonahseguin.papaya.sniffing.sniffers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.backend.config.PapayaLang;
import com.jonahseguin.papaya.sniffing.PacketSniffManager;
import com.jonahseguin.papaya.sniffing.PacketSniffType;
import com.jonahseguin.papaya.sniffing.PacketSniffer;
import com.jonahseguin.papaya.util.TickManager;

import org.bukkit.entity.Player;

/**
 * Created by Jonah on 5/29/2018.
 * Project: papaya
 *
 * @ 11:01 PM
 */
public class SnifferUseEntity extends PacketSniffer {

    public SnifferUseEntity(PacketSniffManager manager) {
        super(manager, PacketSniffType.USE_ENTITY);
    }

    @Override
    public boolean shouldSniff(PacketType packetType) {
        return packetType == PacketType.Play.Client.USE_ENTITY;
    }

    @Override
    public void sniff(PacketEvent event) {
        final Player player = event.getPlayer();
        if (manager.isSniffing(player, getSniffType())) {
            // final PapayaProfile profile = Papaya.getInstance().getProfileCache().get(player);
            final TickManager tM = Papaya.getInstance().getTickManager();
            final StructureModifier<EnumWrappers.EntityUseAction> actions = event.getPacket().getEntityUseActions();
            if (actions.size() == 1 && event.getPacket().getModifier().readSafely(1) != null) {
                final String action = actions.read(0).toString();
                final int entityID = event.getPacket().getIntegers().readSafely(0);

                final String msg = PapayaLang.SNIFF_SERVERBOUND_USE_ENTITY.format(tM.getTotalTick(), tM.getRelativeTick(), player.getName(), entityID, action);
                manager.sniffMessage(msg);
            }
        }
    }
}
