package cc.carm.lib.configuration.bungee.value;

import cc.carm.lib.configuration.bungee.BungeeConfigValue;
import cc.carm.lib.configuration.bungee.builder.message.BungeeMessageValueBuilder;
import cc.carm.lib.configuration.bungee.data.MessageText;
import cc.carm.lib.configuration.common.value.ConfigMessage;
import cc.carm.lib.configuration.core.source.ConfigCommentInfo;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
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

    public ConfiguredMessage(@Nullable ConfigurationProvider<?> provider,
                             @Nullable String sectionPath, @Nullable ConfigCommentInfo comments,
                             @NotNull MessageText defaultMessage, @NotNull String[] params,
                             @NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable M> messageParser,
                             @NotNull BiConsumer<@NotNull CommandSender, @NotNull M> sendFunction) {
        super(provider, sectionPath, comments, MessageText.class, defaultMessage, params, messageParser, sendFunction, MessageText::of);
    }


    public void broadcast(@NotNull Map<String, Object> placeholders) {
        ProxyServer.getInstance().getPlayers().forEach(pl -> send(pl, placeholders));
        send(ProxyServer.getInstance().getConsole(), placeholders);
    }


}
