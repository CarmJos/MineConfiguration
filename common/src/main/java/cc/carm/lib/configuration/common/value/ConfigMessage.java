package cc.carm.lib.configuration.common.value;

import cc.carm.lib.configuration.common.data.AbstractText;
import cc.carm.lib.configuration.core.function.ConfigValueParser;
import cc.carm.lib.configuration.core.source.ConfigCommentInfo;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class ConfigMessage<M, T extends AbstractText<R>, R>
        extends ConfiguredValue<T> {

    protected final @NotNull String[] params;
    protected final @NotNull BiFunction<@Nullable R, @NotNull String, @Nullable M> messageParser;
    protected final @NotNull BiConsumer<@NotNull R, @NotNull M> sendFunction;

    protected final @NotNull Function<String, T> textBuilder;

    public ConfigMessage(@Nullable ConfigurationProvider<?> provider,
                         @Nullable String sectionPath, @Nullable ConfigCommentInfo comments,
                         @NotNull Class<T> textClazz, @NotNull T defaultMessage, @NotNull String[] params,
                         @NotNull BiFunction<@Nullable R, @NotNull String, @Nullable M> messageParser,
                         @NotNull BiConsumer<@NotNull R, @NotNull M> sendFunction,
                         @NotNull Function<String, T> textBuilder) {
        super(provider, sectionPath, comments, textClazz, defaultMessage,
                ConfigValueParser.castToString().andThen((s, d) -> textBuilder.apply(s)), AbstractText::getMessage
        );
        this.params = params;
        this.messageParser = messageParser;
        this.sendFunction = sendFunction;
        this.textBuilder = textBuilder;
    }

    public @Nullable M parse(@Nullable R sender, @Nullable Object... values) {
        return parse(sender, AbstractText.buildParams(params, values));
    }

    public @Nullable M parse(@Nullable R sender, @NotNull Map<String, Object> placeholders) {
        T value = get();
        if (value == null || value.getMessage().isEmpty()) return null;
        else return value.parse(this.messageParser, sender, placeholders);
    }

    public void send(@Nullable R receiver, @Nullable Object... values) {
        send(receiver, AbstractText.buildParams(params, values));
    }

    public void send(@Nullable R receiver, @NotNull Map<String, Object> placeholders) {
        if (receiver == null) return;
        M parsed = parse(receiver, placeholders);
        if (parsed == null) return;
        sendFunction.accept(receiver, parsed);
    }

    public void broadcast(@Nullable Object... values) {
        broadcast(AbstractText.buildParams(params, values));
    }

    public abstract void broadcast(@NotNull Map<String, Object> placeholders);

    public void set(@Nullable String value) {
        this.set(value == null ? null : buildText(value));
    }

    protected T buildText(String value) {
        return textBuilder.apply(value);
    }
}
