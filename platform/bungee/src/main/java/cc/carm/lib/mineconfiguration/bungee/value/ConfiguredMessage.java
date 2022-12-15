package cc.carm.lib.mineconfiguration.bungee.value;

import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.mineconfiguration.bungee.BungeeConfigValue;
import cc.carm.lib.mineconfiguration.bungee.builder.message.BungeeMessageValueBuilder;
import cc.carm.lib.mineconfiguration.bungee.data.MessageText;
import cc.carm.lib.mineconfiguration.common.value.ConfigMessage;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class ConfiguredMessage<M> extends ConfigMessage<M, MessageText, CommandSender> {

    @NotNull
    public static <M> BungeeMessageValueBuilder<@Nullable M> create(@NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable M> messageParser) {
        return BungeeConfigValue.builder().createMessage().asValue(messageParser);
    }

    public static BungeeMessageValueBuilder<String> asString() {
        return BungeeConfigValue.builder().createMessage().asStringValue();
    }

    public static ConfiguredMessage<String> ofString() {
        return asString().build();
    }

    public static ConfiguredMessage<String> ofString(@NotNull String defaultMessage) {
        return asString().defaults(defaultMessage).build();
    }

    public ConfiguredMessage(@Nullable ConfigurationProvider<?> provider, @Nullable String sectionPath,
                             @Nullable List<String> headerComments, @Nullable String inlineComments,
                             @NotNull MessageText defaultMessage, @NotNull String[] params,
                             @NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable M> messageParser,
                             @NotNull BiConsumer<@NotNull CommandSender, @NotNull M> sendFunction) {
        super(provider, sectionPath, headerComments, inlineComments, MessageText.class, defaultMessage, params, messageParser, sendFunction, MessageText::of);
    }

    @Override
    public @NotNull Collection<CommandSender> getAllReceivers() {
        List<CommandSender> senders = new ArrayList<>();
        senders.add(ProxyServer.getInstance().getConsole());
        senders.addAll(ProxyServer.getInstance().getPlayers());
        return senders;
    }


}
