/*
 * Copyright (c) 2018 Jonah Seguin (Shawckz).  All rights reserved.  You may not modify, decompile, distribute or use any code/text contained in this document(plugin) without explicit signed permission from Jonah Seguin.
 */

package com.jonahseguin.papaya.util.gui.events;

import com.jonahseguin.papaya.util.gui.items.MenuItem;
import com.jonahseguin.papaya.util.gui.menus.ItemMenu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;


public class ItemClickEvent {
    private final Player player;
    private boolean goBack = false;
    private boolean close = false;
    private boolean update = false;
    private boolean allowOtherClickTypes = false;
    private final ClickType clickType;
    private final ItemMenu menu;
    private final MenuItem item;

    public ItemClickEvent(Player player, ClickType clickType, ItemMenu menu, MenuItem item) {
        this.player = player;
        this.clickType = clickType;
        this.menu = menu;
        this.item = item;
    }

    public ItemMenu getMenu() {
        return menu;
    }

    public MenuItem getItem() {
        return item;
    }

    public boolean isAllowOtherClickTypes() {
        return allowOtherClickTypes;
    }

    public void setAllowOtherClickTypes(boolean allowOtherClickTypes) {
        this.allowOtherClickTypes = allowOtherClickTypes;
    }

    public ClickType getClickType() {
        return clickType;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean willGoBack() {
        return goBack;
    }

    public void setWillGoBack(boolean goBack) {
        this.goBack = goBack;
        if (goBack) {
            close = false;
            update = false;
        }
    }

    public boolean willClose() {
        return close;
    }

    public void setWillClose(boolean close) {
        this.close = close;
        if (close) {
            goBack = false;
            update = false;
        }
    }

    public boolean willUpdate() {
        return update;
    }

    public void setWillUpdate(boolean update) {
        this.update = update;
        if (update) {
            goBack = false;
            close = false;
        }
    }
}
