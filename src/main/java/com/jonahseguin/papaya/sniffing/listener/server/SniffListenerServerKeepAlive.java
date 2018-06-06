package com.jonahseguin.papaya.sniffing.listener.server;

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
public class SniffListenerServerKeepAlive extends PacketAdapter {

    private final PacketSniffManager manager;

    public SniffListenerServerKeepAlive(PacketSniffManager manager) {
        super(Papaya.getInstance(), PacketType.Play.Server.KEEP_ALIVE);
        this.manager = manager;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        manager.getSnifferIfApplicable(event).ifPresent(sniffer -> sniffer.sniff(event));
    }
}
