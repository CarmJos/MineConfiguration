package cc.carm.lib.mineconfiguration.bungee.value;

import cc.carm.lib.configuration.core.value.ValueManifest;
import cc.carm.lib.mineconfiguration.bungee.BungeeConfigValue;
import cc.carm.lib.mineconfiguration.bungee.builder.message.BungeeMessageListBuilder;
import cc.carm.lib.mineconfiguration.bungee.data.TextConfig;
import cc.carm.lib.mineconfiguration.common.value.ConfigMessageList;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class ConfiguredMessageList<M> extends ConfigMessageList<M, TextConfig, CommandSender> {

    @NotNull
    public static <M> BungeeMessageListBuilder<M> create(@NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable M> messageParser) {
        return BungeeConfigValue.builder().createMessage().asList(messageParser);
    }

    public static BungeeMessageListBuilder<String> asStrings() {
        return BungeeConfigValue.builder().createMessage().asStringList();
    }

    public static ConfiguredMessageList<String> ofStrings(@NotNull String... defaultMessages) {
        return asStrings().defaults(defaultMessages).build();
    }

    public ConfiguredMessageList(@NotNull ValueManifest<List<TextConfig>> manifest, @NotNull String[] params,
                                 @NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable M> messageParser,
                                 @NotNull BiConsumer<@NotNull CommandSender, @NotNull List<M>> sendFunction) {
        super(manifest, TextConfig.class, params, messageParser, sendFunction, TextConfig::of);
    }

    @Override
    public @NotNull Collection<CommandSender> getAllReceivers() {
        List<CommandSender> senders = new ArrayList<>();
        senders.add(ProxyServer.getInstance().getConsole());
        senders.addAll(ProxyServer.getInstance().getPlayers());
        return senders;
    }
}
