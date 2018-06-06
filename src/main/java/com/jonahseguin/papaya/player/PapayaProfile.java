package com.jonahseguin.papaya.player;

import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.backend.config.PapayaLang;
import com.jonahseguin.papaya.backend.permission.PapayaPerm;
import com.jonahseguin.papaya.check.CheckFailure;
import com.jonahseguin.papaya.check.CheckType;
import com.jonahseguin.papaya.player.data.ProfileClientData;
import com.jonahseguin.papaya.player.data.ProfileServerData;
import com.jonahseguin.papaya.player.record.ProfileRecord;
import com.jonahseguin.papaya.sniffing.PacketSniffManager;
import com.jonahseguin.papaya.sniffing.PacketSniffType;
import com.jonahseguin.payload.profile.profile.PayloadProfile;
import lombok.Getter;
import lombok.Setter;
import org.mongodb.morphia.annotations.Entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Created by Jonah on 5/29/2018.
 * Project: papaya
 *
 * @ 7:53 PM
 */
@Getter
@Setter
@Entity("papayaProfiles")
public class PapayaProfile extends PayloadProfile {

    private boolean debug = false;
    private boolean alerts = false;
    private long checkCooldownEnd = 0; // timestamp it ends
    private transient Set<PacketSniffType> sniffing = new HashSet<>();
    private ProfileRecord record = new ProfileRecord();

    private transient ProfileClientData clientData;
    private transient ProfileServerData serverData;
    private transient Map<CheckType, Long> alertCooldowns = new HashMap<>();

    public PapayaProfile() {
    }

    public PapayaProfile(String name, String uniqueId) {
        this.name = name;
        this.uniqueId = uniqueId;
    }

    @Override
    public void initialize(Player player) {
        super.initialize(player);
        this.clientData = new ProfileClientData(this);
        this.serverData = new ProfileServerData(this);
        this.checkCooldownEnd = System.currentTimeMillis() + (1000 * 3);
        if (player.hasPermission(PapayaPerm.ALERTS.getPermission())) {
            if (!this.alerts) {
                Papaya.getInstance().getPapayaCommands().getCmdAlerts().onCmdAlerts(this);
            }
        }
    }

    public boolean shouldCheckMovement() {
        if (isOnline()) {
            return player.getGameMode() == GameMode.SURVIVAL && !player.isFlying() && !player.getAllowFlight();
        }
        return false;
    }

    public boolean onCheckCooldown() {
        return checkCooldownEnd > System.currentTimeMillis();
    }

    public boolean isOnline() {
        return getPlayer() != null && getPlayer().isOnline();
    }

    public void startSniffing(PacketSniffType type) {
        sniffing.add(type);
    }

    public void stopSniffing(PacketSniffType type) {
        sniffing.remove(type);
    }

    public boolean isSniffing(PacketSniffType type) {
        return sniffing.contains(type);
    }

    public void msg(PapayaLang lang, Object... args) {
        if (isOnline()) {
            player.sendMessage(lang.format(args));
        }
    }

    public void msgPrefixed(PapayaLang lang, Object... args) {
        if (isOnline()) {
            player.sendMessage(lang.formatWithPrefix(args));
        }
    }

    public void alertCooldown(CheckType type, int seconds) {
        alertCooldowns.put(type, System.currentTimeMillis() + (1000 * seconds));
    }

    public long getAlertCooldown(CheckType type) {
        long finish = 0;
        if (alertCooldowns.containsKey(type)) {
            return alertCooldowns.get(type);
        }
        return finish;
    }

    public boolean onAlertCooldown(CheckType type) {
        return getAlertCooldown(type) > System.currentTimeMillis();
    }

    public int getPing() {
        return ((CraftPlayer) player).getHandle().ping;
    }

}
