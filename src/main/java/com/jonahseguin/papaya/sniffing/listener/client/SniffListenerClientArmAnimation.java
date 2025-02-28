package com.jonahseguin.papaya.sniffing.listener.client;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.sniffing.PacketSniffManager;

/**
 * Created by Jonah on 5/29/2018.
 * Project: papaya
 *
 * @ 10:22 PM
 */
public class SniffListenerClientArmAnimation extends PacketAdapter {

    private final PacketSniffManager manager;

    public SniffListenerClientArmAnimation(PacketSniffManager manager) {
        super(Papaya.getInstance(), PacketType.Play.Client.ARM_ANIMATION);
        this.manager = manager;
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        manager.getSnifferIfApplicable(event).ifPresent(sniffer -> sniffer.sniff(event));
    }
}
