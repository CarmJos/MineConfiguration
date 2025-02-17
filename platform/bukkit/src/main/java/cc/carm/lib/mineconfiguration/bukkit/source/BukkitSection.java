package cc.carm.lib.mineconfiguration.bukkit.source;

import cc.carm.lib.configuration.source.section.ConfigureSection;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class BukkitSection implements ConfigureSection {

    protected final @NotNull BukkitSource source;
    protected final @Nullable BukkitSection parent;
    protected final @NotNull ConfigurationSection data;

    public BukkitSection(@NotNull BukkitSource source, @Nullable BukkitSection parent,
                         @NotNull ConfigurationSection data) {
        this.source = source;
        this.parent = parent;
        this.data = data;
    }

    @Override
    public @NotNull BukkitSource source() {
        return this.source;
    }

    @Override
    public @Nullable BukkitSection parent() {
        return this.parent;
    }

    public @NotNull ConfigurationSection data() {
        return this.data;
    }

    @Override
    public @NotNull Set<String> getKeys(boolean deep) {
        return data().getKeys(deep);
    }

    @Override
    public @NotNull @UnmodifiableView Map<String, Object> getValues(boolean deep) {
        return data().getValues(deep);
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {
        data().set(path, value);
    }

    @Override
    public boolean contains(@NotNull String path) {
        return data().contains(path);
    }

    @Override
    public @Nullable List<?> getList(@NotNull String path) {
        return data().getList(path);
    }

    @Override
    public @Nullable ConfigureSection getSection(@NotNull String path) {
        Object value = get(path);
        if (value instanceof ConfigureSection) {
            return (ConfigureSection) value;
        }
        return null;
    }

    @Override
    public @Nullable Object get(@NotNull String path) {
        Object value = data().get(path);
        if (value instanceof ConfigurationSection) {
            return new BukkitSection(source(), this, (ConfigurationSection) value);
        }
        return value;
    }

}
