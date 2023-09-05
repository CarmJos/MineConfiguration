package cc.carm.lib.mineconfiguration.common.value;

import cc.carm.lib.configuration.core.function.ConfigValueParser;
import cc.carm.lib.configuration.core.value.ValueManifest;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;
import cc.carm.lib.mineconfiguration.common.data.AbstractText;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class ConfigMessage<M, T extends AbstractText<R>, R>
        extends ConfiguredValue<T> implements BaseMessage<R, M> {

    protected final @NotNull String[] params;
    protected final @NotNull BiFunction<@Nullable R, @NotNull String, @Nullable M> messageParser;
    protected final @NotNull BiConsumer<@NotNull R, @NotNull M> sendFunction;

    protected final @NotNull Function<String, T> textBuilder;

    public ConfigMessage(@NotNull ValueManifest<T> manifest,
                         @NotNull Class<T> textClazz, @NotNull String[] params,
                         @NotNull BiFunction<@Nullable R, @NotNull String, @Nullable M> messageParser,
                         @NotNull BiConsumer<@NotNull R, @NotNull M> sendFunction,
                         @NotNull Function<String, T> textBuilder) {
        super(
                manifest, textClazz,
                ConfigValueParser.castToString().andThen((s, d) -> textBuilder.apply(s)),
                AbstractText::getMessage
        );
        this.params = params;
        this.messageParser = messageParser;
        this.sendFunction = sendFunction;
        this.textBuilder = textBuilder;
    }

    @Override
    public String[] getParams() {
        return params;
    }

    @Override
    public void apply(@NotNull R receiver, @NotNull M message) {
        sendFunction.accept(receiver, message);
    }

    protected <N> @Nullable N parseTo(@Nullable R sender, @NotNull Map<String, Object> placeholders,
                                      @NotNull BiFunction<@Nullable R, @NotNull String, @Nullable N> parser) {
        T value = get();
        if (value == null || value.getMessage().isEmpty()) return null;
        else return value.parse(parser, sender, placeholders);
    }

    public @Nullable String parseString(@Nullable R sender, @NotNull Map<String, Object> placeholders) {
        return parseTo(sender, placeholders, (r, s) -> s);
    }

    @Override
    public @Nullable M parse(@Nullable R sender, @NotNull Map<String, Object> placeholders) {
        return parseTo(sender, placeholders, this.messageParser);
    }

    public void set(@Nullable String value) {
        this.set(value == null ? null : buildText(value));
    }

    protected T buildText(String value) {
        return textBuilder.apply(value);
    }

    public abstract class PreparedMessage<P, N> {

        protected final @NotNull Object[] values;

        protected PreparedMessage(@NotNull Object[] values) {
            this.values = values;
        }

        public Object[] getValues() {
            return values;
        }

        public abstract void to(P receiver);

        public void to(Collection<P> receivers) {
            receivers.forEach(this::to);
        }

        public N get(P receiver) {
            return null;
        }

    }

}
