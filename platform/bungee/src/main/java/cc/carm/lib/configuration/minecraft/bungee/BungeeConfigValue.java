package cc.carm.lib.configuration.minecraft.bungee;

import cc.carm.lib.configuration.minecraft.bungee.builder.BungeeConfigBuilder;
import cc.carm.lib.configuration.minecraft.bungee.source.BungeeConfigProvider;
import cc.carm.lib.configuration.minecraft.bungee.source.BungeeSectionWrapper;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.value.impl.CachedConfigValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BungeeConfigValue<T> extends CachedConfigValue<T> {

    public static @NotNull BungeeConfigBuilder builder() {
        return new BungeeConfigBuilder();
    }

    public BungeeConfigValue(@Nullable BungeeConfigProvider provider, @Nullable String configPath,
                             @Nullable List<String> headerComments, @Nullable String inlineComments,
                             @Nullable T defaultValue) {
        super(provider, configPath, headerComments, inlineComments, defaultValue);
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
