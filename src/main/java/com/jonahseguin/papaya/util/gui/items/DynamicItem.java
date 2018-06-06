/*
 * Copyright (c) 2018 Jonah Seguin (Shawckz).  All rights reserved.  You may not modify, decompile, distribute or use any code/text contained in this document(plugin) without explicit signed permission from Jonah Seguin.
 */

package com.jonahseguin.papaya.util.gui.items;

import com.jonahseguin.papaya.util.gui.events.ItemClickEvent;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Jonah on 5/7/2018.
 * Project: purifiedCore
 *
 * @ 1:12 AM
 */
public abstract class DynamicItem extends MenuItem {

    public DynamicItem() {
        super("Default Name", new ItemStack(Material.AIR)); // Default to empty, will be overriden by getFinalIcon
    }

    @Override
    public abstract ItemStack getFinalIcon(Player player);

    @Override
    public abstract void onItemClick(ItemClickEvent event);
}
