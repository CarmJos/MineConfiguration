package cc.carm.lib.configuration.craft.builder.message;

import cc.carm.lib.configuration.craft.utils.ColorParser;
import cc.carm.lib.configuration.craft.value.ConfiguredMessage;
import cc.carm.lib.configuration.craft.value.ConfiguredMessageList;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public class MessageConfigBuilder {

    public <M> @NotNull MessageValueBuilder<M> asValue(@NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable M> messageParser) {
        return new MessageValueBuilder<>(messageParser);
    }

    public <M> @NotNull MessageListBuilder<M> asList(@NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable M> messageParser) {
        return new MessageListBuilder<>(messageParser);
    }

    public @NotNull MessageValueBuilder<String> asStringValue() {
        return asValue((sender, message) -> ColorParser.parseColor(message))
                .whenSend(CommandSender::sendMessage);
    }

    public @NotNull ConfiguredMessage<String> valueOfString() {
        return valueOfString("");
    }

    public @NotNull ConfiguredMessage<String> valueOfString(@NotNull String defaultMessage) {
        return asStringValue().content(defaultMessage).build();
    }


    public @NotNull MessageListBuilder<String> asStringList() {
        return asList((sender, message) -> ColorParser.parseColor(message))
                .whenSend((sender, messages) -> messages.forEach(sender::sendMessage));
    }

    public @NotNull ConfiguredMessageList<String> listOfString(@NotNull String... defaultMessages) {
        return asStringList().contents(defaultMessages).build();
    }

}
