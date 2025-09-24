package cc.carm.lib.mineconfiguration.adventure.value;

import cc.carm.lib.configuration.value.ValueManifest;
import cc.carm.lib.configuration.value.text.ConfiguredText;
import cc.carm.lib.configuration.value.text.data.TextContents;
import cc.carm.lib.easyplugin.utils.ColorParser;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class ConfiguredMessage extends ConfiguredText<Component, Audience> {

    public static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.legacySection();

    public static AtomicReference<ComponentSerializer<?, ?, String>> SERIALIZER = new AtomicReference<>(LEGACY_SERIALIZER);

    @NotNull

    public static ConfiguredMessage.Builder create() {
        return new Builder();
    }

    public static ConfiguredMessage ofString(@NotNull String... messages) {
        return create().defaults(messages).build();
    }

    public ConfiguredMessage(@NotNull ValueManifest<TextContents, TextContents> manifest,
                             @NotNull BiFunction<Audience, String, String> parser,
                             @NotNull BiFunction<Audience, String, Component> compiler,
                             @NotNull BiConsumer<Audience, List<Component>> dispatcher,
                             @NotNull String[] params) {
        super(manifest, parser, compiler, dispatcher, params);
    }

    public void sendActionBar(Audience audience, Object... values) {
        Component content = prepare(values).parseLine(audience, (sender, message) -> Component.text(message));
        if (content != null) audience.sendActionBar(content);
    }

    public static class Builder extends ConfiguredText.Builder<Component, Audience, Builder> {

        public Builder() {
            super();
            this.parser = (receiver, message) -> ColorParser.parse(message);
            this.compiler = (receiver, message) -> SERIALIZER.get().deserialize(message);
            this.dispatcher = (receiver, message) -> message.forEach(receiver::sendMessage);
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
