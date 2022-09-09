package cc.carm.lib.mineconfiguration.bukkit.source;

import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class CraftSectionWrapper implements ConfigurationWrapper<ConfigurationSection> {

    protected final ConfigurationSection configuration;

    protected CraftSectionWrapper(ConfigurationSection configuration) {
        this.configuration = configuration;
    }

    @Override
    public @NotNull ConfigurationSection getSource() {
        return this.configuration;
    }

    @Override
    public @NotNull Set<String> getKeys(boolean deep) {
        return this.configuration.getKeys(deep);
    }

    @Override
    public @NotNull Map<String, Object> getValues(boolean deep) {
        return this.configuration.getValues(deep);
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {
        this.configuration.set(path, value);
    }

    @Override
    public boolean contains(@NotNull String path) {
        return this.configuration.contains(path);
    }

    @Override
    public @Nullable Object get(@NotNull String path) {
        return this.configuration.get(path);
    }

    @Override
    public boolean isList(@NotNull String path) {
        return this.configuration.isList(path);
    }

    @Override
    public @Nullable List<?> getList(@NotNull String path) {
        return this.configuration.getList(path);
    }

    @Override
    public boolean isConfigurationSection(@NotNull String path) {
        return this.configuration.isConfigurationSection(path);
    }

    @Override
    public @Nullable CraftSectionWrapper getConfigurationSection(@NotNull String path) {
        return Optional.ofNullable(configuration.getConfigurationSection(path))
                .map(CraftSectionWrapper::of).orElse(null);
    }

    public static CraftSectionWrapper of(ConfigurationSection section) {
        return new CraftSectionWrapper(section);
    }
}
