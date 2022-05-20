package cc.carm.lib.configuration.minecraft.data;

import cc.carm.lib.configuration.minecraft.utils.ParamsUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        return parse(parser, receiver, ParamsUtils.buildParams(params, values));
    }

    public <M> @Nullable M parse(@NotNull BiFunction<@Nullable R, @NotNull String, @NotNull M> parser,
                                 @Nullable R receiver, @NotNull Map<String, Object> placeholders) {
        String message = getMessage();
        if (message.isEmpty()) return null; // No further processing
        else return parser.apply(receiver, ParamsUtils.setPlaceholders(message, placeholders));
    }


}
