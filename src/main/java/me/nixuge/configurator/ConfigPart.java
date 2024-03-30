package me.nixuge.configurator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.nixuge.configurator.error.ConfigLoadException;
import me.nixuge.configurator.error.ConfigParseException;

public abstract class ConfigPart extends ConfigStringParsing {
    protected ConfigurationSection rootConfig;

    public enum ConfigSourceType {
        MAIN_CONFIG,
        OTHER_FILE
    }
    /**
     * @param sourceType the source type, either a key inside the main config or a separate file.
     * @param source If MAIN_CONFIG the subkey, if OTHER_FILE the file name.
     */
    public ConfigPart(ConfigSourceType sourceType, String source) {
        switch (sourceType) {
            case MAIN_CONFIG:
                rootConfig = Configurator.getFileConfigBlock(source);
                break;
            case OTHER_FILE:
                rootConfig = loadConfigFromOtherFile(source);
                break;
        }
    }
    private ConfigurationSection loadConfigFromOtherFile(String fileName) {
        JavaPlugin plugin = Configurator.getPlugin();
        // First check if file exists for user
        File userFile = new File(plugin.getDataFolder(), fileName);
        if (userFile.exists()) {
            InputStream stream;
            try {
                stream = new FileInputStream(userFile);
                if (stream != null) {
                    Reader reader = new BufferedReader(new InputStreamReader(stream));
                    return YamlConfiguration.loadConfiguration(reader).getDefaultSection();
                }
            } catch (FileNotFoundException e) {}
        }
        // Otherwise check if it exists in the plugin dir
        InputStream stream = Configurator.getPlugin().getResource(fileName);
        if (stream != null) {
            Reader reader = new BufferedReader(new InputStreamReader(stream));
            return YamlConfiguration.loadConfiguration(reader).getDefaultSection();
        }
        // Otherwise error out
        throw new ConfigLoadException("Couldn't find config file " + fileName + ", neither in the user folder nor in the plugins resources.");
    }

    protected int getInt(ConfigurationSection conf, String key, int defaultVal) {
        int value;
        try {
            value = conf.getInt(key);
        } catch (Exception e) {
            throw new ConfigParseException(String.format("Invalid key \"%s\" (int). Please check your config. Defaulted to \"%s\"", key, defaultVal));
        }
        return value;
    }

    protected String getString(ConfigurationSection conf, String key, String defaultVal) {
        String value;
        try {
            value = conf.getString(key);
        } catch (Exception e) {
            throw new ConfigParseException(String.format("Invalid key \"%s\" (String). Please check your config. Defaulted to \"%s\"", key, defaultVal));
        }
        return value;
    }

    protected boolean getBoolean(ConfigurationSection conf, String key, boolean defaultVal) {
        boolean value;
        try {
            value = conf.getBoolean(key);
        } catch (Exception e) {
            throw new ConfigParseException(String.format("Invalid key \"%s\" (boolean). Please check your config. Defaulted to \"%s\"", key, defaultVal));
        }
        return value;
    }

    protected List<String> getStringList(ConfigurationSection conf, String key, List<String> defaultVal) {
        List<String> value;
        try {
            value = conf.getStringList(key);
        } catch (Exception e) {
            throw new ConfigParseException(String.format("Invalid key \"%s\" (String list). Please check your config. Defaulted to \"%s\"", key, defaultVal));
        }
        return value;
    }
}