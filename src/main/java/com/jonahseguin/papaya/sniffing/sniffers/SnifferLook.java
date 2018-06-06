package com.jonahseguin.papaya.sniffing.sniffers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.backend.config.PapayaLang;
import com.jonahseguin.papaya.sniffing.PacketSniffManager;
import com.jonahseguin.papaya.sniffing.PacketSniffType;
import com.jonahseguin.papaya.sniffing.PacketSniffer;
import com.jonahseguin.papaya.util.TickManager;

import java.text.DecimalFormat;

import org.bukkit.entity.Player;

/**
 * Created by Jonah on 5/29/2018.
 * Project: papaya
 *
 * @ 11:01 PM
 */
public class SnifferLook extends PacketSniffer {

    private static final DecimalFormat DF = new DecimalFormat("0.00");

    public SnifferLook(PacketSniffManager manager) {
        super(manager, PacketSniffType.PLAYER_LOOK);
    }

    @Override
    public boolean shouldSniff(PacketType packetType) {
        return packetType == PacketType.Play.Client.LOOK;
    }

    @Override
    public void sniff(PacketEvent event) {
        final Player player = event.getPlayer();
        if (manager.isSniffing(player, getSniffType())) {
            final TickManager tM = Papaya.getInstance().getTickManager();

            final float yaw = event.getPacket().getFloat().readSafely(0);
            final float pitch = event.getPacket().getFloat().readSafely(1);
            final double principalYaw = yaw + Math.ceil(-yaw / 360F) * 360F;

            final boolean onGround = event.getPacket().getBooleans().readSafely(0);

            String msg = PapayaLang.SNIFF_SERVERBOUND_LOOK.format(tM.getTotalTick(), tM.getRelativeTick(), player.getName(), DF.format(principalYaw), DF.format(pitch), onGround);
            manager.sniffMessage(msg);
        }
    }
}
