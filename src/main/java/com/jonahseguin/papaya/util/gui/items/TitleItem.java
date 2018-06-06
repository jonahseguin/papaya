/*
 * Copyright (c) 2018 Jonah Seguin (Shawckz).  All rights reserved.  You may not modify, decompile, distribute or use any code/text contained in this document(plugin) without explicit signed permission from Jonah Seguin.
 */

package com.jonahseguin.papaya.util.gui.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TitleItem extends StaticMenuItem {

    public TitleItem(String title) {
        super(ChatColor.GOLD + title, new ItemStack(Material.SKULL_ITEM));
    }
}
