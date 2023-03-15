package cc.carm.lib.mineconfiguration.bukkit.value;

import cc.carm.lib.configuration.core.value.ValueManifest;
import cc.carm.lib.mineconfiguration.bukkit.CraftConfigValue;
import cc.carm.lib.mineconfiguration.bukkit.builder.message.CraftMessageValueBuilder;
import cc.carm.lib.mineconfiguration.bukkit.data.TextConfig;
import cc.carm.lib.mineconfiguration.common.value.ConfigMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class ConfiguredMessage<M> extends ConfigMessage<M, TextConfig, CommandSender> {

    @NotNull
    public static <M> CraftMessageValueBuilder<@Nullable M> create(@NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable M> messageParser) {
        return CraftConfigValue.builder().createMessage().asValue(messageParser);
    }

    public static CraftMessageValueBuilder<String> asString() {
        return CraftConfigValue.builder().createMessage().asStringValue();
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
        senders.add(Bukkit.getConsoleSender());
        senders.addAll(Bukkit.getOnlinePlayers());
        return senders;
    }

}
