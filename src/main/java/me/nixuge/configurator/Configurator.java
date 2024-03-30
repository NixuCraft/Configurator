package me.nixuge.configurator;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class Configurator {
    // private static Configurator configuratorInstance;

    private static JavaPlugin plugin;
    private static FileConfiguration mainConfig;
    
    public static void saveConfig() {
        mainConfig.options().copyDefaults(true);
        plugin.saveConfig();
    }

    public static ConfigurationSection getFileConfigBlock(String str) {
        return mainConfig.getConfigurationSection(str);
    }
    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public void init(JavaPlugin main) {
        plugin = main;
        // configuratorInstance = this;

        FileConfiguration conf = plugin.getConfig();
        if (conf.getValues(false).size() == 0) {
            plugin.saveDefaultConfig();
            plugin.reloadConfig();
        }
        mainConfig = conf;

        initConfigParts();
    }
    protected abstract void initConfigParts();
}