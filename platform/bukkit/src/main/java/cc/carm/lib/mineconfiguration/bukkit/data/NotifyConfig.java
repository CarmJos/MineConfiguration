package cc.carm.lib.mineconfiguration.bukkit.data;

import cc.carm.lib.mineconfiguration.bukkit.value.notify.type.NotifyCache;
import cc.carm.lib.mineconfiguration.bukkit.value.notify.type.NotifyType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public class NotifyConfig {

    public static @Nullable NotifyConfig deserialize(@NotNull String config) {
        return Optional.ofNullable(NotifyCache.deserialize(config)).map(NotifyConfig::of).orElse(null);
    }

    public static @NotNull NotifyConfig of(@NotNull NotifyCache<?, ?> cache) {
        return new NotifyConfig(cache);
    }

    public static @Nullable NotifyConfig of(@NotNull String key, @Nullable String param, @Nullable String content) {
        return Optional.ofNullable(NotifyCache.of(key, param, content)).map(NotifyConfig::of).orElse(null);
    }

    public static <T extends NotifyType<M>, M> NotifyConfig of(@NotNull T type, @Nullable String param, @Nullable String content) {
        return of(type, type.parseMeta(param, content));
    }

    public static <T extends NotifyType<M>, M> NotifyConfig of(@NotNull T type, @Nullable M meta) {
        return of(NotifyCache.of(type, meta));
    }

    protected final @NotNull NotifyCache<?, ?> cache;


    public NotifyConfig(@NotNull NotifyCache<?, ?> cache) {
        this.cache = cache;
    }

    public @NotNull NotifyCache<?, ?> getCache() {
        return cache;
    }

    public void execute(@NotNull Player player, @NotNull Map<String, Object> placeholders) {
        getCache().execute(player, placeholders);
    }

    public String serialize() {
        return getCache().serialize();
    }

}
