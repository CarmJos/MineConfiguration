package cc.carm.lib.configuration.craft.data;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class MessageText {

    @Contract("!null,-> !null")
    public static @Nullable MessageText of(@Nullable String message) {
        if (message == null) return null;
        else return new MessageText(message);
    }

    public static @NotNull List<MessageText> of(@NotNull List<String> messages) {
        return messages.stream().map(MessageText::of).collect(Collectors.toList());
    }

    public static @NotNull List<MessageText> of(@NotNull String... messages) {
        return Arrays.stream(messages).map(MessageText::of).collect(Collectors.toList());
    }

    protected @NotNull String message;

    public MessageText(@NotNull String message) {
        this.message = message;
    }

    public @NotNull String getMessage() {
        return this.message;
    }

    public <M> @Nullable M parse(@NotNull BiFunction<@Nullable CommandSender, @NotNull String, @NotNull M> parser,
                                 @Nullable CommandSender sender, @Nullable String[] params, @Nullable Object[] values) {
        return parse(parser, sender, buildParams(params, values));
    }

    public <M> @Nullable M parse(@NotNull BiFunction<@Nullable CommandSender, @NotNull String, @NotNull M> parser,
                                 @Nullable CommandSender sender, @NotNull Map<String, Object> placeholders) {
        String message = getMessage();
        if (message.isEmpty()) return null; // No further processing
        else return parser.apply(sender, setPlaceholders(message, placeholders));
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
