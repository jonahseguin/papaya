package com.jonahseguin.papaya.command.commands;

import com.jonahseguin.papaya.backend.config.PapayaLang;
import com.jonahseguin.papaya.command.PapayaCommand;
import com.jonahseguin.papaya.command.PapayaCommands;
import com.jonahseguin.papaya.player.PapayaProfile;
import com.sk89q.intake.Command;
import com.sk89q.intake.Require;
import li.l1t.common.intake.provider.annotation.Sender;

/**
 * Created by Jonah on 6/5/2018.
 * Project: papaya
 *
 * @ 3:15 PM
 */
public class CmdAlerts extends PapayaCommand {

    @Command(aliases = "", desc = "Toggle alerts")
    @Require("papaya.cmd.alerts")
    public void onCmdAlerts(@Sender PapayaProfile profile) {
        profile.setAlerts(!profile.isAlerts());
        profile.msgPrefixed(PapayaLang.CMD_ALERTS, (profile.isAlerts() ? "&aenabled" : "&cdisabled"));
    }

}
