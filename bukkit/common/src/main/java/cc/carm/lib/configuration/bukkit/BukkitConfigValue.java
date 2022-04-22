package cc.carm.lib.configuration.bukkit;

import cc.carm.lib.configuration.bukkit.builder.BukkitConfigBuilder;
import cc.carm.lib.configuration.bukkit.source.BukkitConfigProvider;
import cc.carm.lib.configuration.bukkit.source.BukkitSectionWrapper;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.value.impl.CachedConfigValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BukkitConfigValue<T> extends CachedConfigValue<T> {

    public static @NotNull BukkitConfigBuilder builder() {
        return new BukkitConfigBuilder();
    }


    public BukkitConfigValue(@Nullable BukkitConfigProvider provider,
                             @Nullable String configPath, @NotNull String[] comments, @Nullable T defaultValue) {
        super(provider, configPath, comments, defaultValue);
    }

    public BukkitConfigProvider<?, ?> getBukkitProvider() {
        ConfigurationProvider<?> provider = getProvider();
        if (provider instanceof BukkitConfigProvider) return (BukkitConfigProvider<?, ?>) getProvider();
        else throw new IllegalStateException("Provider is not a SpigotConfigProvider");
    }

    public BukkitSectionWrapper getBukkitConfig() {
        return getBukkitProvider().getConfiguration();
    }


}
