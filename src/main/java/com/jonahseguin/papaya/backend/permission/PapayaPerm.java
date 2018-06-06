package com.jonahseguin.papaya.backend.permission;

import com.jonahseguin.papaya.player.PapayaProfile;

import org.bukkit.command.CommandSender;

/**
 * Created by Jonah on 5/29/2018.
 * Project: papaya
 *
 * @ 7:52 PM
 */
public enum PapayaPerm {

    // Permissions: papaya.<permission>

    ALERTS("alerts"),
    ADMIN("admin"),
    SNIFF("sniff"),
    CMD_PAPAYA("cmd.papaya"),
    ERRORS("errors")
    ;

    public static final String PERMISSION_ROOT = "papaya";

    public final String permission;

    PapayaPerm(final String permission) {
        this.permission = PERMISSION_ROOT + "." + permission;
    }

    public boolean has(CommandSender sender) {
        return sender.hasPermission(permission);
    }

    public boolean has(PapayaProfile profile) {
        return profile.isOnline() && profile.getPlayer().hasPermission(permission);
    }

    public final String getPermission() {
        return permission;
    }

}
