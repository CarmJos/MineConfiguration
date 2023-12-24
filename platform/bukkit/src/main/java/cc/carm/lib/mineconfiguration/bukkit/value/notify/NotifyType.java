package cc.carm.lib.mineconfiguration.bukkit.value.notify;

import cc.carm.lib.mineconfiguration.bukkit.utils.TextParser;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.regex.Pattern;

public abstract class NotifyType<M> {

    // Notify config format: [TYPE(@PARAM)] CONTENTS...
    public static final @NotNull Pattern CONFIG_FORMAT = Pattern.compile("^\\[(?<type>[^@\\]]+)(@(?<param>[^]]+))?] (?<content>.*)$");

    protected final @NotNull String key;
    protected final @NotNull Class<M> metaClass;

    protected NotifyType(@NotNull String key, @NotNull Class<M> metaClass) {
        this.key = key;
        this.metaClass = metaClass;
    }

    public @NotNull String getKey() {
        return key;
    }

    public @NotNull Class<M> getMetaClass() {
        return metaClass;
    }

    /**
     * Parse metadata from string.
     * <br> e.g. "[TYPE-KEY@PARAM] CONTENTS..."
     *
     * @param param   The param of the notify config.
     * @param content The content of the notify config.
     * @return The parsed metadata.
     */
    public abstract @Nullable M parseMeta(@Nullable String param, @Nullable String content);

    /**
     * Serialize the metadata to singleton string to storage on configs.
     *
     * @param meta The parsed metadata.
     * @return The serialized string.
     */
    public abstract @NotNull String serializeConfig(@Nullable M meta);

    /**
     * Execute the notify content to specific player.
     *
     * @param player The player who receive the notification.
     * @param meta   The parsed metadata.
     */
    public abstract void execute(@NotNull Player player, @Nullable M meta, @NotNull Map<String, Object> placeholders);

    @Contract("_, _, null -> null; _, _, !null -> !null")
    protected String setPlaceholders(@NotNull Player player, @Nullable String content,
                                     @NotNull Map<String, Object> placeholders) {
        return TextParser.parseText(player, content, placeholders);
    }

}
