package com.jonahseguin.papaya.check.type;

import com.comphenix.protocol.concurrency.PacketTypeSet;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketEvent;
import com.jonahseguin.papaya.player.PapayaProfile;

/**
 * Created by Jonah on 5/30/2018.
 * Project: papaya
 *
 * @ 5:56 PM
 */
public interface PacketCheck {

    PacketTypeSet packets();

    boolean async();

    void check(PapayaProfile profile, PacketEvent event);


}
