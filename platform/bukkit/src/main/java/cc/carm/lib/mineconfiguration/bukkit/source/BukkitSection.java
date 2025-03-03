package cc.carm.lib.mineconfiguration.bukkit.source;

import cc.carm.lib.configuration.source.section.ConfigureSection;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;

public class BukkitSection implements ConfigureSection {

    protected final @NotNull BukkitSource source;
    protected final @Nullable BukkitSection parent;
    protected final @NotNull String path;
    protected final @NotNull ConfigurationSection data;

    public BukkitSection(@NotNull BukkitSource source, @Nullable BukkitSection parent,
                         @NotNull String path, @NotNull ConfigurationSection data) {
        this.source = source;
        this.parent = parent;
        this.path = path;
        this.data = data;
    }

    public @NotNull BukkitSource source() {
        return this.source;
    }

    @Override
    public @Nullable BukkitSection parent() {
        return this.parent;
    }

    @Override
    public @NotNull String path() {
        return this.path;
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

        Map<String, Object> original = data().getValues(deep);
        // wrap all ConfigurationSection
        for (Map.Entry<String, Object> entry : original.entrySet()) {
            if (entry.getValue() instanceof ConfigurationSection) {
                original.put(entry.getKey(), createSection(entry.getKey(), (ConfigurationSection) entry.getValue()));
            }
        }

        return Collections.unmodifiableMap(original);
    }

    public Map<String, Object> toMap(ConfigurationSection section) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (String key : section.getKeys(false)) {
            Object value = section.get(key);
            if (value instanceof ConfigurationSection) {
                map.put(key, toMap((ConfigurationSection) value));
            } else if (value instanceof BukkitSection) {
                map.put(key, toMap(((BukkitSection) value).data()));
            } else if (value instanceof List<?>) {
                List<Object> list = new ArrayList<>();
                for (Object o : (List<?>) value) {
                    if (o instanceof ConfigurationSection) {
                        list.add(toMap((ConfigurationSection) o));
                    } else if (o instanceof BukkitSection) {
                        list.add(toMap(((BukkitSection) o).data()));
                    } else {
                        list.add(o);
                    }
                }
                map.put(key, list);
            } else {
                map.put(key, value);
            }
        }
        return map;
    }

    @Override
    public @NotNull @UnmodifiableView Map<String, Object> asMap() {
        return toMap(data());
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {
        if (value instanceof BukkitSection) { // unwrap
            value = ((BukkitSection) value).data();
        }
        data().set(path, value);
    }

    @Override
    public void remove(@NotNull String path) {
        data().set(path, null);
    }

    @Override
    public @Nullable ConfigureSection getSection(@NotNull String path) {
        Object value = get(path);
        if (value instanceof ConfigureSection) {
            return (ConfigureSection) value;
        }
        return null;
    }

    public @NotNull BukkitSection createSection(@NotNull String path, @NotNull ConfigurationSection section) {
        return new BukkitSection(source(), this, path, section);
    }

    @Override
    public @NotNull BukkitSection createSection(@NotNull String path, @NotNull Map<?, ?> data) {
        return createSection(path, data().createSection(path, data));
    }

    @Override
    public @Nullable Object get(@NotNull String path) {
        Object value = data().get(path);
        if (value instanceof ConfigurationSection) { // wrap
            return createSection(path, (ConfigurationSection) value);
        }
        return value;
    }

}
