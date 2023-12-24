package cc.carm.lib.mineconfiguration.common;

import cc.carm.lib.configuration.core.Configuration;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractConfiguration<P extends ConfigurationProvider<?>> {

    private final P configProvider;
    private final P messageProvider;

    protected AbstractConfiguration(@NotNull P configProvider, @NotNull P messageProvider) {
        this.configProvider = configProvider;
        this.messageProvider = messageProvider;
    }

    public void initializeConfig(@NotNull Configuration configRoot) {
        this.configProvider.initialize(configRoot);
    }

    public void initializeMessage(@NotNull Configuration messageRoot) {
        this.messageProvider.initialize(messageRoot);
    }

    public void initializeConfig(@NotNull Class<? extends Configuration> configRoot) {
        this.configProvider.initialize(configRoot);
    }

    public void initializeMessage(@NotNull Class<? extends Configuration> messageRoot) {
        this.messageProvider.initialize(messageRoot);
    }

    public P getConfigProvider() {
        return configProvider;
    }

    public P getMessageProvider() {
        return messageProvider;
    }

    public void reload() throws Exception {
        getConfigProvider().reload();
        getMessageProvider().reload();
    }

    public void save() throws Exception {
        getConfigProvider().save();
        getMessageProvider().save();
    }

}
