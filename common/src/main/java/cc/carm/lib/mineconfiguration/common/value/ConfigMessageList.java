package cc.carm.lib.mineconfiguration.common.value;

import cc.carm.lib.mineconfiguration.common.utils.ParamsUtils;
import cc.carm.lib.mineconfiguration.common.data.AbstractText;
import cc.carm.lib.configuration.core.function.ConfigDataFunction;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.value.type.ConfiguredList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class ConfigMessageList<M, T extends AbstractText<R>, R> extends ConfiguredList<T> {

    protected final @NotNull String[] params;
    protected final @NotNull BiFunction<@Nullable R, @NotNull String, @Nullable M> messageParser;
    protected final @NotNull BiConsumer<@NotNull R, @NotNull List<M>> sendFunction;

    protected final @NotNull Function<String, T> textBuilder;

    @SuppressWarnings("NullableProblems")
    public ConfigMessageList(@Nullable ConfigurationProvider<?> provider, @Nullable String sectionPath,
                             @Nullable List<String> headerComments, @Nullable String inlineComments,
                             @NotNull Class<T> textClazz, @NotNull List<T> messages, @NotNull String[] params,
                             @NotNull BiFunction<@Nullable R, @NotNull String, @Nullable M> messageParser,
                             @NotNull BiConsumer<@NotNull R, @NotNull List<M>> sendFunction,
                             @NotNull Function<String, @NotNull T> textBuilder) {
        super(
                provider, sectionPath, headerComments, inlineComments, textClazz, messages,
                ConfigDataFunction.castToString().andThen(textBuilder::apply), AbstractText::getMessage
        );
        this.params = params;
        this.messageParser = messageParser;
        this.sendFunction = sendFunction;
        this.textBuilder = textBuilder;
    }

    public @Nullable List<M> parse(@Nullable R sender, @Nullable Object... values) {
        return parse(sender, ParamsUtils.buildParams(params, values));
    }

    public @Nullable List<M> parse(@Nullable R sender, @NotNull Map<String, Object> placeholders) {
        List<T> list = get();
        if (list.isEmpty()) return null;

        List<String> messages = list.stream().map(T::getMessage).collect(Collectors.toList());
        if (String.join("", messages).isEmpty()) return null;

        return list.stream().map(value -> value.parse(this.messageParser, sender, placeholders))
                .collect(Collectors.toList());
    }

    public void send(@Nullable R receiver, @Nullable Object... values) {
        send(receiver, ParamsUtils.buildParams(params, values));
    }

    public void send(@Nullable R receiver, @NotNull Map<String, Object> placeholders) {
        if (receiver == null) return;
        List<M> parsed = parse(receiver, placeholders);
        if (parsed == null) return;
        sendFunction.accept(receiver, parsed);
    }

    public void broadcast(@Nullable Object... values) {
        broadcast(ParamsUtils.buildParams(params, values));
    }

    public abstract void broadcast(@NotNull Map<String, Object> placeholders);

    public void setMessages(@NotNull String... values) {
        setMessages(values.length == 0 ? null : Arrays.asList(values));
    }

    public void setMessages(@Nullable List<String> values) {
        if (values == null || values.isEmpty()) {
            set(null);
        } else {
            set(buildText(values));
        }

    }

    protected List<T> buildText(List<String> values) {
        return values.stream().map(textBuilder).collect(Collectors.toList());
    }

}
