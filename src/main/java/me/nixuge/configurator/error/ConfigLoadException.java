package me.nixuge.configurator.error;

public class ConfigLoadException extends RuntimeException {
    public ConfigLoadException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public ConfigLoadException(String string) {
        super(string);
    }
}
