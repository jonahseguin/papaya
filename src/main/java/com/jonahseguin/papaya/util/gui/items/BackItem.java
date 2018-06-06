/*
 * Copyright (c) 2018 Jonah Seguin (Shawckz).  All rights reserved.  You may not modify, decompile, distribute or use any code/text contained in this document(plugin) without explicit signed permission from Jonah Seguin.
 */

package com.jonahseguin.papaya.util.gui.items;

import com.jonahseguin.papaya.util.gui.events.ItemClickEvent;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BackItem extends StaticMenuItem {

    public BackItem() {
        super(ChatColor.RED + "Back", new ItemStack(Material.FENCE_GATE));
    }

    @Override
    public void onItemClick(ItemClickEvent event) {
        event.setWillGoBack(true);
    }
}
