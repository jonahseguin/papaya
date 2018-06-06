package com.jonahseguin.papaya.player;

import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.backend.config.PapayaLang;
import com.jonahseguin.papaya.backend.permission.PapayaPerm;
import com.jonahseguin.payload.common.cache.CacheDebugger;

import org.bukkit.entity.Player;

/**
 * Created by Jonah on 12/27/2017.
 * Project: purifiedCore
 *
 * @ 3:37 PM
 */
public class PapayaCacheDebugger implements CacheDebugger {

    private final Papaya papaya;

    public PapayaCacheDebugger() {
        this.papaya = Papaya.getInstance();
    }

    @Override
    public void debug(String s) {
        Papaya.debug(s);
    }

    @Override
    public void error(Exception e) {
        papaya.getErrorHandler().addRecord(e, PapayaLang.CACHE_ERROR_PREFIX.format());
    }

    @Override
    public void error(Exception e, String s) {
        papaya.getErrorHandler().addRecord(e, PapayaLang.CACHE_ERROR_PREFIX.format() + s);
    }

    @Override
    public boolean onStartupFailure() {
        papaya.getPapayaConfig().setMaintenance(true);
        for (Player pl : papaya.getServer().getOnlinePlayers()) {
            if (PapayaPerm.ADMIN.has(pl)) {
                pl.kickPlayer(PapayaLang.CACHE_FAIL_KICK_MESSAGE.format());
            } else {
                pl.sendMessage(PapayaLang.CACHE_FAIL_ADMIN_MESSAGE.format());
            }
        }
        return true;
    }
}
