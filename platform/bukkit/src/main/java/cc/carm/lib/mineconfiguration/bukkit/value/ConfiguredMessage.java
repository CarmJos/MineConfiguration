package cc.carm.lib.mineconfiguration.bukkit.value;

import cc.carm.lib.configuration.value.ValueManifest;
import cc.carm.lib.configuration.value.text.ConfiguredText;
import cc.carm.lib.configuration.value.text.data.TextContents;
import cc.carm.lib.mineconfiguration.bukkit.utils.MessageUtils;
import com.cryptomorin.xseries.messages.ActionBar;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class ConfiguredMessage<M> extends ConfiguredText<M, CommandSender> {

    @NotNull
    public static <M> ConfiguredMessage.Builder<M> create(
            @NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable M> compiler
    ) {
        return new Builder<M>().compiler(compiler);
    }

    public static Builder<String> asString() {
        return create((sender, message) -> message)
                .parser(MessageUtils::parseMessage)
                .dispatcher((sender, message) -> message.forEach(sender::sendMessage));
    }

    public static ConfiguredMessage<String> ofString() {
        return asString().build();
    }

    public static ConfiguredMessage<String> ofString(@NotNull String... messages) {
        return asString().defaults(messages).build();
    }

    public ConfiguredMessage(@NotNull ValueManifest<TextContents, TextContents> manifest,
                             @NotNull BiFunction<CommandSender, String, String> parser,
                             @NotNull BiFunction<CommandSender, String, M> compiler,
                             @NotNull BiConsumer<CommandSender, List<M>> dispatcher,
                             @NotNull String[] params) {
        super(manifest, parser, compiler, dispatcher, params);
    }

    public void sendActionBar(@NotNull Player player, Object... values) {
        ActionBar.sendActionBar(player, prepare(values).parseLine(player, (sender, message) -> message));
    }

    public void print(Object... values) {
        prepare(values).to(Bukkit.getConsoleSender());
    }

    public static class Builder<M> extends ConfiguredText.Builder<M, CommandSender, Builder<M>> {

        @Override
        public @NotNull ConfiguredMessage<M> build() {
            return new ConfiguredMessage<>(buildManifest(), this.parser, this.compiler, this.dispatcher, this.params);
        }

        @Override
        public @NotNull ConfiguredMessage.Builder<M> self() {
            return this;
        }
    }


}
