package com.jonahseguin.papaya.sniffing.sniffers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
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
public class SnifferOnGround extends PacketSniffer {

    public SnifferOnGround(PacketSniffManager manager) {
        super(manager, PacketSniffType.PLAYER_ON_GROUND);
    }

    @Override
    public boolean shouldSniff(PacketType packetType) {
        return packetType == PacketType.Play.Client.FLYING;
    }

    @Override
    public void sniff(PacketEvent event) {
        final Player player = event.getPlayer();
        if (manager.isSniffing(player, getSniffType())) {
            // final PapayaProfile profile = Papaya.getInstance().getProfileCache().get(player);
            final TickManager tM = Papaya.getInstance().getTickManager();

            final boolean onGround = event.getPacket().getBooleans().readSafely(0);

            final String msg = PapayaLang.SNIFF_SERVERBOUND_ON_GROUND.format(tM.getTotalTick(), tM.getRelativeTick(), player.getName(), onGround);
            manager.sniffMessage(msg);
        }
    }
}
