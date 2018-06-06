package com.jonahseguin.papaya.sniffing.sniffers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.backend.config.PapayaLang;
import com.jonahseguin.papaya.sniffing.PacketSniffManager;
import com.jonahseguin.papaya.sniffing.PacketSniffType;
import com.jonahseguin.papaya.sniffing.PacketSniffer;
import com.jonahseguin.papaya.util.TickManager;
import com.jonahseguin.papaya.util.packet.PacketArmAnimationType;
import com.jonahseguin.papaya.util.packet.PacketEntityActionType;

import org.bukkit.entity.Player;

/**
 * Created by Jonah on 5/29/2018.
 * Project: papaya
 *
 * @ 11:01 PM
 */
public class SnifferArmAnimation extends PacketSniffer {

    public SnifferArmAnimation(PacketSniffManager manager) {
        super(manager, PacketSniffType.ARM_ANIMATION);
    }

    @Override
    public boolean shouldSniff(PacketType packetType) {
        return packetType == PacketType.Play.Client.ARM_ANIMATION;
    }

    @Override
    public void sniff(PacketEvent event) {
        final Player player = event.getPlayer();
        if (manager.isSniffing(player, getSniffType())) {
            final int actionID = event.getPacket().getIntegers().readSafely(1);
            final PacketArmAnimationType action = PacketArmAnimationType.fromId(actionID);
            if (action != null) {
                // final PapayaProfile profile = Papaya.getInstance().getProfileCache().get(player);
                final TickManager tM = Papaya.getInstance().getTickManager();

                final String msg = PapayaLang.SNIFF_SERVERBOUND_ARM_ANIMATION.format(tM.getTotalTick(), tM.getRelativeTick(), player.getName(), action.toString());
                manager.sniffMessage(msg);
            }
        }
    }
}
