package cc.carm.lib.configuration.minecraft.common.builder.message;

import cc.carm.lib.configuration.minecraft.common.data.AbstractText;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public abstract class MessageConfigBuilder<R, T extends AbstractText<R>> {

    protected final @NotNull Class<R> receiverClazz;
    protected final @NotNull Class<T> textClazz;

    public MessageConfigBuilder(@NotNull Class<R> receiverClazz,
                                @NotNull Class<T> textClazz) {
        this.receiverClazz = receiverClazz;
        this.textClazz = textClazz;
    }

    /**
     * 以单条消息为目标，构建一个消息配置。
     *
     * @param parser 消息解析器，负责将String转换为目标消息类型。
     * @param <M>    消息类型
     * @return 单条消息构建器
     */
    public abstract <M> @NotNull MessageValueBuilder<M, R, T, ?> asValue(@NotNull BiFunction<@Nullable R, @NotNull String, @Nullable M> parser);

    /**
     * 以多行消息为目标，构建一个消息配置。
     *
     * @param parser 消息解析器
     * @param <M>    消息类型
     * @return 多行消息构建器
     */
    public abstract <M> @NotNull MessageListBuilder<M, R, T, ?> asList(@NotNull BiFunction<@Nullable R, @NotNull String, @Nullable M> parser);


}
