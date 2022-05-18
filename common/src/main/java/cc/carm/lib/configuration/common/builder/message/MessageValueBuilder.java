package cc.carm.lib.configuration.common.builder.message;

import cc.carm.lib.configuration.common.data.AbstractText;
import cc.carm.lib.configuration.common.utils.ParamsUtils;
import cc.carm.lib.configuration.common.value.ConfigMessage;
import cc.carm.lib.configuration.core.builder.CommonConfigBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class MessageValueBuilder<M, R, T extends AbstractText<R>, B extends MessageValueBuilder<M, R, T, B>>
        extends CommonConfigBuilder<T, B> {


    protected final @NotNull Class<R> receiverClazz;
    protected @NotNull String[] params;
    protected @NotNull BiFunction<@Nullable R, @NotNull String, @Nullable M> messageParser;
    protected @NotNull BiConsumer<@NotNull R, @NotNull M> sendHandler;

    protected @NotNull Function<@NotNull String, @NotNull String> paramFormatter;

    protected final @NotNull Function<String, T> textBuilder;

    public MessageValueBuilder(@NotNull Class<R> receiverClazz,
                               @NotNull Function<String, T> textBuilder,
                               @NotNull BiFunction<@Nullable R, @NotNull String, @Nullable M> parser) {
        this.receiverClazz = receiverClazz;
        this.params = new String[0];
        this.paramFormatter = ParamsUtils.DEFAULT_PARAM_FORMATTER;
        this.textBuilder = textBuilder;
        this.messageParser = parser;
        this.sendHandler = (receiver, M) -> {
        };
    }

    public B defaults(@NotNull String message) {
        return defaults(this.textBuilder.apply(message));
    }

    public B params(@NotNull String... params) {
        this.params = params;
        return getThis();
    }

    public B params(@NotNull List<String> params) {
        this.params = params.toArray(new String[0]);
        return getThis();
    }

    public B formatParam(@NotNull Function<@NotNull String, @NotNull String> paramFormatter) {
        this.paramFormatter = paramFormatter;
        return getThis();
    }

    public B whenSend(@NotNull BiConsumer<@NotNull R, @NotNull M> sendFunction) {
        this.sendHandler = sendFunction;
        return getThis();
    }

    @Override
    public abstract @NotNull ConfigMessage<M, T, R> build();

}
