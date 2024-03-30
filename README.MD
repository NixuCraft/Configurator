# Configurator
A mid tier config lib for Minecraft plugins.

# Usage
- Make multiple classes that extend ConfigPart for config section you need.
  
  Those classes need to call super with a ConfigSourceType and a string. If the ConfigSourceType is MAIN_CONFIG, it'll get the rootConfig from the main config.yml, in which case the second argument will be the key inside of config.yml in which that part of your config is located. Otherwise, if it's OTHER_FILE, it's going to use the root of the file with the name given in the second arguments.

  Here's an example class:
```java
public class GeneralConfig extends ConfigPart {
    @Getter
    private final String language;
    
    public GeneralConfig() {
        super(ConfigSourceType.MAIN_CONFIG, "general"); // rootConfig will be in config.yml under the key "general" 
        language = getString(rootConfig, "language", "en"); // 3rd parameter is default value. Refeer to the ConfigPart.java file.
    }
}
```
- Next, make a class that extends Configurator as follows (basically init all your ConfigParts in initConfigParts and put getters for them)
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
- Inside of your onEnable, add the following:
```java
    public void onEnable() {
        new MyPluginConfig().init(this);
    }
```
And you're done !

Now you can use your config as follows:
MyPluginConfig.getGeneral().getLanguage()