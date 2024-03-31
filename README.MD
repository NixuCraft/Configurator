# Configurator
A mid-tier barebones config lib for Minecraft plugins.

# What is it useful for
It's good if you want to do have a config in the format `MyConfig.getSection().getProperty()`

It's also nice if you want some utils to parse strings into Locations/XYZs/Areas, the last two of which this plugin contains (at least for now).

### Note
As of now you need to shadow this into a plugin's jar, might have it as a plugin itself so it can load separately in the future.

# Usage
Make multiple classes that extend ConfigPart for config section you need.

Those classes need to call super in one of two ways:
- If only a string, it'll use that as a key in the main config for the rootConfig.
- If a string, a FileSearchLocations and a bool, it'll use the string as a filename and use that file as the rootConfig. For the other arguments, refeer to the javadocs
Here's an example class:
```java
public class GeneralConfigMainConfig extends ConfigPart {
    @Getter
    private final String language;
    
    public GeneralConfig() {
        super("general"); // rootConfig will be in config.yml under the key "general" 
        language = getString(rootConfig, "language", "en"); // 3rd parameter is default value. Refeer to the ConfigPart.java file.
    }
}

public class GeneralConfigExternalConfig extends ConfigPart {
    @Getter
    private final String language;
    
    public GeneralConfig() {
        super("general.yml", FileSearchLocations.BOTH, true); // rootConfig will be in general.yml. It'll search for that file in both the plugin and the data folder, and if it doesn't find it in the data folder, it'll create it with the default values inside of the plugin resources.
        language = getString(rootConfig, "language", "en"); // 3rd parameter is default value. Refeer to the ConfigPart.java file.
    }
}
```

Next, make a class that extends Configurator as follows (basically init all your ConfigParts in initConfigParts and put getters for them)
```java
public class MyPluginConfig extends Configurator {
    @Getter
    private static GeneralConfig general;

    @Override
    protected void initConfigParts() {
        general = new GeneralConfig();
    }
}
```

Inside of your onEnable, add the following:
```java
    public void onEnable() {
        new MyPluginConfig().init(this);
    }
```

And you're done !

Now you can use your config as follows:
MyPluginConfig.getGeneral().getLanguage()
