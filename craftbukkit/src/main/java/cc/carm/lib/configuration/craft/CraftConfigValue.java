package cc.carm.lib.configuration.craft;

import cc.carm.lib.configuration.craft.builder.CraftConfigBuilder;
import cc.carm.lib.configuration.craft.source.CraftConfigProvider;
import cc.carm.lib.configuration.craft.source.CraftSectionWrapper;
import cc.carm.lib.configuration.core.source.ConfigCommentInfo;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.value.impl.CachedConfigValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CraftConfigValue<T> extends CachedConfigValue<T> {

    public static @NotNull CraftConfigBuilder builder() {
        return new CraftConfigBuilder();
    }

    public CraftConfigValue(@Nullable CraftConfigProvider provider,
                            @Nullable String configPath, @Nullable ConfigCommentInfo comments, @Nullable T defaultValue) {
        super(provider, configPath, comments, defaultValue);
    }

    public CraftConfigProvider getBukkitProvider() {
        ConfigurationProvider<?> provider = getProvider();
        if (provider instanceof CraftConfigProvider) return (CraftConfigProvider) getProvider();
        else throw new IllegalStateException("Provider is not a SpigotConfigProvider");
    }

    public CraftSectionWrapper getBukkitConfig() {
        return getBukkitProvider().getConfiguration();
    }

}
