package cc.carm.lib.mineconfiguration.bungee.source;

import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

import static cc.carm.lib.mineconfiguration.bungee.source.BungeeConfigProvider.SEPARATOR;

public class BungeeSectionWrapper implements ConfigurationWrapper {

    private final Configuration section;

    private BungeeSectionWrapper(@NotNull Configuration section) {
        this.section = section;
    }

    @Contract("!null->!null")
    public static @Nullable BungeeSectionWrapper of(@Nullable Configuration section) {
        return section == null ? null : new BungeeSectionWrapper(section);
    }

    protected static Set<String> getAllKeys(@NotNull Configuration config) {
        Set<String> keys = new LinkedHashSet<>();
        for (String key : config.getKeys()) {
            keys.add(key);
            Object value = config.get(key);
            if (value instanceof Configuration) {
                getAllKeys((Configuration) value).stream()
                        .map(subKey -> key + SEPARATOR + subKey).forEach(keys::add);
            }
        }
        return keys;
    }

    @Override
    public @NotNull Set<String> getKeys(boolean deep) {
        if (deep) {
            return new LinkedHashSet<>(getAllKeys(section));
        } else {
            return new LinkedHashSet<>(section.getKeys());
        }
    }

    @Override
    public @NotNull Map<String, Object> getValues(boolean deep) {
        return getKeys(deep).stream()
                .collect(Collectors.toMap(key -> key, section::get, (a, b) -> b, LinkedHashMap::new));
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
        return get(path) instanceof List<?>;
    }

    @Override
    public @Nullable List<?> getList(@NotNull String path) {
        return this.section.getList(path);
    }

    @Override
    public boolean isConfigurationSection(@NotNull String path) {
        return get(path) instanceof Configuration;
    }

    @Override
    public @Nullable ConfigurationWrapper getConfigurationSection(@NotNull String path) {
        return of(this.section.getSection(path));
    }
}
