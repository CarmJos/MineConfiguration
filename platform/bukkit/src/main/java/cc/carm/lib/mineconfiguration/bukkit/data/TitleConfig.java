package cc.carm.lib.mineconfiguration.bukkit.data;

import cc.carm.lib.configuration.source.section.ConfigureSection;
import cc.carm.lib.configuration.value.text.PreparedText;
import cc.carm.lib.mineconfiguration.bukkit.utils.MessageUtils;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredTitle;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.LinkedHashMap;
import java.util.Map;

public class TitleConfig {

    public static @NotNull TitleConfig of(@Nullable String line1, @Nullable String line2) {
        return new TitleConfig(line1, line2);
    }

    public static @NotNull TitleConfig of(@Nullable String line1, @Nullable String line2,
                                          int fadeIn, int stay, int fadeOut) {
        return new TitleConfig(line1, line2, fadeIn, stay, fadeOut);
    }

    protected @Nullable String line1;
    protected @Nullable String line2;

    protected @Range(from = 0L, to = Integer.MAX_VALUE) int fadeIn = 10;
    protected @Range(from = 0L, to = Integer.MAX_VALUE) int stay = 60;
    protected @Range(from = 0L, to = Integer.MAX_VALUE) int fadeOut = 10;

    protected TitleConfig(@Nullable String line1, @Nullable String line2) {
        this.line1 = line1;
        this.line2 = line2;
    }

    protected TitleConfig(@Nullable String line1, @Nullable String line2, int fadeIn, int stay, int fadeOut) {
        this.line1 = line1;
        this.line2 = line2;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public int stay() {
        return stay;
    }

    public void stay(int stay) {
        this.stay = stay;
    }

    public @Nullable String line1() {
        return line1;
    }

    public void line1(@Nullable String line1) {
        this.line1 = line1;
    }

    public @Nullable String line2() {
        return line2;
    }

    public void line2(@Nullable String line2) {
        this.line2 = line2;
    }

    public int fadeIn() {
        return fadeIn;
    }

    public void fadeIn(int fadeIn) {
        this.fadeIn = fadeIn;
    }

    public int fadeOut() {
        return fadeOut;
    }

    public void fadeOut(int fadeOut) {
        this.fadeOut = fadeOut;
    }

    public boolean isStandardTime() {
        return this.fadeIn == 10 && this.stay == 60 && this.fadeOut == 10;
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
                parseLine(player, this.line1, placeholders),
                parseLine(player, this.line2, placeholders)
        );
    }

    protected @NotNull String parseLine(@NotNull Player player, @Nullable String text,
                                        @NotNull Map<String, Object> placeholders) {
        return text == null ? "" : MessageUtils.parseMessage(player, PreparedText.setPlaceholders(text, placeholders));
    }

    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        if (this.line1 != null) map.put("line1", this.line1);
        if (this.line2 != null) map.put("line2", this.line2);
        if (this.fadeIn > 0 && this.fadeIn != 10) map.put("fadeIn", this.fadeIn);
        if (this.stay > 0 && this.stay != 60) map.put("stay", this.stay);
        if (this.fadeOut > 0 && this.fadeOut != 10) map.put("fadeOut", this.fadeOut);

        return map;
    }

    public static @Nullable TitleConfig deserialize(@NotNull ConfigureSection section) {
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
