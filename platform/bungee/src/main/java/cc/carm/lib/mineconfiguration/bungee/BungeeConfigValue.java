package cc.carm.lib.mineconfiguration.bungee;

import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.value.ValueManifest;
import cc.carm.lib.configuration.core.value.impl.CachedConfigValue;
import cc.carm.lib.mineconfiguration.bungee.builder.BungeeConfigBuilder;
import cc.carm.lib.mineconfiguration.bungee.source.BungeeConfigProvider;
import cc.carm.lib.mineconfiguration.bungee.source.BungeeSectionWrapper;
import org.jetbrains.annotations.NotNull;

public abstract class BungeeConfigValue<T> extends CachedConfigValue<T> {

    public static @NotNull BungeeConfigBuilder builder() {
        return new BungeeConfigBuilder();
    }

    public BungeeConfigValue(@NotNull ValueManifest<T> manifest) {
        super(manifest);
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
