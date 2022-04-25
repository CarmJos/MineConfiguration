package cc.carm.lib.configuration.craft.builder.message;

import cc.carm.lib.configuration.core.builder.CommonConfigBuilder;
import cc.carm.lib.configuration.craft.value.ConfiguredMessage;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MessageValueBuilder<M>
        extends CommonConfigBuilder<String, MessageValueBuilder<M>> {

    public static Function<@NotNull String, @NotNull String> DEFAULT_PARAM_FORMATTER = (s) -> "%(" + s + ")";

    protected @NotNull String message;
    protected @NotNull String[] params;
    protected @NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable M> messageParser;
    protected @NotNull BiConsumer<@NotNull CommandSender, @NotNull M> sendHandler;

    protected @NotNull Function<@NotNull String, @NotNull String> paramFormatter;

    public MessageValueBuilder(@NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable M> parser) {
        this.message = "";
        this.params = new String[0];
        this.paramFormatter = DEFAULT_PARAM_FORMATTER;
        this.messageParser = parser;
        this.sendHandler = (sender, M) -> {
        };
    }

    public MessageValueBuilder<M> content(@NotNull String message) {
        this.message = message;
        return this;
    }

    public MessageValueBuilder<M> params(@NotNull String... params) {
        this.params = params;
        return this;
    }

    public MessageValueBuilder<M> params(@NotNull List<String> params) {
        this.params = params.toArray(new String[0]);
        return this;
    }

    public MessageValueBuilder<M> formatParam(@NotNull Function<@NotNull String, @NotNull String> paramFormatter) {
        this.paramFormatter = paramFormatter;
        return this;
    }

    public MessageValueBuilder<M> whenSend(@NotNull BiConsumer<@NotNull CommandSender, @NotNull M> sendFunction) {
        this.sendHandler = sendFunction;
        return this;
    }

    @Override
    protected @NotNull MessageValueBuilder<M> getThis() {
        return this;
    }

    @Override
    public @NotNull ConfiguredMessage<M> build() {
        return new ConfiguredMessage<>(
                this.provider, this.path, buildComments(),
                this.message, buildParams(), this.messageParser, this.sendHandler
        );
    }


    protected final String[] buildParams() {
        return Arrays.stream(params).map(param -> paramFormatter.apply(param)).toArray(String[]::new);
    }

}
