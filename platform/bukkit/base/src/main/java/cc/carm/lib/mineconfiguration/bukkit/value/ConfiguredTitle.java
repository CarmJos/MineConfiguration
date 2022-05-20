package cc.carm.lib.mineconfiguration.bukkit.value;

import cc.carm.lib.mineconfiguration.bukkit.CraftConfigValue;
import cc.carm.lib.mineconfiguration.bukkit.data.TitleConfig;
import cc.carm.lib.mineconfiguration.common.utils.ParamsUtils;
import cc.carm.lib.configuration.core.function.ConfigValueParser;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import cc.carm.lib.configuration.core.value.type.ConfiguredSection;
import cc.carm.lib.mineconfiguration.bukkit.builder.title.TitleConfigBuilder;
import cc.carm.lib.mineconfiguration.bukkit.function.TitleSendConsumer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.List;
import java.util.Map;

public class ConfiguredTitle extends ConfiguredSection<TitleConfig> {

    public static TitleConfigBuilder create() {
        return CraftConfigValue.builder().createTitle();
    }

    public static ConfiguredTitle of(@Nullable String line1, @Nullable String line2) {
        return create().defaults(line1, line2).build();
    }

    public static ConfiguredTitle of(@Nullable String line1, @Nullable String line2,
                                     int fadeIn, int stay, int fadeOut) {
        return create().defaults(line1, line2).fadeIn(fadeIn).stay(stay).fadeOut(fadeOut).build();
    }

    protected final @NotNull TitleSendConsumer sendConsumer;
    protected final @NotNull String[] params;

    protected final int fadeIn;
    protected final int stay;
    protected final int fadeOut;

    public ConfiguredTitle(@Nullable ConfigurationProvider<?> provider, @Nullable String sectionPath,
                           @Nullable List<String> headerComments, @Nullable String inlineComments,
                           @Nullable TitleConfig defaultValue, @NotNull String[] params,
                           @NotNull TitleSendConsumer sendConsumer,
                           int fadeIn, int stay, int fadeOut) {
        super(provider, sectionPath, headerComments, inlineComments, TitleConfig.class, defaultValue, getTitleParser(), TitleConfig::serialize);
        this.sendConsumer = sendConsumer;
        this.params = params;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    @Range(from = 0L, to = Integer.MAX_VALUE)
    public int getFadeInTicks() {
        return fadeIn;
    }

    @Range(from = 0L, to = Integer.MAX_VALUE)
    public int getStayTicks() {
        return stay;
    }

    @Range(from = 0L, to = Integer.MAX_VALUE)
    public int getFadeOutTicks() {
        return fadeOut;
    }

    public @NotNull TitleSendConsumer getSendConsumer() {
        return sendConsumer;
    }

    public void send(@NotNull Player player, Object... values) {
        send(player, this.params, values);
    }

    public void send(@NotNull Player player, @NotNull String[] params, @NotNull Object[] values) {
        send(player, ParamsUtils.buildParams(params, values));
    }

    public void send(@NotNull Player player, @NotNull Map<String, Object> placeholders) {
        TitleConfig config = get();
        if (config != null) {
            config.send(player, fadeIn, stay, fadeOut, placeholders, sendConsumer);
        }
    }

    public void sendAll(Object... values) {
        sendAll(this.params, values);
    }

    public void sendAll(@NotNull String[] params, @NotNull Object[] values) {
        sendAll(ParamsUtils.buildParams(params, values));
    }

    public void sendAll(@NotNull Map<String, Object> placeholders) {
        TitleConfig config = get();
        if (config == null) return;

        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> send(onlinePlayer, placeholders));
    }


    public static ConfigValueParser<ConfigurationWrapper, TitleConfig> getTitleParser() {
        return (s, d) -> TitleConfig.deserialize(s);
    }
}
