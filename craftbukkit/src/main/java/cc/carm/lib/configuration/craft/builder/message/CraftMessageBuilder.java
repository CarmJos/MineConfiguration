package cc.carm.lib.configuration.craft.builder.message;

import cc.carm.lib.configuration.common.builder.message.MessageConfigBuilder;
import cc.carm.lib.configuration.common.utils.ColorParser;
import cc.carm.lib.configuration.craft.data.MessageText;
import cc.carm.lib.configuration.craft.utils.PAPIHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public class CraftMessageBuilder extends MessageConfigBuilder<CommandSender, MessageText> {


    public CraftMessageBuilder() {
        super(CommandSender.class, MessageText.class);
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
        return (receiver, message) -> {
            if (receiver instanceof Player && hasPlaceholderAPI()) {
                message = PAPIHelper.parseMessages((Player) receiver, message);
            }
            return ColorParser.parse(message);
        };
    }

    public static boolean hasPlaceholderAPI() {
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

}
