package com.jonahseguin.papaya.sniffing;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Jonah on 5/29/2018.
 * Project: papaya
 *
 * @ 10:20 PM
 */
@Getter
@Setter
public abstract class PacketSniffer {

    protected final PacketSniffManager manager;
    private final PacketSniffType sniffType;

    public PacketSniffer(PacketSniffManager manager, PacketSniffType sniffType) {
        this.manager = manager;
        this.sniffType = sniffType;
    }


    public abstract boolean shouldSniff(PacketType packetType);

    public abstract void sniff(PacketEvent event);

}
