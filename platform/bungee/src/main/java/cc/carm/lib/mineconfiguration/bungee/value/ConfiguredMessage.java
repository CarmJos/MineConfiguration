package cc.carm.lib.mineconfiguration.bungee.value;

import cc.carm.lib.configuration.value.ValueManifest;
import cc.carm.lib.configuration.value.text.ConfiguredText;
import cc.carm.lib.configuration.value.text.data.TextContents;
import cc.carm.lib.easyplugin.utils.ColorParser;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class ConfiguredMessage extends ConfiguredText<BaseComponent[], CommandSender> {

    @NotNull
    public static ConfiguredMessage.Builder create() {
        return new Builder();
    }

    public static ConfiguredMessage ofString(@NotNull String... messages) {
        return create().defaults(messages).build();
    }

    public ConfiguredMessage(@NotNull ValueManifest<TextContents, TextContents> manifest,
                             @NotNull BiFunction<CommandSender, String, String> parser,
                             @NotNull BiFunction<CommandSender, String, BaseComponent[]> compiler,
                             @NotNull BiConsumer<CommandSender, List<BaseComponent[]>> dispatcher,
                             @NotNull String[] params) {
        super(manifest, parser, compiler, dispatcher, params);
    }

    public void print(Object... values) {
        prepare(values).to(ProxyServer.getInstance().getConsole());
    }

    public static class Builder extends ConfiguredText.Builder<BaseComponent[], CommandSender, Builder> {

        public Builder() {
            super();
            this.parser = (sender, message) -> ColorParser.parse(message);
            this.compiler = (sender, message) -> new BaseComponent[]{new TextComponent(message)};
            this.dispatcher = (sender, message) -> {
                for (BaseComponent[] component : message) {
                    sender.sendMessage(component);
                }
            };
        }

        @Override
        public @NotNull ConfiguredMessage build() {
            return new ConfiguredMessage(buildManifest(), this.parser, this.compiler, this.dispatcher, this.params);
        }

        @Override
        public @NotNull ConfiguredMessage.Builder self() {
            return this;
        }
    }


}
