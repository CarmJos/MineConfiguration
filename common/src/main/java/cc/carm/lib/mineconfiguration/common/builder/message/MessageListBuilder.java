package cc.carm.lib.mineconfiguration.common.builder.message;


import cc.carm.lib.configuration.core.builder.CommonConfigBuilder;
import cc.carm.lib.configuration.core.value.ValueManifest;
import cc.carm.lib.mineconfiguration.common.data.AbstractText;
import cc.carm.lib.mineconfiguration.common.utils.ParamsUtils;
import cc.carm.lib.mineconfiguration.common.value.ConfigMessageList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class MessageListBuilder<M, R, T extends AbstractText<R>, B extends MessageListBuilder<M, R, T, B>>
        extends CommonConfigBuilder<List<T>, B> {

    protected final @NotNull Class<R> receiverClazz;

    protected @NotNull String[] params;
    protected @NotNull BiFunction<@Nullable R, @NotNull String, @Nullable M> messageParser;
    protected @NotNull BiConsumer<@NotNull R, @NotNull List<M>> sendFunction;

    protected @NotNull Function<@NotNull String, @NotNull String> paramFormatter;
    protected final @NotNull Function<String, T> textBuilder;

    public MessageListBuilder(@NotNull Class<R> receiverClazz,
                              @NotNull Function<String, T> textBuilder,
                              @NotNull BiFunction<@Nullable R, @NotNull String, @Nullable M> parser) {
        this.receiverClazz = receiverClazz;
        this.textBuilder = textBuilder;
        this.params = new String[0];
        this.messageParser = parser;
        this.paramFormatter = ParamsUtils.DEFAULT_PARAM_FORMATTER;
        this.sendFunction = (sender, M) -> {
        };
    }

    public B defaults(@NotNull String... messages) {
        return defaults(new ArrayList<>(Arrays.stream(messages).map(textBuilder).collect(Collectors.toList())));
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

    public B whenSend(@NotNull BiConsumer<@NotNull R, @NotNull List<M>> sendFunction) {
        this.sendFunction = sendFunction;
        return getThis();
    }

    @Override
    public abstract @NotNull ConfigMessageList<M, T, R> build();

    protected @NotNull ValueManifest<List<T>> buildManifest(@NotNull List<T> emptyValue) {
        return new ValueManifest<>(
                this.provider, this.path, this.headerComments, this.inlineComment,
                Optional.ofNullable(this.defaultValue).orElse(emptyValue)
        );
    }

}
