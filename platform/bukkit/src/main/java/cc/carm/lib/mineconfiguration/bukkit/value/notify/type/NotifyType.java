package cc.carm.lib.mineconfiguration.bukkit.value.notify.type;

import cc.carm.lib.configuration.value.text.function.TextParser;
import cc.carm.lib.mineconfiguration.bukkit.utils.MessageUtils;
import cc.carm.lib.mineconfiguration.bukkit.value.notify.type.standard.SoundNotify;
import cc.carm.lib.mineconfiguration.bukkit.value.notify.type.standard.StringNotify;
import cc.carm.lib.mineconfiguration.bukkit.value.notify.type.standard.TitleNotify;
import com.cryptomorin.xseries.messages.ActionBar;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class NotifyType<M> {

    public interface Standard {

        StringNotify MESSAGE = StringNotify.of("MESSAGE", Player::sendMessage, content -> Optional.ofNullable(content).orElse(" "));
        StringNotify MSG = StringNotify.of("MSG", Player::sendMessage);
        StringNotify ACTIONBAR = StringNotify.of("ACTIONBAR", ActionBar::sendActionBar);
        TitleNotify TITLE = new TitleNotify("TITLE");
        SoundNotify SOUND = new SoundNotify("SOUND");

        static NotifyType<?>[] values() {
            return new NotifyType<?>[]{MESSAGE, MSG, ACTIONBAR, TITLE, SOUND};
        }

        static NotifyType<?> valueOf(String name) {
            return Arrays.stream(values()).filter(type -> type.key.equalsIgnoreCase(name)).findFirst().orElse(null);
        }

    }

    public static final Set<NotifyType<?>> TYPES = new HashSet<>(Arrays.asList(Standard.values()));

    public static NotifyType<?> get(@NotNull String key) {
        return TYPES.stream().filter(type -> type.key.equalsIgnoreCase(key)).findFirst().orElse(null);
    }

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
    protected String setPlaceholders(@NotNull Player player, @NotNull Map<String, Object> placeholders,
                                     @Nullable String content) {
        if (content == null) return null;
        return MessageUtils.parseMessage(player, TextParser.setPlaceholders(content, placeholders));
    }

}
