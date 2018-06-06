/*
 * Copyright (c) 2018 Jonah Seguin (Shawckz).  All rights reserved.  You may not modify, decompile, distribute or use any code/text contained in this document(plugin) without explicit signed permission from Jonah Seguin.
 */

package com.jonahseguin.papaya.command.provider;

import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.backend.config.PapayaLang;
import com.jonahseguin.papaya.player.PapayaProfile;
import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;
import li.l1t.common.intake.exception.CommandExitMessage;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by Jonah on 5/8/2018.
 * Project: Factions
 *
 * @ 5:22 PM
 */
public class OnlinePapayaProfileProvider implements Provider<PapayaProfile> {

    @Override
    public boolean isProvided() {
        return false;
    }

    @Nullable
    @Override
    public PapayaProfile get(CommandArgs commandArgs, List<? extends Annotation> list) throws ArgumentException, ProvisionException {
        if (commandArgs.hasNext()) {
            final String name = commandArgs.next();
            final Player player = Bukkit.getPlayer(name);
            if (player != null) {
                return Papaya.getInstance().getProfileCache().get(player);
            } else {
                throw new CommandExitMessage(PapayaLang.ONLINE_PLAYER_NOT_FOUND.format(name));
            }
        } else {
            //throw new CommandExitMessage(ChatColor.RED + "Not enough arguments");
            return null;
        }
    }

    @Override
    public List<String> getSuggestions(String s) {
        return Papaya.getInstance().getProfileCache().getCache().getOnlineProfiles().stream().map(PapayaProfile::getName).filter(name -> name.toLowerCase().contains(s.toLowerCase().toLowerCase())).collect(Collectors.toList());
    }
}
