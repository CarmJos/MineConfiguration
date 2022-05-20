package cc.carm.lib.configuration.bungee.builder.message;

import cc.carm.lib.configuration.bungee.data.MessageText;
import cc.carm.lib.configuration.minecraft.builder.message.MessageConfigBuilder;
import cc.carm.lib.configuration.minecraft.utils.ColorParser;
import net.md_5.bungee.api.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public class BungeeMessageBuilder extends MessageConfigBuilder<CommandSender, MessageText> {


    public BungeeMessageBuilder() {
        super(CommandSender.class, MessageText.class);
    }

    @Override
    public @NotNull <M> BungeeMessageValueBuilder<M> asValue(@NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable M> parser) {
        return new BungeeMessageValueBuilder<>(parser);
    }

    @Override
    public @NotNull <M> BungeeMessageListBuilder<M> asList(@NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable M> parser) {
        return new BungeeMessageListBuilder<>(parser);
    }

    public @NotNull
    BungeeMessageValueBuilder<String> asStringValue() {
        return asValue(defaultParser()).whenSend(CommandSender::sendMessage);
    }

    public @NotNull
    BungeeMessageListBuilder<String> asStringList() {
        return asList(defaultParser()).whenSend((r, m) -> m.forEach(r::sendMessage));
    }

    protected static @NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable String> defaultParser() {
        return (receiver, message) -> ColorParser.parse(message);
    }

}
