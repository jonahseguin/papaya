package com.jonahseguin.papaya.check.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.jonahseguin.papaya.check.CheckPacketAdapter;

import org.bukkit.plugin.Plugin;

/**
 * Created by Jonah on 5/30/2018.
 * Project: papaya
 *
 * @ 6:35 PM
 */
public class PacketListenerKeepAlive extends CheckPacketAdapter {

    public PacketListenerKeepAlive(Plugin plugin) {
        super(plugin, PacketType.Play.Client.KEEP_ALIVE, PacketType.Play.Server.KEEP_ALIVE);
    }



}
