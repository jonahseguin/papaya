/*
 * Copyright (c) 2018 Jonah Seguin (Shawckz).  All rights reserved.  You may not modify, decompile, distribute or use any code/text contained in this document(plugin) without explicit signed permission from Jonah Seguin.
 */

package com.jonahseguin.papaya.command.provider;

import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.player.PapayaProfile;
import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;
import li.l1t.common.intake.exception.CommandExitMessage;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Jonah on 5/8/2018.
 * Project: Factions
 *
 * @ 5:22 PM
 */
public class PapayaProfileSenderProvider implements Provider<PapayaProfile> {

    @Override
    public boolean isProvided() {
        return true;
    }

    @Nullable
    @Override
    public PapayaProfile get(CommandArgs commandArgs, List<? extends Annotation> list) throws ArgumentException, ProvisionException {
        CommandSender sender = getFromNamespaceOrFail(commandArgs, CommandSender.class);
        if (sender != null) {
            if (sender instanceof Player) {
                final Player player = (Player) sender;
                return Papaya.getInstance().getProfileCache().get(player);
            } else {
                throw new CommandExitMessage("Papaya: This is a player-only command.");
            }
        } else {
            throw new CommandExitMessage("Papaya: Could not send command because your sender is null, please report this error to an administrator.");
        }
    }

    @Override
    public List<String> getSuggestions(String s) {
        return Collections.emptyList();
    }

    public <T> T getFromNamespaceOrFail(CommandArgs arguments, Class<T> key) throws ProvisionException {
        T value = arguments.getNamespace().get(key);
        if (value == null) {
            throw new ProvisionException("No " + key.getSimpleName() + " in namespace");
        }
        return value;
    }

}
