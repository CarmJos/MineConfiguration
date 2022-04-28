package cc.carm.lib.configuration.common.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * @param <R> Receiver type
 */
public abstract class AbstractText<R> {

    private final @NotNull Class<R> receiverClazz;
    protected @NotNull String message;

    public AbstractText(@NotNull Class<R> receiverClazz, @NotNull String message) {
        this.receiverClazz = receiverClazz;
        this.message = message;
    }

    public @NotNull Class<R> getReceiverClazz() {
        return receiverClazz;
    }

    public @NotNull String getMessage() {
        return this.message;
    }

    public <M> @Nullable M parse(@NotNull BiFunction<@Nullable R, @NotNull String, @NotNull M> parser,
                                 @Nullable R receiver, @Nullable String[] params, @Nullable Object[] values) {
        return parse(parser, receiver, buildParams(params, values));
    }

    public <M> @Nullable M parse(@NotNull BiFunction<@Nullable R, @NotNull String, @NotNull M> parser,
                                 @Nullable R receiver, @NotNull Map<String, Object> placeholders) {
        String message = getMessage();
        if (message.isEmpty()) return null; // No further processing
        else return parser.apply(receiver, setPlaceholders(message, placeholders));
    }

    public static Map<String, Object> buildParams(@Nullable String[] params, @Nullable Object[] values) {
        Map<String, Object> map = new HashMap<>();
        if (params == null || params.length == 0) return map;
        for (int i = 0; i < params.length; i++) {
            map.put(params[i], values.length > i ? values[i] : "?");
        }
        return map;
    }

    public static String setPlaceholders(@NotNull String messages, @NotNull Map<String, Object> placeholders) {
        if (messages.isEmpty()) return messages;

        String parsed = messages;
        for (Map.Entry<String, Object> entry : placeholders.entrySet()) {
            Object value = entry.getValue();
            parsed = parsed.replace(entry.getKey(), value == null ? "" : value.toString());
        }

        return parsed;
    }


}
