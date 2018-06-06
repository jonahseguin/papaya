/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.papaya.backend.config;

import com.jonahseguin.papaya.Papaya;

public enum PapayaLang {

    CACHE_FAIL_KICK_MESSAGE("&cThe server is experiencing technical difficulties.  Please join back later."),
    CACHE_FAIL_ADMIN_MESSAGE("&6Papaya&7: &c&lAll non-administrative players were kicked due to a caching failure."),
    CACHE_ERROR_PREFIX("[Papaya Cache: Error] "),

    DEBUG_FORMAT("&7[&6Papaya &8Debug&7] &7{0}"),
    ALERT_GENERIC("&8[&6!&8] &f{0}"),
    PAPAYA_PREFIX("&8[&6!&8] "),
    ALERT_CHECK_GENERIC("&8[&6!&8] &f{0} &cfailed &f{1} &8[{2}]"), // [!] player failed Fly [1]
    ALERT_CHECK_GENERIC_DETAIL("&8[&6!&8] &f{0} &cfailed &f{1} &8({2}) &8[{3}]"), // [!] player failed Fly (3 b/s) [1]
    LAG_SPIKE_PARDONED("&cLag spike detected &8[{0}ms]&7: &6{1} &fcheck failures have been pardoned."),

    ONLINE_PLAYER_NOT_FOUND("&6Papaya&7: &cPlayer '{0}' is not online."),
    PLAYER_NOT_FOUND("&6Papaya&7: &cA profile with name '{0}' does not exist."),

    SNIFF_CLEAR("&fAll active packet sniffers have been stopped."),
    SNIFF_CLIENTBOUND_KEEP_ALIVE("&7[&eServer&7]&7[TT{0}][RT{1}] &f{2}: &7Keep-Alive ID {3}"),
    SNIFF_SERVERBOUND_KEEP_ALIVE("&7[&bClient&7]&7[TT{0}][RT{1}] &f{2}: &7Keep-Alive ID {3}"),
    SNIFF_SERVERBOUND_USE_ENTITY("&7[&bClient&7]&7[TT{0}][RT{1}] &f{2}: &7Use Entity {3} {4}"),
    SNIFF_SERVERBOUND_ON_GROUND("&7[&bClient&7]&7[TT{0}][RT{1}] &f{2}: &7On Ground: {3}"),
    SNIFF_SERVERBOUND_ENTITY_ACTION("&7[&bClient&7]&7[TT{0}][RT{1}] &f{2}: &7Action: {3}"),
    SNIFF_SERVERBOUND_POSITION("&7[&bClient&7]&7[TT{0}][RT{1}] &f{2}: &7Position: [{3}, {4}, {5}][{6}][ground: {7}]"),
    SNIFF_SERVERBOUND_LOOK("&7[&bClient&7]&7[TT{0}][RT{1}] &f{2}: &7Look: [yaw: {3}][pitch: {4}][ground: {5}]"),
    SNIFF_SERVERBOUND_POSITION_LOOK("&7[&bClient&7]&7[TT{0}][RT{1}] &f{2}: &7P+L: [{3}, {4}, {5}][yaw: {6}][pitch: {7}][{8}][ground: {9}]"),
    SNIFF_SERVERBOUND_ARM_ANIMATION("&7[&bClient&7]&7[TT{0}][RT{1}] &f{2}: &7Arm anim: {3}"),

    CMD_DEBUG("&6Papaya&7 debug mode: &f{0}"),
    CMD_ALERTS("&7Alerts have been {0}&7.")
    ;

    private final String defaultValue;

    PapayaLang(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public static PapayaLang fromString(String s) {
        for (PapayaLang lang : values()) {
            if (lang.toString().equalsIgnoreCase(s)) {
                return lang;
            }
        }
        return null;
    }

    public String format(Object... args) {
        return Papaya.getInstance().getLanguageConfig().getFormattedLang(this, args);
    }

    public String formatWithPrefix(Object... args) {
        return PapayaLang.PAPAYA_PREFIX.format() + format(args);
    }

    public String getDefaultValue() {
        return defaultValue;
    }

}
