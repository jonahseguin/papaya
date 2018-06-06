package com.jonahseguin.papaya.command.commands;

import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.backend.config.PapayaLang;
import com.jonahseguin.papaya.command.PapayaCommand;
import com.jonahseguin.papaya.command.annotation.Online;
import com.jonahseguin.papaya.player.PapayaProfile;
import com.sk89q.intake.Command;
import com.sk89q.intake.Require;
import li.l1t.common.intake.provider.annotation.Sender;

/**
 * Created by Jonah on 5/30/2018.
 * Project: papaya
 *
 * @ 9:33 AM
 */
public class CmdDebug extends PapayaCommand {

    @Command(aliases = "", desc = "Toggle Papaya Debug mode")
    @Require("papaya.cmd.debug")
    public void onCmdSniff(@Sender PapayaProfile sender) {
        sender.setDebug(!sender.isDebug());
        sender.msg(PapayaLang.CMD_DEBUG, (sender.isDebug() ? "&aOn" : "&cOff"));
    }

}
