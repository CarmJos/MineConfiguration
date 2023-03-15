package cc.carm.lib.mineconfiguration.common.value;

import cc.carm.lib.mineconfiguration.common.builder.message.MessageValueBuilder;
import cc.carm.lib.mineconfiguration.common.utils.ParamsUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public interface BaseMessage<R, M> {

    /**
     * 得到所有的接收者
     *
     * @return 全部可能的接收者
     */
    @Unmodifiable
    @NotNull Iterable<R> getAllReceivers();

    /**
     * 得到消息中的通过 {@link MessageValueBuilder#params(String...)}已定变量名(按定义顺序)
     *
     * @return 已定变量
     */
    @NotNull String[] getParams();

    /**
     * 向接收者发送消息的根方法。
     *
     * @param receiver 接收者
     * @param message  消息内容
     */
    @ApiStatus.OverrideOnly
    void apply(@NotNull R receiver, @NotNull M message);

    /**
     * 为某位接收者解析此消息。
     *
     * @param receiver     接收者
     * @param placeholders 消息中的变量与对应参数
     * @return 解析变量后的消息内容
     */
    @Nullable M parse(@Nullable R receiver, @NotNull Map<String, Object> placeholders);

    /**
     * 为某位接收者解析此消息。
     *
     * @param receiver 接收者
     * @param values   已定变量的对应参数
     * @return 解析变量后的消息内容
     */
    default @Nullable M parse(@Nullable R receiver, @Nullable Object... values) {
        return parse(receiver, ParamsUtils.buildParams(getParams(), values));
    }

    /**
     * 向某位接收者发送消息
     *
     * @param receiver 消息的接收者
     * @param values   已定变量的对应参数
     */
    default void send(@Nullable R receiver, @Nullable Object... values) {
        send(receiver, ParamsUtils.buildParams(getParams(), values));
    }

    /**
     * 向某位接收者发送消息
     *
     * @param receiver     消息的接收者
     * @param placeholders 消息中的变量与对应参数
     */
    default void send(@Nullable R receiver, @NotNull Map<String, Object> placeholders) {
        if (receiver == null) return;
        M parsed = parse(receiver, placeholders);
        if (parsed == null) return;
        apply(receiver, parsed);
    }

    /**
     * 向全部接收者(包括后台)发送不同参数的消息
     *
     * @param eachValues 每位接收者将收到已定变量的对应参数(按定义顺序)
     */
    default void sendToEach(@NotNull Function<@NotNull R, Object[]> eachValues) {
        sendToEach(null, eachValues);
    }

    /**
     * 向特定接收者发送不同参数的消息
     *
     * @param limiter    接收者限定器，为空则不限定接收者。
     * @param eachValues 每位接收者将收到已定变量的对应参数(按定义顺序)
     */
    default void sendToEach(@Nullable Predicate<R> limiter,
                            @NotNull Function<@NotNull R, Object[]> eachValues) {
        Predicate<R> predicate = Optional.ofNullable(limiter).orElse(r -> true);
        for (R r : getAllReceivers()) {
            if (predicate.test(r)) {
                send(r, ParamsUtils.buildParams(getParams(), eachValues.apply(r)));
            }
        }
    }


    /**
     * 广播此消息(包括后台)
     *
     * @param values 已定变量的对应参数(按定义顺序)
     */
    default void sendToAll(@Nullable Object... values) {
        broadcast(values);
    }

    /**
     * 广播此消息(包括后台)
     *
     * @param placeholders 消息中的变量与对应参数
     */
    default void sendToAll(@NotNull Map<String, Object> placeholders) {
        broadcast(placeholders);
    }


    /**
     * 广播此消息(包括后台)
     *
     * @param values 已定变量的对应参数(按定义顺序)
     */
    default void broadcast(@Nullable Object... values) {
        broadcast(ParamsUtils.buildParams(getParams(), values));
    }

    /**
     * 广播此消息(包括后台)
     *
     * @param placeholders 消息中的变量与对应参数
     */
    default void broadcast(@NotNull Map<String, Object> placeholders) {
        getAllReceivers().forEach(r -> send(r, placeholders));
    }

}
