package cc.carm.lib.configuration.craft.data;

import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import cc.carm.lib.configuration.craft.function.TitleSendConsumer;
import cc.carm.lib.configuration.craft.utils.TextParser;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class TitleConfig {

    public static @NotNull TitleConfig of(@Nullable String line1, @Nullable String line2) {
        return of(
                Optional.ofNullable(line1).map(TextConfig::of).orElse(null),
                Optional.ofNullable(line2).map(TextConfig::of).orElse(null)
        );
    }

    public static @NotNull TitleConfig of(@Nullable TextConfig line1, @Nullable TextConfig line2) {
        return new TitleConfig(line1, line2);
    }

    protected @Nullable TextConfig line1;
    protected @Nullable TextConfig line2;

    protected TitleConfig(@Nullable TextConfig line1, @Nullable TextConfig line2) {
        this.line1 = line1;
        this.line2 = line2;
    }

    public void send(@NotNull Player player,
                     @Range(from = 0L, to = Long.MAX_VALUE) long fadeIn,
                     @Range(from = 0L, to = Long.MAX_VALUE) long stay,
                     @Range(from = 0L, to = Long.MAX_VALUE) long fadeOut,
                     @NotNull Map<String, Object> placeholders,
                     @Nullable TitleSendConsumer sendConsumer) {
        if (this.line1 == null && this.line2 == null) return;
        if (sendConsumer == null) return;
        sendConsumer.send(
                player, fadeIn, stay, fadeOut,
                parseLine(this.line1, player, placeholders),
                parseLine(this.line2, player, placeholders)
        );
    }

    protected @NotNull String parseLine(@Nullable TextConfig text,
                                        @NotNull Player player, @NotNull Map<String, Object> placeholders) {
        if (text == null) return "";
        else return TextParser.parseText(player, text.getMessage(), placeholders);
    }

    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        if (this.line1 != null) map.put("line1", this.line1.getMessage());
        if (this.line2 != null) map.put("line2", this.line2.getMessage());
        return map;
    }

    public static @NotNull TitleConfig deserialize(@NotNull ConfigurationWrapper section) {
        return of(section.getString("line1"), section.getString("line2"));
    }

}
