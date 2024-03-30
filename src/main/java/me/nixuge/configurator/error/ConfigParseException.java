package me.nixuge.configurator.error;

public class ConfigParseException extends RuntimeException {
    public ConfigParseException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public ConfigParseException(String string) {
        super(string);
    }
}
