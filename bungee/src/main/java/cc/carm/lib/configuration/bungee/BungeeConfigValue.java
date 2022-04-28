package cc.carm.lib.configuration.bungee;

import cc.carm.lib.configuration.bungee.builder.BungeeConfigBuilder;
import cc.carm.lib.configuration.bungee.source.BungeeConfigProvider;
import cc.carm.lib.configuration.bungee.source.BungeeSectionWrapper;
import cc.carm.lib.configuration.core.source.ConfigCommentInfo;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.value.impl.CachedConfigValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BungeeConfigValue<T> extends CachedConfigValue<T> {

    public static @NotNull BungeeConfigBuilder builder() {
        return new BungeeConfigBuilder();
    }

    public BungeeConfigValue(@Nullable BungeeConfigProvider provider,
                             @Nullable String configPath, @Nullable ConfigCommentInfo comments, @Nullable T defaultValue) {
        super(provider, configPath, comments, defaultValue);
    }

    public BungeeConfigProvider getBukkitProvider() {
        ConfigurationProvider<?> provider = getProvider();
        if (provider instanceof BungeeConfigProvider) return (BungeeConfigProvider) getProvider();
        else throw new IllegalStateException("Provider is not a SpigotConfigProvider");
    }

    public BungeeSectionWrapper getBukkitConfig() {
        return getBukkitProvider().getConfiguration();
    }

}
