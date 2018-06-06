package com.jonahseguin.papaya.sniffing.gui;

import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.player.PapayaProfile;
import com.jonahseguin.papaya.sniffing.PacketSniffType;
import com.jonahseguin.papaya.util.gui.events.ItemClickEvent;
import com.jonahseguin.papaya.util.gui.items.MenuItem;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Jonah on 5/30/2018.
 * Project: papaya
 *
 * @ 9:19 AM
 */
public class SnifferItem extends MenuItem {

    private final PapayaProfile profile;
    private final PacketSniffType type;

    public SnifferItem(PapayaProfile profile, PacketSniffType type) {
        super(type.toString().replace("_", " "), new ItemStack(Material.ENCHANTED_BOOK));
        this.profile = profile;
        this.type = type;
    }

    @Override
    public ItemStack getFinalIcon(Player player) {
        final ItemStack is = new ItemStack(Material.ENCHANTED_BOOK, 1);
        ItemMeta im = is.getItemMeta();
        boolean sniffing = profile.isSniffing(type);
        im.setDisplayName((sniffing ? ChatColor.GREEN : ChatColor.RED) + type.toString().replace("_", " "));
        List<String> lore = new ArrayList<>();
        lore.add(Papaya.format("&7Sniffing: {0}", (sniffing ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled")));
        lore.add(Papaya.format("&7Click to toggle."));
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }

    @Override
    public void onItemClick(ItemClickEvent event) {
        event.setAllowOtherClickTypes(false);
        if (event.getClickType() == ClickType.LEFT) {
            if (profile.isSniffing(type)) {
                profile.stopSniffing(type);
            }
            else {
                profile.startSniffing(type);
            }
            event.setWillUpdate(true);
        }
    }
}
