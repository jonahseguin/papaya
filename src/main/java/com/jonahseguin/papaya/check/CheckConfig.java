/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.papaya.check;

import com.jonahseguin.papaya.Papaya;
import com.jonahseguin.papaya.backend.config.AbstractSerializer;
import com.jonahseguin.papaya.backend.config.annotations.ConfigData;
import com.jonahseguin.papaya.backend.config.annotations.ConfigSerializer;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Superclass of all Checks, sorted into directories based on their RCheckType
 * Each individual check has it's own Config file
 * All fields annotated with @ConfigData will be automatically loaded to the fields from the respective configuration file upon #load being called.
 * And saved from the local fields to the config when #save is called.
 */
public class CheckConfig {

    private transient final File file;
    private transient final File directory;
    private transient YamlConfiguration config;
    private transient final CheckType checkType;


    public CheckConfig(CheckType checkType) {
        String directory = Papaya.getInstance().getDataFolder().getPath();
        this.checkType = checkType;
        this.directory = new File(directory + File.separator + "checks");
        this.file = new File(this.directory, checkType.getName() + ".yml");
        this.config = new YamlConfiguration();
        createFile();
        setupConfig();
        saveDefaults();
    }


    public void createFile() {
        if (!directory.exists()) {
            directory.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Papaya.getInstance().getErrorHandler().addRecord(ex, "CheckConfig: Could not createNewFile (createFile)");
            }
        }
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException ex) {
            Papaya.getInstance().getErrorHandler().addRecord(ex, "CheckConfig: Could not load config (createFile)");
        }
    }

    public void setupConfig() {
        save();
        load();
        saveConfig();
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public void save() {
        save(file, config);
    }

    public void save(File file, YamlConfiguration config) {
        Class c = this.getClass();

        List<Field> fields = new ArrayList<>();

        for (Field f : c.getDeclaredFields()) {
            fields.add(f);
        }

        c = this.getClass().getSuperclass();

        for (Field f : c.getDeclaredFields()) {
            fields.add(f);
        }

        c = c.getSuperclass();

        for (Field f : c.getDeclaredFields()) {
            fields.add(f);
        }

        c = c.getSuperclass();

        for (Field f : c.getDeclaredFields()) {
            fields.add(f);
        }


        for (Field f : fields) {
            if (f.isAnnotationPresent(ConfigData.class)) {
                ConfigData configData = f.getAnnotation(ConfigData.class);
                try {
                    f.setAccessible(true);
                    Object saveValue = f.get(this);

                    if (f.isAnnotationPresent(ConfigSerializer.class)) {
                        ConfigSerializer serializer = f.getAnnotation(ConfigSerializer.class);
                        AbstractSerializer as = serializer.serializer().newInstance();
                        saveValue = as.toString(saveValue);
                    }
                    final String path = configData.value();
                    try {
                        config.addDefault(path, saveValue);
                        config.set(path, saveValue);
                    } catch (Exception ex) {
                        Papaya.getInstance().getErrorHandler().addRecord(ex, "CheckConfig: Could not set to config (save)");
                    }
                } catch (IllegalAccessException | InstantiationException ex) {
                    Papaya.getInstance().getErrorHandler().addRecord(ex, "CheckConfig: Could not get AbstractSerializer instance (save)");
                }
            }
        }
        saveConfig(config, file);
    }

    public void saveConfig() {
        saveConfig(config, file);
    }

    public void saveConfig(YamlConfiguration config, File file) {
        try {
            config.save(file);
        } catch (IOException ex) {
            Papaya.getInstance().getErrorHandler().addRecord(ex, "CheckConfig: Could not save config (saveConfig)");
        }
    }

    public void load() {
        load(config);
    }

    public void load(YamlConfiguration config) {
        Class c = this.getClass();

        List<Field> fields = new ArrayList<>();

        for (Field f : c.getDeclaredFields()) {
            fields.add(f);
        }

        c = this.getClass().getSuperclass();


        for (Field f : c.getDeclaredFields()) {
            fields.add(f);
        }
        c = c.getSuperclass();


        for (Field f : c.getDeclaredFields()) {
            fields.add(f);
        }

        c = c.getSuperclass();


        for (Field f : c.getDeclaredFields()) {
            fields.add(f);
        }

        for (Field f : fields) {
            f.setAccessible(true);
            if (f.isAnnotationPresent(ConfigData.class)) {
                ConfigData configData = f.getAnnotation(ConfigData.class);
                final String path = configData.value();
                if (config.contains(path)) {
                    f.setAccessible(true);
                    if (!f.isAnnotationPresent(ConfigSerializer.class)) {
                        try {
                            f.set(this, config.get(path));
                        } catch (IllegalAccessException ex) {
                            Papaya.getInstance().getErrorHandler().addRecord(ex, "CheckConfig: Could not set field (AbstractSerializer error)");
                        }
                    } else {
                        try {
                            AbstractSerializer serializer = f.getAnnotation(ConfigSerializer.class).serializer().newInstance();
                            f.set(this, serializer.fromString(config.get(path)));
                        } catch (InstantiationException | IllegalAccessException ex) {
                            Papaya.getInstance().getErrorHandler().addRecord(ex, "CheckConfig: Could not set field (AbstractSerializer error)");
                        }
                    }
                }
            }
        }
    }

    public void resetToDefaults() {
        loadDefaults();
        save();
    }

    public void saveDefaults() {
        File dir = new File(Papaya.getInstance().getDataFolder().getPath() + File.separator + "defaults");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(Papaya.getInstance().getDataFolder().getPath() + File.separator + "defaults" + File.separator + checkType.getName() + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
                YamlConfiguration config = new YamlConfiguration();
                config.load(file);
                save(file, config);
            } catch (IOException | InvalidConfigurationException ex) {
                Papaya.getInstance().getErrorHandler().addRecord(ex, "CheckConfig: Could not save defaults (saveDefaults)");
            }
        }
    }

    public void loadDefaults() {
        File file = new File(Papaya.getInstance().getDataFolder().getPath() + File.separator + "defaults" + File.separator + checkType.getName() + ".yml");
        if (file.exists()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            load(config);
        } else {
            Papaya.log("CheckConfig file default does not exist ("+checkType.getName()+"), creating and trying again");
            saveDefaults();
            loadDefaults();
        }
    }

    public File getFile() {
        return file;
    }
}
