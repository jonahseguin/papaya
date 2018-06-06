package com.jonahseguin.papaya.command;

import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.command.annotation.Online;
import com.jonahseguin.papaya.command.commands.CmdAlerts;
import com.jonahseguin.papaya.command.commands.CmdDebug;
import com.jonahseguin.papaya.command.commands.CmdPapaya;
import com.jonahseguin.papaya.command.commands.CmdSniff;
import com.jonahseguin.papaya.command.provider.OnlinePapayaProfileProvider;
import com.jonahseguin.papaya.command.provider.PapayaProfileProvider;
import com.jonahseguin.papaya.command.provider.PapayaProfileSenderProvider;
import com.jonahseguin.papaya.player.PapayaProfile;
import li.l1t.common.intake.CommandBuilder;
import li.l1t.common.intake.CommandsManager;
import li.l1t.common.intake.provider.annotation.Sender;
import lombok.Getter;

import java.util.Locale;

/**
 * Created by Jonah on 5/30/2018.
 * Project: papaya
 *
 * @ 9:05 AM
 */
@Getter
public class PapayaCommands {

    private final Papaya papaya;
    private final CommandsManager commandsManager;

    private final CmdPapaya cmdPapaya = new CmdPapaya();
    private final CmdSniff cmdSniff = new CmdSniff();
    private final CmdDebug cmdDebug = new CmdDebug();
    private final CmdAlerts cmdAlerts = new CmdAlerts();

    public PapayaCommands(Papaya papaya) {
        this.papaya = papaya;
        this.commandsManager = new CommandsManager(papaya);
    }

    public void register() {
        commandsManager.setLocale(Locale.ENGLISH);
        this.registerInjections();
        this.registerCommands();
    }

    private void registerCommands() {
        this.commandsManager.registerCommand(cmdPapaya, "papaya", "!", "ac", "pap", "pa", "aya");
        CommandBuilder sub = this.commandsManager.getBuilderFor("papaya");
        sub.withSubHandler(cmdSniff, "sniff", "sniffer", "packetsniff");
        sub.withSubHandler(cmdDebug, "debug");
        sub.withSubHandler(cmdAlerts, "alerts");
        sub.register();

        this.commandsManager.registerCommand(cmdAlerts, "alerts", "palerts");

    }

    private void registerInjections() {
        commandsManager.putIntoNamespace(Papaya.class, papaya);
        commandsManager.bind(PapayaProfile.class).toProvider(new PapayaProfileProvider());
        commandsManager.bind(PapayaProfile.class).annotatedWith(Online.class).toProvider(new OnlinePapayaProfileProvider());
        commandsManager.bind(PapayaProfile.class).annotatedWith(Sender.class).toProvider(new PapayaProfileSenderProvider());
    }

}
