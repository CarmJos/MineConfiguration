package cc.carm.lib.configuration.craft.value;

import cc.carm.lib.configuration.core.function.ConfigDataFunction;
import cc.carm.lib.configuration.core.source.ConfigCommentInfo;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.value.type.ConfiguredList;
import cc.carm.lib.configuration.craft.CraftConfigValue;
import cc.carm.lib.configuration.craft.builder.message.MessageListBuilder;
import cc.carm.lib.configuration.craft.data.MessageText;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class ConfiguredMessageList<M> extends ConfiguredList<MessageText> {

    @NotNull
    public static <M> MessageListBuilder<M> create(@NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable M> messageParser) {
        return CraftConfigValue.builder().createMessage().asList(messageParser);
    }

    public static MessageListBuilder<String> fromString() {
        return CraftConfigValue.builder().createMessage().asStringList();
    }

    public static ConfiguredMessageList<String> ofString(@NotNull String... defaultMessages) {
        return CraftConfigValue.builder().createMessage().listOfString(defaultMessages);
    }

    protected final @NotNull String[] params;
    protected final @NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable M> messageParser;
    protected final @NotNull BiConsumer<@NotNull CommandSender, @NotNull List<M>> sendFunction;

    public ConfiguredMessageList(@Nullable ConfigurationProvider<?> provider,
                                 @Nullable String sectionPath, @Nullable ConfigCommentInfo comments,
                                 @NotNull List<MessageText> messages, @NotNull String[] params,
                                 @NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable M> messageParser,
                                 @NotNull BiConsumer<@NotNull CommandSender, @NotNull List<M>> sendFunction) {
        super(provider, sectionPath, comments, MessageText.class, messages,
                ConfigDataFunction.castToString().andThen(MessageText::new), MessageText::getMessage
        );
        this.params = params;
        this.messageParser = messageParser;
        this.sendFunction = sendFunction;
    }

    public @Nullable List<M> parse(@Nullable CommandSender sender, @Nullable Object... values) {
        return parse(sender, MessageText.buildParams(params, values));
    }

    public @Nullable List<M> parse(@Nullable CommandSender sender, @NotNull Map<String, Object> placeholders) {
        List<MessageText> list = get();
        if (list.isEmpty()) return null;

        List<String> messages = list.stream().map(MessageText::getMessage).collect(Collectors.toList());
        if (String.join("", messages).isEmpty()) return null;

        return list.stream().map(value -> value.parse(this.messageParser, sender, placeholders))
                .collect(Collectors.toList());
    }

    public void send(@Nullable CommandSender receiver, @Nullable Object... values) {
        send(receiver, MessageText.buildParams(params, values));
    }

    public void send(@Nullable CommandSender receiver, @NotNull Map<String, Object> placeholders) {
        if (receiver == null) return;
        List<M> parsed = parse(receiver, placeholders);
        if (parsed == null) return;
        sendFunction.accept(receiver, parsed);
    }

    public void broadcast(@Nullable Object... values) {
        broadcast(MessageText.buildParams(params, values));
    }

    public void broadcast(@NotNull Map<String, Object> placeholders) {
        Bukkit.getOnlinePlayers().forEach(pl -> send(pl, placeholders));
        send(Bukkit.getConsoleSender(), placeholders);
    }

    public void setNull() {
        set(null);
    }

    public void setMessages(@NotNull String... values) {
        if (values.length == 0) {
            setNull();
            return;
        }
        set(MessageText.of(values));
    }

    public void setMessages(@Nullable List<String> values) {
        if (values == null || values.isEmpty()) {
            setNull();
            return;
        }
        set(MessageText.of(values));
    }

}
