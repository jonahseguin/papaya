package com.jonahseguin.papaya.check.alert;

import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.backend.config.PapayaLang;
import com.jonahseguin.papaya.backend.permission.PapayaPerm;
import com.jonahseguin.papaya.check.violation.Violation;
import com.jonahseguin.papaya.player.PapayaProfile;

import org.bukkit.ChatColor;

/**
 * Created by Jonah on 6/4/2018.
 * Project: papaya
 *
 * @ 4:39 PM
 */
public class AlertManager {

    private final Papaya papaya;

    public AlertManager(Papaya papaya) {
        this.papaya = papaya;
    }

    public void sendAlert(String message) {
        for (PapayaProfile profile : papaya.getProfileCache().getCache().getOnlineProfiles()) {
            if (profile != null && profile.isOnline() && profile.getPlayer().hasPermission(PapayaPerm.ALERTS.getPermission()) && profile.isAlerts()) {
                profile.msg(PapayaLang.ALERT_GENERIC, message);
            }
        }
        this.consoleAlert(message);
    }

    public void sendAlertNoPrefix(String message) {
        for (PapayaProfile profile : papaya.getProfileCache().getCache().getOnlineProfiles()) {
            if (profile != null && profile.isOnline() && profile.getPlayer().hasPermission(PapayaPerm.ALERTS.getPermission()) && profile.isAlerts()) {
                profile.getPlayer().sendMessage(message);
            }
        }
        this.consoleAlert(message);
    }

    public void checkAlert(Violation violation, int vl) {
        if (violation != null) {
            if (violation.getDetail().length() > 0) {
                sendAlertNoPrefix(PapayaLang.ALERT_CHECK_GENERIC_DETAIL.format(violation.getProfile().getName(), violation.getCheckType().getName(), violation.getDetail(), vl));
            }
            else {
                sendAlertNoPrefix(PapayaLang.ALERT_CHECK_GENERIC.format(violation.getProfile().getName(), violation.getCheckType().getName(), vl));
            }
        }
    }

    public void consoleAlert(String message) {
        papaya.getLogger().info(ChatColor.stripColor(PapayaLang.ALERT_GENERIC.format(message)));
    }

}
