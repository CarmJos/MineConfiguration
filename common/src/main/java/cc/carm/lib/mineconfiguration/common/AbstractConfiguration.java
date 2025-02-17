package cc.carm.lib.mineconfiguration.common;

import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.configuration.source.ConfigurationHolder;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractConfiguration<HOLDER extends ConfigurationHolder<?>> {

    private final HOLDER config;
    private final HOLDER message;

    protected AbstractConfiguration(@NotNull HOLDER config, @NotNull HOLDER message) {
        this.config = config;
        this.message = message;
    }

    public void initializeConfig(@NotNull Configuration configRoot) {
        this.config.initialize(configRoot);
    }

    public void initializeMessage(@NotNull Configuration messageRoot) {
        this.message.initialize(messageRoot);
    }

    public void initializeConfig(@NotNull Class<? extends Configuration> configRoot) {
        this.config.initialize(configRoot);
    }

    public void initializeMessage(@NotNull Class<? extends Configuration> messageRoot) {
        this.message.initialize(messageRoot);
    }

    public HOLDER getConfig() {
        return config;
    }

    public HOLDER getMessage() {
        return message;
    }

    public void reload() throws Exception {
        getConfig().reload();
        getMessage().reload();
    }

    public void save() throws Exception {
        getConfig().save();
        getMessage().save();
    }

}
