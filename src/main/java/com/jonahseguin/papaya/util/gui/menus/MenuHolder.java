/*
 * Copyright (c) 2018 Jonah Seguin (Shawckz).  All rights reserved.  You may not modify, decompile, distribute or use any code/text contained in this document(plugin) without explicit signed permission from Jonah Seguin.
 */

package com.jonahseguin.papaya.util.gui.menus;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class MenuHolder implements InventoryHolder {
    private ItemMenu menu;
    private Inventory inventory;

    public MenuHolder(ItemMenu menu, Inventory inventory) {
        this.menu = menu;
        this.inventory = inventory;
    }

    public ItemMenu getMenu() {
        return menu;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
