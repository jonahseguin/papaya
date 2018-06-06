package com.jonahseguin.papaya.command.commands;

import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.command.PapayaCommand;
import com.sk89q.intake.Command;
import com.sk89q.intake.Require;
import li.l1t.common.intake.provider.annotation.Sender;

import org.bukkit.command.CommandSender;

/**
 * Created by Jonah on 5/30/2018.
 * Project: papaya
 *
 * @ 8:39 AM
 */
public class CmdPapaya extends PapayaCommand {

    @Command(aliases = "", desc = "Main Papaya Command")
    @Require("papaya.cmd.papaya")
    public void onCmdPapaya(@Sender CommandSender sender) {
        sender.sendMessage(color("&8&m---&r &6Papaya &fCheat Protection &8&m---"));
        sender.sendMessage(color("&7v" + Papaya.getInstance().getDescription().getVersion() + ", developed by Jonah Seguin (Shawckz)"));
        sender.sendMessage(color("&fFor help or commands use &6/papaya help &for &6/papaya commands&f."));
        sender.sendMessage(color("&fFor additional help and information, visit &6papaya.purified.io/help&f."));
    }

}
