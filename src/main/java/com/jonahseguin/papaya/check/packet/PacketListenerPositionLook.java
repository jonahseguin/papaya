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
public class PacketListenerPositionLook extends CheckPacketAdapter {

    public PacketListenerPositionLook(Plugin plugin) {
        super(new PacketAdapter.AdapterParameteters().clientSide().optionAsync().types(PacketType.Play.Client.POSITION_LOOK).plugin(plugin));
    }



}
