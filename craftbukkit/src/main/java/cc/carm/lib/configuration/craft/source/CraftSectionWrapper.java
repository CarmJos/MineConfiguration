package cc.carm.lib.configuration.craft.source;

import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class CraftSectionWrapper implements ConfigurationWrapper {

    protected final ConfigurationSection section;

    protected CraftSectionWrapper(ConfigurationSection section) {
        this.section = section;
    }

    public ConfigurationSection getSourceSection() {
        return section;
    }

    @Override
    public @NotNull Set<String> getKeys(boolean deep) {
        return this.section.getKeys(deep);
    }

    @Override
    public @NotNull Map<String, Object> getValues(boolean deep) {
        return this.section.getValues(deep);
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {
        this.section.set(path, value);
    }

    @Override
    public boolean contains(@NotNull String path) {
        return this.section.contains(path);
    }

    @Override
    public @Nullable Object get(@NotNull String path) {
        return this.section.get(path);
    }

    @Override
    public boolean isList(@NotNull String path) {
        return this.section.isList(path);
    }

    @Override
    public @Nullable List<?> getList(@NotNull String path) {
        return this.section.getList(path);
    }

    @Override
    public boolean isConfigurationSection(@NotNull String path) {
        return this.section.isConfigurationSection(path);
    }

    @Override
    public @Nullable ConfigurationWrapper getConfigurationSection(@NotNull String path) {
        return Optional.ofNullable(section.getConfigurationSection(path))
                .map(CraftSectionWrapper::of).orElse(null);
    }

    public static CraftSectionWrapper of(ConfigurationSection section) {
        return new CraftSectionWrapper(section);
    }
}
