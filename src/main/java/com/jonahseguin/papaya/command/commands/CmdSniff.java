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
public class CmdSniff extends PapayaCommand {

    @Command(aliases = "", desc = "Packet Sniffing", min = 1, usage = "<online player>")
    @Require("papaya.cmd.sniff")
    public void onCmdSniff(@Sender PapayaProfile sender, @Online PapayaProfile target) {
        Papaya.getInstance().getPacketSniffManager().getGui().getMenuFor(target).open(sender.getPlayer());
    }

    @Command(aliases = "clear", desc = "Clear active Packet Sniffers")
    @Require("papaya.cmd.sniff.clear")
    public void onCmdSniffClear(@Sender PapayaProfile sender) {
        for (PapayaProfile profile : Papaya.getInstance().getProfileCache().getCache().getOnlineProfiles()) {
            if (profile != null) {
                profile.getSniffing().clear();
            }
        }
        sender.msgPrefixed(PapayaLang.SNIFF_CLEAR);
    }

}
