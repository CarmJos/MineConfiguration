package cc.carm.lib.mineconfiguration.bukkit.data;

import cc.carm.lib.mineconfiguration.bukkit.value.notify.ConfiguredNotify;
import cc.carm.lib.mineconfiguration.bukkit.value.notify.NotifyType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.regex.Matcher;

public class NotifyConfig<T extends NotifyType<M>, M> {

    public static @Nullable NotifyConfig<?, ?> deserialize(@NotNull String config) {
        // parse config with config_format
        Matcher matcher = NotifyType.CONFIG_FORMAT.matcher(config.trim());
        if (!matcher.matches()) return of("MESSAGE", null, config);
        else return of(matcher.group("type"), matcher.group("param"), matcher.group("content"));
    }

    public static @Nullable NotifyConfig<?, ?> of(@NotNull String key, @Nullable String param, @Nullable String content) {
        NotifyType<?> type = ConfiguredNotify.getType(key);
        if (type == null) return null;
        return NotifyConfig.of(type, param, content);
    }

    public static <T extends NotifyType<M>, M> NotifyConfig<T, M> of(@NotNull T type, @Nullable String param, @Nullable String content) {
        return new NotifyConfig<>(type, type.parseMeta(param, content));
    }

    public static <T extends NotifyType<M>, M> NotifyConfig<T, M> of(@NotNull T type, @Nullable M meta) {
        return new NotifyConfig<>(type, meta);
    }

    protected final @NotNull T type;
    protected final @Nullable M meta;

    public NotifyConfig(@NotNull T type, @Nullable M meta) {
        this.type = type;
        this.meta = meta;
    }

    public void execute(@NotNull Player player, @NotNull Map<String, Object> placeholders) {
        type.execute(player, meta, placeholders);
    }

    public String serialize() {
        return type.serializeConfig(meta);
    }

}
