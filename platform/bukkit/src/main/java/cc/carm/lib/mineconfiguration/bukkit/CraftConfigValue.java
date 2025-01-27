package cc.carm.lib.mineconfiguration.bukkit;

import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.value.ValueManifest;
import cc.carm.lib.configuration.core.value.impl.CachedConfigValue;
import cc.carm.lib.mineconfiguration.bukkit.source.CraftConfigProvider;
import cc.carm.lib.mineconfiguration.bukkit.source.CraftSectionWrapper;
import org.jetbrains.annotations.NotNull;

public abstract class CraftConfigValue<T> extends CachedConfigValue<T> {
    
    protected CraftConfigValue(@NotNull ValueManifest<T> manifest) {
        super(manifest);
    }

    public CraftConfigProvider getBukkitProvider() {
        ConfigurationProvider<?> provider = getProvider();
        if (provider instanceof CraftConfigProvider) return (CraftConfigProvider) getProvider();
        else throw new IllegalStateException("Provider is not a CraftConfigProvider");
    }

    public CraftSectionWrapper getBukkitConfig() {
        return getBukkitProvider().getConfiguration();
    }

}
