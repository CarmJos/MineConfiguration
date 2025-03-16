package cc.carm.lib.mineconfiguration.bukkit.value;

import cc.carm.lib.configuration.adapter.ValueAdapter;
import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.builder.AbstractConfigBuilder;
import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.configuration.source.section.ConfigureSection;
import cc.carm.lib.configuration.value.ValueManifest;
import cc.carm.lib.configuration.value.standard.ConfiguredValue;
import cc.carm.lib.configuration.value.text.function.TextParser;
import cc.carm.lib.mineconfiguration.bukkit.data.TitleConfig;
import com.cryptomorin.xseries.messages.Titles;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class ConfiguredTitle extends ConfiguredValue<TitleConfig> {

    public static final @NotNull ConfiguredTitle.TitleConsumer DEFAULT_TITLE_CONSUMER = Titles::sendTitle;

    public static Builder create() {
        return new Builder();
    }

    public static ConfiguredTitle of(@Nullable String line1, @Nullable String line2) {
        return create().defaults(line1, line2).build();
    }

    public static ConfiguredTitle of(@Nullable String line1, @Nullable String line2,
                                     int fadeIn, int stay, int fadeOut) {
        return create().defaults(line1, line2).fadeIn(fadeIn).stay(stay).fadeOut(fadeOut).build();
    }

    public static final ValueType<TitleConfig> TITLE_TYPE = ValueType.of(TitleConfig.class);
    public static final ValueAdapter<TitleConfig> TITLE_ADAPTER = new ValueAdapter<>(TITLE_TYPE,
            (holder, type, value) -> value.serialize(),
            (holder, type, value) -> {
                ConfigureSection section = holder.deserialize(ConfigureSection.class, value);
                return TitleConfig.deserialize(section);
            }
    );

    protected final @NotNull ConfiguredTitle.TitleConsumer sendConsumer;

    protected final @NotNull UnaryOperator<String> paramBuilder;
    protected final @NotNull String[] params;

    public ConfiguredTitle(@NotNull ValueManifest<TitleConfig, TitleConfig> manifest, ValueAdapter<TitleConfig> adapter,
                           @NotNull UnaryOperator<String> paramBuilder, @NotNull String[] params,
                           @NotNull ConfiguredTitle.TitleConsumer sendConsumer) {
        super(manifest, adapter);
        this.paramBuilder = paramBuilder;
        this.sendConsumer = sendConsumer;
        this.params = params;
    }

    public void set(Consumer<TitleConfig> handler) {
        TitleConfig conf = get();
        handler.accept(conf);
        set(conf);
    }

    public void setLine1(@Nullable String line1) {
        set(conf -> conf.line1(line1));
    }

    public void setLine2(@Nullable String line2) {
        set(conf -> conf.line2(line2));
    }

    public void setFadeIn(int fadeIn) {
        set(conf -> conf.fadeIn(fadeIn));
    }

    public void setStay(int stay) {
        set(conf -> conf.stay(stay));
    }

    public void setFadeOut(int fadeOut) {
        set(conf -> conf.fadeOut(fadeOut));
    }

    public void send(@NotNull Player player, Object... values) {
        send(player, this.params, values);
    }

    public void send(@NotNull Player player, @NotNull String[] params, @NotNull Object[] values) {
        send(player, TextParser.buildParams(paramBuilder, params, values));
    }

    public void send(@NotNull Player player, @NotNull Map<String, Object> placeholders) {
        TitleConfig config = get();
        if (config != null) config.send(player, placeholders, sendConsumer);
    }

    public void sendToAll(Object... values) {
        sendToAll(this.params, values);
    }

    public void sendToAll(@NotNull String[] params, @NotNull Object[] values) {
        sendToAll(TextParser.buildParams(paramBuilder, params, values));
    }

    public void sendToAll(@NotNull Map<String, Object> placeholders) {
        TitleConfig config = get();
        if (config == null) return;

        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> send(onlinePlayer, placeholders));
    }

    public void sendToEach(@NotNull Function<@NotNull Player, Object[]> eachValues) {
        sendToEach(null, eachValues);
    }

    public void sendToEach(@Nullable Predicate<Player> limiter,
                           @NotNull Function<@NotNull Player, Object[]> eachValues) {
        Predicate<Player> predicate = Optional.ofNullable(limiter).orElse(r -> true);
        Bukkit.getOnlinePlayers().stream().filter(predicate)
                .forEach(r -> send(r, eachValues.apply(r)));
    }

    @FunctionalInterface
    public interface TitleConsumer {

        /**
         * 向目标玩家发送标题文字
         *
         * @param player  目标玩家
         * @param fadeIn  淡入时间 (ticks)
         * @param stay    保留时间 (ticks)
         * @param fadeOut 淡出时间 (ticks)
         * @param line1   第一行文字
         * @param line2   第二行文字
         */
        void send(@NotNull Player player,
                  @Range(from = 0L, to = Integer.MAX_VALUE) int fadeIn,
                  @Range(from = 0L, to = Integer.MAX_VALUE) int stay,
                  @Range(from = 0L, to = Integer.MAX_VALUE) int fadeOut,
                  @NotNull String line1, @NotNull String line2);

    }

    public static class Builder extends AbstractConfigBuilder<TitleConfig, TitleConfig, ConfiguredTitle, ConfigurationHolder<?>, Builder> {

        protected @NotNull ValueAdapter<TitleConfig> adapter = TITLE_ADAPTER;
        protected @NotNull TitleConfig title = TitleConfig.of(null, null);
        protected @NotNull String[] params = new String[0];

        protected @NotNull ConfiguredTitle.TitleConsumer sendConsumer = DEFAULT_TITLE_CONSUMER;
        protected @NotNull UnaryOperator<String> paramFormatter = s -> "%(" + s + ")";

        protected Builder() {
            super(ConfigurationHolder.class, TITLE_TYPE);
            defaults(() -> this.title);
        }

        public @NotNull Builder adapter(@NotNull ValueAdapter<TitleConfig> adapter) {
            this.adapter = adapter;
            return this;
        }

        public @NotNull Builder defaults(Consumer<TitleConfig> handler) {
            handler.accept(this.title);
            return this;
        }

        public @NotNull Builder defaults(@Nullable String line1, @Nullable String line2) {
            return line1(line1).line2(line2);
        }

        public @NotNull Builder line1(@Nullable String line1) {
            return defaults(t -> t.line1(line1));
        }

        public @NotNull Builder line2(@Nullable String line2) {
            return defaults(t -> t.line2(line2));
        }

        public @NotNull Builder fadeIn(@Range(from = 0L, to = Integer.MAX_VALUE) int fadeIn) {
            return defaults(t -> t.fadeIn(fadeIn));
        }

        public @NotNull Builder stay(@Range(from = 0L, to = Integer.MAX_VALUE) int stay) {
            return defaults(t -> t.stay(stay));
        }

        public @NotNull Builder fadeOut(@Range(from = 0L, to = Integer.MAX_VALUE) int fadeOut) {
            return defaults(t -> t.fadeOut(fadeOut));
        }

        public @NotNull Builder params(String... params) {
            this.params = params;
            return this;
        }

        public @NotNull Builder params(@NotNull List<String> params) {
            return params(params.toArray(new String[0]));
        }

        public @NotNull Builder whenSend(@NotNull ConfiguredTitle.TitleConsumer consumer) {
            this.sendConsumer = consumer;
            return this;
        }

        @Override
        protected @NotNull Builder self() {
            return this;
        }

        @Override
        public @NotNull ConfiguredTitle build() {
            return new ConfiguredTitle(buildManifest(), this.adapter, this.paramFormatter, this.params, this.sendConsumer);
        }
    }

}
