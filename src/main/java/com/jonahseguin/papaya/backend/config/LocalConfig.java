/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.papaya.backend.config;


import com.jonahseguin.papaya.backend.config.annotations.ConfigSerializer;
import com.jonahseguin.papaya.backend.config.annotations.SerializeData;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;


@Getter
public class LocalConfig {

    private final Plugin plugin;
    private final YamlConfiguration config;
    private final String fileName;
    private final File file;
    private final File directory;

    public LocalConfig(Plugin plugin) {
        this(plugin, "config.yml");
    }

    public LocalConfig(Plugin plugin, String filename) {
        this(plugin, filename, plugin.getDataFolder().getPath());
    }

    public LocalConfig(Plugin plugin, String filename, String directory) {
        this.plugin = plugin;
        this.fileName = filename;
        this.directory = new File(directory);
        this.file = new File(directory, filename);
        config = new YamlConfiguration();
        createFile();
    }

    public void createFile() {
        if (!directory.exists()) {
            directory.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException ex) {
            ex.printStackTrace();
        }
    }

    public void resetToDefaults() {
        loadDefaults();
        save();
    }

    public void saveDefaults() {
        File dir = new File(this.plugin.getDataFolder().getPath() + File.separator + "defaults");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(this.plugin.getDataFolder().getPath() + File.separator + "defaults" + File.separator + fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
                YamlConfiguration config = new YamlConfiguration();
                config.load(file);
                save(file, config);
            } catch (IOException | InvalidConfigurationException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void loadDefaults() {
        File file = new File(this.plugin.getDataFolder().getPath() + File.separator + "defaults" + File.separator + fileName);
        if (file.exists()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            load(config);
        } else {
            saveDefaults();
            loadDefaults();
        }
    }

    public void save() {
        save(file, config);
    }

    public void save(File file, YamlConfiguration config) {
        Field[] toSave = this.getClass().getDeclaredFields();
        for (Field f : toSave) {
            if (f.isAnnotationPresent(SerializeData.class)) {
                SerializeData configData = f.getAnnotation(SerializeData.class);
                try {
                    f.setAccessible(true);
                    Object saveValue = f.get(this);
                    if (f.isAnnotationPresent(ConfigSerializer.class)) {
                        ConfigSerializer serializer = f.getAnnotation(ConfigSerializer.class);
                        AbstractSerializer as = (AbstractSerializer) serializer.serializer().newInstance();
                        saveValue = as.toString(saveValue);
                    }
                    config.addDefault(configData.value(), saveValue);
                    config.set(configData.value(), saveValue);
                } catch (IllegalAccessException | InstantiationException ex) {
                    ex.printStackTrace();
                }
            }
        }
        try {
            config.save(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean setValue(String key, String value) {
        Field[] toLoad = this.getClass().getDeclaredFields();
        for (Field f : toLoad) {
            if (f.isAnnotationPresent(SerializeData.class)) {
                SerializeData configData = f.getAnnotation(SerializeData.class);
                f.setAccessible(true);
                if (configData.value().equalsIgnoreCase(key)) {
                    Object val = value;
                    if (!f.isAnnotationPresent(ConfigSerializer.class)) {
                        val = LocalConfig.toObject(f.getType(), value);
                        try {
                            f.set(this, val);
                        } catch (IllegalAccessException expected) {
                            expected.printStackTrace();
                            return false;
                        }
                    } else {
                        try {
                            AbstractSerializer serializer = (AbstractSerializer) f.getAnnotation(ConfigSerializer.class).serializer().newInstance();
                            f.set(this, serializer.fromString(value));
                        } catch (InstantiationException | IllegalAccessException ex) {
                            ex.printStackTrace();
                            return false;
                        }
                    }
                }
            }
        }
        save();
        return true;
    }

    public void load(YamlConfiguration config) {
        Field[] toLoad = this.getClass().getDeclaredFields();
        for (Field f : toLoad) {
            f.setAccessible(true);
            if (f.isAnnotationPresent(SerializeData.class)) {
                SerializeData configData = f.getAnnotation(SerializeData.class);
                if (config.contains(configData.value())) {
                    f.setAccessible(true);
                    if (!f.isAnnotationPresent(ConfigSerializer.class)) {
                        try {
                            f.set(this, config.get(configData.value()));
                        } catch (IllegalAccessException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        try {
                            AbstractSerializer serializer = (AbstractSerializer) f.getAnnotation(ConfigSerializer.class).serializer().newInstance();
                            f.set(this, serializer.fromString(config.get(configData.value())));
                        } catch (InstantiationException | IllegalAccessException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public void load() {
        load(config);
    }

    public static Object toObject(Class clazz, String value) {
        if (Boolean.class == clazz || Boolean.TYPE == clazz) return Boolean.parseBoolean(value);
        if (Byte.class == clazz || Byte.TYPE == clazz) return Byte.parseByte(value);
        if (Short.class == clazz || Short.TYPE == clazz) return Short.parseShort(value);
        if (Integer.class == clazz || Integer.TYPE == clazz) return Integer.parseInt(value);
        if (Long.class == clazz || Long.TYPE == clazz) return Long.parseLong(value);
        if (Float.class == clazz || Float.TYPE == clazz) return Float.parseFloat(value);
        if (Double.class == clazz || Double.TYPE == clazz) return Double.parseDouble(value);
        return value;
    }
}
