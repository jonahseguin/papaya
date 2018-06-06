package com.jonahseguin.papaya.fix;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.jonahseguin.papaya.Papaya;

import org.bukkit.entity.Entity;

/**
 * Created by Jonah on 5/31/2018.
 * Project: papaya
 *
 * @ 11:02 PM
 */
public class AsyncHitDetection {

    public AsyncHitDetection(Papaya instance) {
        if (instance.getPapayaConfig().isAsyncHitDetection()) {
            final ProtocolManager manager = Papaya.getInstance().getProtocolManager();

            // Register Async handler for animation packet.
            final PacketAdapter.AdapterParameteters params = new PacketAdapter.AdapterParameteters().clientSide().optionAsync().types(PacketType.Play.Client.USE_ENTITY).plugin(instance);
            manager.getAsynchronousManager().registerAsyncHandler(new PacketAdapter(params) {
                @Override
                public void onPacketReceiving(PacketEvent event) {
                    if (event.getPacket().getEntityUseActions().read(0) != EnumWrappers.EntityUseAction.ATTACK) return;
                    final Entity entity = event.getPacket().getEntityModifier(event).read(0);
                }
            }).start();
        }
    }
}
