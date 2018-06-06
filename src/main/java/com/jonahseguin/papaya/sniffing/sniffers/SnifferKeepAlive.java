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
public class SnifferKeepAlive extends PacketSniffer {

    public SnifferKeepAlive(PacketSniffManager manager) {
        super(manager, PacketSniffType.KEEP_ALIVE);
    }

    @Override
    public boolean shouldSniff(PacketType packetType) {
        return packetType == PacketType.Play.Client.KEEP_ALIVE || packetType == PacketType.Play.Server.KEEP_ALIVE;
    }

    @Override
    public void sniff(PacketEvent event) {
        final Player player = event.getPlayer();
        if (manager.isSniffing(player, getSniffType())) {
            // final PapayaProfile profile = Papaya.getInstance().getProfileCache().get(player);
            final TickManager tM = Papaya.getInstance().getTickManager();
            final int id = event.getPacket().getIntegers().readSafely(0);

            final PapayaLang lang;
            if (event.getPacketType() == PacketType.Play.Client.KEEP_ALIVE) {
                lang = PapayaLang.SNIFF_SERVERBOUND_KEEP_ALIVE;
            }
            else {
                lang = PapayaLang.SNIFF_CLIENTBOUND_KEEP_ALIVE;
            }
            final String msg = lang.format(tM.getTotalTick(), tM.getRelativeTick(), player.getName(), id);
            manager.sniffMessage(msg);
        }
    }
}
