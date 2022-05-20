package cc.carm.lib.mineconfiguration.bukkit.value;

import cc.carm.lib.mineconfiguration.bukkit.CraftConfigValue;
import cc.carm.lib.mineconfiguration.bukkit.data.TextConfig;
import cc.carm.lib.mineconfiguration.common.value.ConfigMessageList;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.mineconfiguration.bukkit.builder.message.CraftMessageListBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class ConfiguredMessageList<M> extends ConfigMessageList<M, TextConfig, CommandSender> {

    @NotNull
    public static <M> CraftMessageListBuilder<M> create(@NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable M> messageParser) {
        return CraftConfigValue.builder().createMessage().asList(messageParser);
    }

    public static CraftMessageListBuilder<String> asStrings() {
        return CraftConfigValue.builder().createMessage().asStringList();
    }

    public static ConfiguredMessageList<String> ofStrings(@NotNull String... defaultMessages) {
        return asStrings().defaults(defaultMessages).build();
    }

    public ConfiguredMessageList(@Nullable ConfigurationProvider<?> provider, @Nullable String sectionPath,
                                 @Nullable List<String> headerComments, @Nullable String inlineComments,
                                 @NotNull List<TextConfig> messages, @NotNull String[] params,
                                 @NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable M> messageParser,
                                 @NotNull BiConsumer<@NotNull CommandSender, @NotNull List<M>> sendFunction) {
        super(provider, sectionPath, headerComments, inlineComments, TextConfig.class, messages, params, messageParser, sendFunction, TextConfig::of);
    }

    public void broadcast(@NotNull Map<String, Object> placeholders) {
        Bukkit.getOnlinePlayers().forEach(pl -> send(pl, placeholders));
        send(Bukkit.getConsoleSender(), placeholders);
    }

}
