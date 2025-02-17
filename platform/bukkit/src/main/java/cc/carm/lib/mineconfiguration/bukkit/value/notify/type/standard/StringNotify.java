package cc.carm.lib.mineconfiguration.bukkit.value.notify.type.standard;

import cc.carm.lib.mineconfiguration.bukkit.value.notify.type.NotifyType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.UnaryOperator;

public abstract class StringNotify extends NotifyType<String> {

    public static StringNotify of(String key, BiConsumer<Player, String> consumer) {
        return of(key, consumer, (content) -> "[" + key + "] " + (content == null ? " " : content));
    }

    public static StringNotify of(String key,
                                  BiConsumer<Player, String> consumer,
                                  UnaryOperator<String> serializer) {
        return new StringNotify(key) {
            @Override
            public @NotNull String serializeConfig(@Nullable String meta) {
                return serializer.apply(meta);
            }

            @Override
            public void send(@NotNull Player player, @NotNull String parsedContent) {
                consumer.accept(player, parsedContent);
            }
        };
    }


    protected StringNotify(String key) {
        super(key, String.class);
    }

    public abstract void send(@NotNull Player player, @NotNull String parsedContent);

    @Override
    public @Nullable String parseMeta(@Nullable String param, @Nullable String content) {
        return content;
    }

    @Override
    public void execute(@NotNull Player player, @Nullable String content, @NotNull Map<String, Object> placeholders) {
        if (content == null) return;
        send(player, setPlaceholders(player, placeholders, content));
    }

}
