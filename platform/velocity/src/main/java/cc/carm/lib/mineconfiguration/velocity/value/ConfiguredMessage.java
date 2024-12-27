package cc.carm.lib.mineconfiguration.velocity.value;

import cc.carm.lib.configuration.core.value.ValueManifest;
import cc.carm.lib.mineconfiguration.velocity.BungeeConfigValue;
import cc.carm.lib.mineconfiguration.velocity.builder.message.BungeeMessageValueBuilder;
import cc.carm.lib.mineconfiguration.velocity.data.TextConfig;
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

public class ConfiguredMessage<M> extends ConfigMessage<M, TextConfig, CommandSender> {

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

    public ConfiguredMessage(@NotNull ValueManifest<TextConfig> manifest, @NotNull String[] params,
                             @NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable M> messageParser,
                             @NotNull BiConsumer<@NotNull CommandSender, @NotNull M> sendFunction) {
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
