package cc.carm.lib.configuration.bukkit.value;

import cc.carm.lib.configuration.bukkit.data.MessageConfig;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.value.impl.CachedConfigValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfiguredMessage extends CachedConfigValue<MessageConfig> {


    public ConfiguredMessage(@Nullable ConfigurationProvider<?> provider,
                             @Nullable String sectionPath, @NotNull String[] comments,
                             @Nullable MessageConfig defaultValue) {
        super(provider, sectionPath, comments, defaultValue);
    }

    @Override
    public @Nullable MessageConfig get() {
        return null;
    }

    @Override
    public void set(@Nullable MessageConfig value) {

    }
}
