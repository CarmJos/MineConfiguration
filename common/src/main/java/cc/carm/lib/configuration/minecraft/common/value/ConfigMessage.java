package cc.carm.lib.configuration.minecraft.common.value;

import cc.carm.lib.configuration.minecraft.common.data.AbstractText;
import cc.carm.lib.configuration.minecraft.common.utils.ParamsUtils;
import cc.carm.lib.configuration.core.function.ConfigValueParser;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class ConfigMessage<M, T extends AbstractText<R>, R>
        extends ConfiguredValue<T> {

    protected final @NotNull String[] params;
    protected final @NotNull BiFunction<@Nullable R, @NotNull String, @Nullable M> messageParser;
    protected final @NotNull BiConsumer<@NotNull R, @NotNull M> messageConsumer;

    protected final @NotNull Function<String, T> textBuilder;

    public ConfigMessage(@Nullable ConfigurationProvider<?> provider, @Nullable String sectionPath,
                         @Nullable List<String> headerComments, @Nullable String inlineComments,
                         @NotNull Class<T> textClazz, @NotNull T defaultMessage, @NotNull String[] params,
                         @NotNull BiFunction<@Nullable R, @NotNull String, @Nullable M> messageParser,
                         @NotNull BiConsumer<@NotNull R, @NotNull M> messageConsumer,
                         @NotNull Function<String, T> textBuilder) {
        super(provider, sectionPath, headerComments, inlineComments, textClazz, defaultMessage,
                ConfigValueParser.castToString().andThen((s, d) -> textBuilder.apply(s)), AbstractText::getMessage
        );
        this.params = params;
        this.messageParser = messageParser;
        this.messageConsumer = messageConsumer;
        this.textBuilder = textBuilder;
    }

    public @Nullable M parse(@Nullable R sender, @Nullable Object... values) {
        return parse(sender, ParamsUtils.buildParams(params, values));
    }

    public @Nullable M parse(@Nullable R sender, @NotNull Map<String, Object> placeholders) {
        T value = get();
        if (value == null || value.getMessage().isEmpty()) return null;
        else return value.parse(this.messageParser, sender, placeholders);
    }

    public void send(@Nullable R receiver, @Nullable Object... values) {
        send(receiver, ParamsUtils.buildParams(params, values));
    }

    public void send(@Nullable R receiver, @NotNull Map<String, Object> placeholders) {
        if (receiver == null) return;
        M parsed = parse(receiver, placeholders);
        if (parsed == null) return;
        messageConsumer.accept(receiver, parsed);
    }

    public void broadcast(@Nullable Object... values) {
        broadcast(ParamsUtils.buildParams(params, values));
    }

    public abstract void broadcast(@NotNull Map<String, Object> placeholders);

    public void set(@Nullable String value) {
        this.set(value == null ? null : buildText(value));
    }

    protected T buildText(String value) {
        return textBuilder.apply(value);
    }
}
