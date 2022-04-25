package cc.carm.lib.configuration.craft.value;

import cc.carm.lib.configuration.core.function.ConfigValueParser;
import cc.carm.lib.configuration.core.source.ConfigCommentInfo;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;
import cc.carm.lib.configuration.craft.CraftConfigValue;
import cc.carm.lib.configuration.craft.builder.message.MessageConfigBuilder;
import cc.carm.lib.configuration.craft.data.MessageText;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class ConfiguredMessage<M> extends ConfiguredValue<MessageText> {


    public static MessageConfigBuilder create() {
        return CraftConfigValue.builder().createMessage();
    }

    public static ConfiguredMessage<String> ofString() {
        return create().valueOfString();
    }

    public static ConfiguredMessage<String> ofString(@NotNull String defaultMessage) {
        return create().valueOfString(defaultMessage);
    }

    protected final @NotNull String[] params;
    protected final @NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable M> messageParser;
    protected final @NotNull BiConsumer<@NotNull CommandSender, @NotNull M> sendFunction;

    public ConfiguredMessage(@Nullable ConfigurationProvider<?> provider,
                             @Nullable String sectionPath, @Nullable ConfigCommentInfo comments,
                             @NotNull String message, @NotNull String[] params,
                             @NotNull BiFunction<@Nullable CommandSender, @NotNull String, @Nullable M> messageParser,
                             @NotNull BiConsumer<@NotNull CommandSender, @NotNull M> sendFunction) {
        super(provider, sectionPath, comments, MessageText.class, MessageText.of(message),
                ConfigValueParser.castToString().andThen((s, d) -> MessageText.of(s)),
                MessageText::getMessage
        );
        this.params = params;
        this.messageParser = messageParser;
        this.sendFunction = sendFunction;
    }

    public @Nullable M parse(@Nullable CommandSender sender, @Nullable Object... values) {
        MessageText value = get();
        if (value == null) return null;
        else return value.parse(this.messageParser, sender, this.params, values);
    }

    public @Nullable M parse(@Nullable CommandSender sender, @NotNull Map<String, Object> placeholders) {
        MessageText value = get();
        if (value == null) return null;
        else return value.parse(this.messageParser, sender, placeholders);
    }

    public void send(@Nullable CommandSender receiver, @Nullable Object... values) {
        if (receiver == null) return;
        M parsed = parse(receiver, values);
        if (parsed == null) return;
        sendFunction.accept(receiver, parsed);
    }

    public void send(@Nullable CommandSender receiver, @NotNull Map<String, Object> placeholders) {
        if (receiver == null) return;
        M parsed = parse(receiver, placeholders);
        if (parsed == null) return;
        sendFunction.accept(receiver, parsed);
    }

    public void broadcast(@Nullable Object... values) {
        Bukkit.getOnlinePlayers().forEach(pl -> send(pl, values));
        send(Bukkit.getConsoleSender(), values);
    }

    public void broadcast(@NotNull Map<String, Object> placeholders) {
        Bukkit.getOnlinePlayers().forEach(pl -> send(pl, placeholders));
        send(Bukkit.getConsoleSender(), placeholders);
    }

    public void set(@Nullable String value) {
        this.set(value == null ? null : new MessageText(value));
    }

}
