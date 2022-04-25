package cc.carm.lib.configuration.craft.builder.message;

import cc.carm.lib.configuration.core.builder.CommonConfigBuilder;
import cc.carm.lib.configuration.craft.value.ConfiguredMessageList;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MessageListBuilder<M>
        extends CommonConfigBuilder<String, MessageListBuilder<M>> {

    protected @NotNull List<String> messages;
    protected @NotNull String[] params;
    protected @NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable M> messageParser;
    protected @NotNull BiConsumer<@NotNull CommandSender, @NotNull List<M>> sendFunction;

    protected @NotNull Function<@NotNull String, @NotNull String> paramFormatter;

    public MessageListBuilder(@NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable M> parser) {
        this.messages = new ArrayList<>();
        this.params = new String[0];
        this.messageParser = parser;
        this.paramFormatter = MessageValueBuilder.DEFAULT_PARAM_FORMATTER;
        this.sendFunction = (sender, M) -> {
        };
    }

    public MessageListBuilder<M> contents(@NotNull String... messages) {
        return contents(Arrays.asList(messages));
    }

    public MessageListBuilder<M> contents(@NotNull List<String> messages) {
        this.messages = new ArrayList<>(messages);
        return this;
    }

    public MessageListBuilder<M> params(@NotNull String... params) {
        this.params = params;
        return this;
    }

    public MessageListBuilder<M> params(@NotNull List<String> params) {
        this.params = params.toArray(new String[0]);
        return this;
    }

    public MessageListBuilder<M> formatParam(@NotNull Function<@NotNull String, @NotNull String> paramFormatter) {
        this.paramFormatter = paramFormatter;
        return this;
    }

    public MessageListBuilder<M> whenSend(@NotNull BiConsumer<@NotNull CommandSender, @NotNull List<M>> sendFunction) {
        this.sendFunction = sendFunction;
        return this;
    }

    @Override
    protected @NotNull MessageListBuilder<M> getThis() {
        return this;
    }

    @Override
    public @NotNull ConfiguredMessageList<M> build() {
        return new ConfiguredMessageList<>(
                this.provider, this.path, buildComments(),
                this.messages, buildParams(), this.messageParser, this.sendFunction
        );
    }

    protected final String[] buildParams() {
        return Arrays.stream(params).map(param -> paramFormatter.apply(param)).toArray(String[]::new);
    }

}
