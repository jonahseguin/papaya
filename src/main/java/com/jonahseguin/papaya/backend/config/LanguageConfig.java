/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.papaya.backend.config;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

@Getter
@Setter
public class LanguageConfig extends LocalConfig {

    public LanguageConfig(Plugin plugin) {
        super(plugin, "lang.yml");
        load();
        save();

        for (PapayaLang lang : PapayaLang.values()) {//Load default values into local memory hashmap cache
            if (!this.lang.containsKey(lang)) {
                this.lang.put(lang, lang.getDefaultValue());
            }
        }

        for (PapayaLang key : lang.keySet()) {//Save values into config that don't exist already
            if (!getConfig().contains(key.toString())) {
                getConfig().set(key.toString(), lang.get(key));
            }
        }

        for (String key : getConfig().getKeys(false)) {//Load lang values from config into local memory hashmap cache
            PapayaLang lang = PapayaLang.fromString(key);
            if (lang != null) {
                this.lang.put(lang, getConfig().getString(key));
            }
        }
        save();
    }


    private final Map<PapayaLang, String> lang = new HashMap<>();

    public String getFormattedLang(PapayaLang lang, Object... args) {
        if (!this.lang.containsKey(lang)) {
            this.lang.put(lang, lang.getDefaultValue());
        }
        String val = this.lang.get(lang);
        if (args != null) {
            if (args.length > 0) {
                for (int i = 0; i < args.length; i++) {
                    if (val.contains("{" + i + "}")) {
                        val = val.replace("{" + i + "}", args[i].toString());
                    }
                }
            }
        }
        return ChatColor.translateAlternateColorCodes('&', val);
    }

}
