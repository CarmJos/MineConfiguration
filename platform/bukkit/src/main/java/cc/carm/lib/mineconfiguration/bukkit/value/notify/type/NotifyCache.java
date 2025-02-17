package cc.carm.lib.mineconfiguration.bukkit.value.notify.type;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotifyCache<T extends NotifyType<M>, M> {

    // Notify config format: [TYPE(@PARAM)] CONTENTS...
    public static final @NotNull Pattern CONFIG_FORMAT = Pattern.compile("\\[(?<type>[^@\\]]+)(@(?<param>[^]]+))?] (?<content>.*)");

    public static @Nullable NotifyCache<?, ?> deserialize(@NotNull String config) {
        // parse config with config_format
        Matcher matcher = CONFIG_FORMAT.matcher(config.trim());
        if (!matcher.matches()) return of("MESSAGE", null, config);
        return of(matcher.group("type"), matcher.group("param"), matcher.group("content"));
    }

    public static @Nullable NotifyCache<?, ?> of(@NotNull String key, @Nullable String param, @Nullable String content) {
        NotifyType<?> type = NotifyType.get(key);
        if (type == null) return null;
        return NotifyCache.of(type, param, content);
    }

    public static <T extends NotifyType<M>, M> NotifyCache<T, M> of(@NotNull T type, @Nullable String param, @Nullable String content) {
        return new NotifyCache<>(type, type.parseMeta(param, content));
    }

    public static <T extends NotifyType<M>, M> NotifyCache<T, M> of(@NotNull T type, @Nullable M meta) {
        return new NotifyCache<>(type, meta);
    }

    protected final @NotNull T type;
    protected final @Nullable M meta;

    public NotifyCache(@NotNull T type, @Nullable M meta) {
        this.type = type;
        this.meta = meta;
    }

    public @NotNull T getType() {
        return type;
    }

    public @Nullable M getMeta() {
        return meta;
    }

    public void execute(@NotNull Player player, @NotNull Map<String, Object> placeholders) {
        type.execute(player, meta, placeholders);
    }

    public String serialize() {
        return type.serializeConfig(meta);
    }


}
