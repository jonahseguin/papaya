/*
 * Copyright (c) 2018 Jonah Seguin (Shawckz).  All rights reserved.  You may not modify, decompile, distribute or use any code/text contained in this document(plugin) without explicit signed permission from Jonah Seguin.
 */

package com.jonahseguin.papaya.command;

import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.backend.config.PapayaLang;
import com.jonahseguin.papaya.player.PapayaProfile;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Jonah on 5/8/2018.
 * Project: Factions
 *
 * @ 6:49 PM
 */
public abstract class PapayaCommand {

    public PapayaProfile getSender(CommandSender sender) {
        if (isPlayer(sender)) {
            Player player = (Player) sender;
            return Papaya.getInstance().getProfileCache().get(player);
        }
        return null;
    }

    public boolean isPlayer(CommandSender sender) {
        return sender instanceof Player;
    }

    public String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public String format(String s, Object... args) {
        return Papaya.format(s, args);
    }

    public String format(PapayaLang lang, Object... args) {
        return lang.format(args);
    }

}
