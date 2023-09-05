package cc.carm.lib.mineconfiguration.common.value;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PreparedMessage<R, M> {

    protected final @NotNull BaseMessage<R, M> message;
    protected final @NotNull Object[] values;

    protected PreparedMessage(@NotNull BaseMessage<R, M> message, @NotNull Object[] values) {
        this.message = message;
        this.values = values;
    }

    /**
     * 为某位接收者解析此消息。
     *
     * @param receiver 接收者
     * @return 解析变量后的消息内容
     */
    public @Nullable M parse(@Nullable R receiver) {
        return message.parse(receiver, values);
    }

    /**
     * 向某位接收者发送消息
     *
     * @param receiver 消息的接收者
     */
    public void to(@Nullable R receiver) {
        message.send(receiver, values);
    }

    /**
     * 向某位接收者发送消息
     *
     * @param receivers 消息的接收者们
     */
    public void to(@NotNull Iterable<R> receivers) {
        receivers.forEach(this::to);
    }

    public void toAll() {
        to(message.getAllReceivers());
    }

}
