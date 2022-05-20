package cc.carm.lib.configuration.minecraft.bukkit.builder.message;

import cc.carm.lib.configuration.minecraft.bukkit.data.TextConfig;
import cc.carm.lib.configuration.minecraft.bukkit.utils.TextParser;
import cc.carm.lib.configuration.minecraft.common.builder.message.MessageConfigBuilder;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.function.BiFunction;

public class CraftMessageBuilder extends MessageConfigBuilder<CommandSender, TextConfig> {


    public CraftMessageBuilder() {
        super(CommandSender.class, TextConfig.class);
    }

    @Override
    public @NotNull <M> CraftMessageValueBuilder<M> asValue(@NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable M> parser) {
        return new CraftMessageValueBuilder<>(parser);
    }

    @Override
    public @NotNull <M> CraftMessageListBuilder<M> asList(@NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable M> parser) {
        return new CraftMessageListBuilder<>(parser);
    }

    public @NotNull
    CraftMessageValueBuilder<String> asStringValue() {
        return asValue(defaultParser()).whenSend(CommandSender::sendMessage);
    }

    public @NotNull
    CraftMessageListBuilder<String> asStringList() {
        return asList(defaultParser()).whenSend((r, m) -> m.forEach(r::sendMessage));
    }

    protected static @NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable String> defaultParser() {
        return (receiver, message) -> TextParser.parseText(receiver, message, new HashMap<>());
    }

}
