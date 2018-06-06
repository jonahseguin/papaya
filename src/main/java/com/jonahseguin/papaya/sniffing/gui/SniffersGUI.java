package com.jonahseguin.papaya.sniffing.gui;

import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.player.PapayaProfile;
import com.jonahseguin.papaya.sniffing.PacketSniffManager;
import com.jonahseguin.papaya.sniffing.PacketSniffType;
import com.jonahseguin.papaya.util.gui.menus.ItemMenu;

/**
 * Created by Jonah on 5/30/2018.
 * Project: papaya
 *
 * @ 8:38 AM
 */
public class SniffersGUI {

    private final PacketSniffManager manager;

    public SniffersGUI(PacketSniffManager manager) {
        this.manager = manager;
    }

    public ItemMenu getMenuFor(PapayaProfile target) {
        final ItemMenu menu = new ItemMenu(Papaya.format("&6Papaya&7: &rPacket Sniffing"), ItemMenu.Size.THREE_LINE, Papaya.getInstance());
        int index = 0;
        for(PacketSniffType type : PacketSniffType.values()) {
            menu.setItem(index, new SnifferItem(target, type));
            index++;
        }
        menu.fillEmptySlots();
        return menu;
    }


}
