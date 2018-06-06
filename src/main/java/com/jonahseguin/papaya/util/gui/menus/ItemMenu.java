/*
 * Copyright (c) 2018 Jonah Seguin (Shawckz).  All rights reserved.  You may not modify, decompile, distribute or use any code/text contained in this document(plugin) without explicit signed permission from Jonah Seguin.
 */

package com.jonahseguin.papaya.util.gui.menus;

import com.jonahseguin.papaya.util.gui.MenuListener;
import com.jonahseguin.papaya.util.gui.events.ItemClickEvent;
import com.jonahseguin.papaya.util.gui.items.MenuItem;
import com.jonahseguin.papaya.util.gui.items.StaticMenuItem;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemMenu {
    @SuppressWarnings("deprecation")
    private static final MenuItem EMPTY_SLOT_ITEM = new StaticMenuItem(" ", new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15));
    private JavaPlugin plugin;
    private String name;
    private Size size;
    private MenuItem[] items;
    private ItemMenu parent;

    //0:  0 1 2 3 4 5 6 7 8
    //1:  9 0 1 2 3 4 5 6 7
    //2:  8 9 0 1 2 3 4 5 6
    //3:  7 8 9 0 1 2 3 4 5

    public ItemMenu(String name, Size size, JavaPlugin plugin, ItemMenu parent) {
        this.plugin = plugin;
        this.name = name;
        this.size = size;
        this.items = new MenuItem[size.getSize()];
        this.parent = parent;
    }

    public ItemMenu(String name, Size size, JavaPlugin plugin) {
        this(name, size, plugin, null);
    }

    public String getName() {
        return name;
    }

    public Size getSize() {
        return size;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public ItemMenu getParent() {
        return parent;
    }

    public void setParent(ItemMenu parent) {
        this.parent = parent;
    }

    public ItemMenu setItem(int position, MenuItem menuItem) {
        items[position] = menuItem;
        return this;
    }

    public ItemMenu fillEmptySlots(MenuItem menuItem) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                items[i] = menuItem;
            }
        }
        return this;
    }

    public ItemMenu fillEmptySlots() {
        return fillEmptySlots(EMPTY_SLOT_ITEM);
    }

    public void open(Player player) {
        if (!MenuListener.getInstance().isRegistered(plugin)) {
            MenuListener.getInstance().register(plugin);
        }
        Inventory inventory = Bukkit.createInventory(new MenuHolder(this, Bukkit.createInventory(player, size.getSize())), size.getSize(), name);
        apply(inventory, player);
        player.openInventory(inventory);
    }

    @SuppressWarnings("deprecation")
    public void update(Player player) {
        if (player.getOpenInventory() != null) {
            Inventory inventory = player.getOpenInventory().getTopInventory();
            if (inventory.getHolder() instanceof MenuHolder && ((MenuHolder) inventory.getHolder()).getMenu().equals(this)) {
                apply(inventory, player);
                player.updateInventory();
            }
        }
    }

    private void apply(Inventory inventory, Player player) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                inventory.setItem(i, items[i].getFinalIcon(player));
            } else {
                inventory.setItem(i, null);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void onInventoryClick(InventoryClickEvent event) {
        int slot = event.getRawSlot();
        if (slot >= 0 && slot < size.getSize() && items[slot] != null) {
            Player player = (Player) event.getWhoClicked();
            ItemClickEvent itemClickEvent = new ItemClickEvent(player, event.getClick(), this, items[slot]);
            items[slot].onItemClick(itemClickEvent);
            if (event.getClick() != ClickType.LEFT && !itemClickEvent.isAllowOtherClickTypes()) {
                return;
            }
            if (itemClickEvent.willUpdate()) {
                update(player);
            } else {
                player.updateInventory();
                if (itemClickEvent.willClose() || itemClickEvent.willGoBack()) {
                    final String playerName = player.getName();
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        Player p = Bukkit.getPlayerExact(playerName);
                        if (p != null) {
                            p.closeInventory();
                        }
                    }, 1);
                }
                if (itemClickEvent.willGoBack() && hasParent()) {
                    final String playerName = player.getName();
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        Player p = Bukkit.getPlayerExact(playerName);
                        if (p != null) {
                            parent.open(p);
                        }
                    }, 3);
                }
            }
        }
    }

    public void destroy() {
        plugin = null;
        name = null;
        size = null;
        items = null;
        parent = null;
    }

    public enum Size {
        ONE_LINE(9),
        TWO_LINE(18),
        THREE_LINE(27),
        FOUR_LINE(36),
        FIVE_LINE(45),
        SIX_LINE(54);

        private final int size;

        Size(int size) {
            this.size = size;
        }

        public static Size fit(int slots) {
            if (slots < 10) {
                return ONE_LINE;
            } else if (slots < 19) {
                return TWO_LINE;
            } else if (slots < 28) {
                return THREE_LINE;
            } else if (slots < 37) {
                return FOUR_LINE;
            } else if (slots < 46) {
                return FIVE_LINE;
            } else {
                return SIX_LINE;
            }
        }

        public int getSize() {
            return size;
        }
    }
}
