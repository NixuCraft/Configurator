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

    public enum FileSearchLocations {
        PLUGIN_RESOURCES,
        DATA_FOLDER,
        BOTH
    }
    // /**
    //  * @param sourceType the source type, either a key inside the main config or a separate file.
    //  * @param source If MAIN_CONFIG the subkey, if OTHER_FILE the file name.
    //  * @param saveToUserFolderIfNotPresent Only affects OTHER_FILE sourceType, if a file isn't found in the user folder but is found in the ressources, this determines it if should be saved to the user folder or not. Eg you'd set it to "true" for another config, but "false" for some localization data which should never get changed.
    //  */
    /**
     * @param subKey The subkey inside of the main config in which the config is located.
     */
    public ConfigPart(String subKey) {
        rootConfig = Configurator.getFileConfigBlock(subKey);
    }

    /**
     * @param sourceFile The name of the file which contains the config you want.
     * @param sourceLocation The location in which the file is located (data folder, plugin resources or both).
     * @param saveToUserFolderIfNotPresent If enabled, if sourceLocation is pluginresources/both and if it gets to the pluginresources path (=passed the datafolder part if both), it'll attempt to save the pluginresources file to the datafolder once it's at the pluginresources loading part.
     */
    public ConfigPart(String sourceFile, FileSearchLocations sourceLocation, boolean saveToUserFolderIfNotPresent) {
        rootConfig = loadConfigFromOtherFile(sourceFile, sourceLocation, saveToUserFolderIfNotPresent);
    }
    private ConfigurationSection loadConfigFromOtherFile(String fileName, FileSearchLocations sourceLocation, boolean saveToUserFolderIfNotPresent) {
        JavaPlugin plugin = Configurator.getPlugin();

        // First check if file exists for user (if enabled)
        if (sourceLocation == FileSearchLocations.DATA_FOLDER || sourceLocation == FileSearchLocations.BOTH) {
            File userFile = new File(plugin.getDataFolder(), fileName);
            if (userFile.exists()) {
                InputStream stream;
                try {
                    stream = new FileInputStream(userFile);
                    if (stream != null) {
                        Reader reader = new BufferedReader(new InputStreamReader(stream));
                        return YamlConfiguration.loadConfiguration(reader).getConfigurationSection("");
                    }
                } catch (FileNotFoundException e) {}
            }
        }

        // Otherwise check if it exists in the plugin dir
        if (sourceLocation == FileSearchLocations.PLUGIN_RESOURCES || sourceLocation == FileSearchLocations.BOTH) {
            InputStream stream = Configurator.getPlugin().getResource(fileName);
            if (stream != null) {
                if (saveToUserFolderIfNotPresent)
                    Configurator.getPlugin().saveResource(fileName, false);
                Reader reader = new BufferedReader(new InputStreamReader(stream));
                return YamlConfiguration.loadConfiguration(reader).getConfigurationSection("");
            }
        }

        // Otherwise error out if nothing found.
        throw new ConfigLoadException(String.format("Couldn't find config file %s. Search range: %s.", fileName, sourceLocation));
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

    protected List<Integer> getIntegerList(ConfigurationSection conf, String key, List<Integer> defaultVal) {
        List<Integer> value;
        try {
            value = conf.getIntegerList(key);
        } catch (Exception e) {
            throw new ConfigParseException(String.format("Invalid key \"%s\" (String list). Please check your config. Defaulted to \"%s\"", key, defaultVal));
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

    // NOTE:
    // as of now the default values DON'T DO ANYTHING
    // as I prefeer for it to error out every time 
    // Will be changed !
}