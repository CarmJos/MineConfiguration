package cc.carm.lib.mineconfiguration.bukkit.data;

import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import cc.carm.lib.mineconfiguration.bukkit.utils.TextParser;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredTitle;
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

    public static @NotNull TitleConfig of(@Nullable String line1, @Nullable String line2,
                                          int fadeIn, int stay, int fadeOut) {
        return of(
                Optional.ofNullable(line1).map(TextConfig::of).orElse(null),
                Optional.ofNullable(line2).map(TextConfig::of).orElse(null),
                fadeIn, stay, fadeOut
        );
    }

    public static @NotNull TitleConfig of(@Nullable TextConfig line1, @Nullable TextConfig line2,
                                          int fadeIn, int stay, int fadeOut) {
        return new TitleConfig(line1, line2, fadeIn, stay, fadeOut);
    }

    protected @Nullable TextConfig line1;
    protected @Nullable TextConfig line2;

    protected final int fadeIn;
    protected final int stay;
    protected final int fadeOut;

    protected TitleConfig(@Nullable TextConfig line1, @Nullable TextConfig line2) {
        this(line1, line2, -1, -1, -1);
    }

    protected TitleConfig(@Nullable TextConfig line1, @Nullable TextConfig line2, int fadeIn, int stay, int fadeOut) {
        this.line1 = line1;
        this.line2 = line2;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public int getFadeIn() {
        return fadeIn;
    }

    public int getFadeOut() {
        return fadeOut;
    }

    public int getStay() {
        return stay;
    }

    public boolean isStandardTime() {
        return this.fadeIn == 10 && this.stay == 60 && this.fadeOut == 10;
    }

    public @Nullable TextConfig getLine1() {
        return line1;
    }

    public @Nullable TextConfig getLine2() {
        return line2;
    }

    public void send(@NotNull Player player,
                     @NotNull Map<String, Object> placeholders,
                     @Nullable ConfiguredTitle.TitleConsumer sendConsumer) {
        send(
                player,
                this.fadeIn < 0 ? 10 : this.fadeIn,
                this.stay < 0 ? 60 : this.stay,
                this.fadeOut < 0 ? 10 : this.fadeOut,
                placeholders,
                sendConsumer
        );
    }

    public void send(@NotNull Player player,
                     @Range(from = 0L, to = Integer.MAX_VALUE) int fadeIn,
                     @Range(from = 0L, to = Integer.MAX_VALUE) int stay,
                     @Range(from = 0L, to = Integer.MAX_VALUE) int fadeOut,
                     @NotNull Map<String, Object> placeholders,
                     @Nullable ConfiguredTitle.TitleConsumer sendConsumer) {
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
        if (this.fadeIn > 0) map.put("fadeIn", this.fadeIn);
        if (this.stay > 0) map.put("stay", this.stay);
        if (this.fadeOut > 0) map.put("fadeOut", this.fadeOut);
        return map;
    }

    public static @Nullable TitleConfig deserialize(@NotNull ConfigurationWrapper<?> section) {
        String line1 = section.getString("line1");
        String line2 = section.getString("line2");
        if (line1 == null && line2 == null) return null;

        return of(
                line1, line2,
                section.getInt("fadeIn", -1),
                section.getInt("stay", -1),
                section.getInt("fadeOut", -1)
        );
    }

}
