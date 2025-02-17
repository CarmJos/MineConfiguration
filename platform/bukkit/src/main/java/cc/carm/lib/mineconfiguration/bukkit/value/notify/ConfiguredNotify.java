package cc.carm.lib.mineconfiguration.bukkit.value.notify;

import cc.carm.lib.configuration.adapter.ValueAdapter;
import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.builder.AbstractConfigBuilder;
import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.configuration.value.ValueManifest;
import cc.carm.lib.configuration.value.standard.ConfiguredList;
import cc.carm.lib.configuration.value.text.function.TextParser;
import cc.carm.lib.mineconfiguration.bukkit.data.NotifyConfig;
import cc.carm.lib.mineconfiguration.bukkit.data.SoundConfig;
import cc.carm.lib.mineconfiguration.bukkit.data.TitleConfig;
import cc.carm.lib.mineconfiguration.bukkit.value.notify.type.NotifyType;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;

public class ConfiguredNotify extends ConfiguredList<NotifyConfig> {

    public static NotifyBuilder create() {
        return new NotifyBuilder();
    }

    public static final ValueType<NotifyConfig> NOTIFY_TYPE = ValueType.of(NotifyConfig.class);
    public static final ValueAdapter<NotifyConfig> NOTIFY_ADAPTER = new ValueAdapter<>(NOTIFY_TYPE,
            (holder, type, value) -> value.serialize(),
            (holder, type, value) -> {
                String conf = holder.deserialize(String.class, value);
                return NotifyConfig.deserialize(conf);
            }
    );

    protected final @NotNull UnaryOperator<String> paramBuilder;
    protected final @NotNull String[] params;

    public ConfiguredNotify(@NotNull ValueManifest<List<NotifyConfig>> manifest,
                            @NotNull ValueAdapter<NotifyConfig> adapter,
                            @NotNull UnaryOperator<String> paramBuilder, @NotNull String[] params) {
        super(manifest, ArrayList::new, adapter);
        this.paramBuilder = paramBuilder;
        this.params = params;
    }

    public @NotNull PreparedNotify prepare(@NotNull Object... values) {
        return new PreparedNotify(getNotNull(), TextParser.buildParams(paramBuilder, this.params, values));
    }

    public void send(@NotNull Player player, @NotNull Object... values) {
        prepare(values).to(player);
    }

    public void send(@NotNull Iterable<? extends Player> players, @NotNull Object... values) {
        prepare(values).to(players);
    }

    public void sendAll(@NotNull Object... values) {
        prepare(values).toAll();
    }


    public static class NotifyBuilder extends AbstractConfigBuilder<List<NotifyConfig>, ConfiguredNotify, ConfigurationHolder<?>, NotifyBuilder> {

        protected final @NotNull List<NotifyConfig> notifications = new ArrayList<>();

        protected @NotNull String[] params = new String[0];
        protected @NotNull ValueAdapter<NotifyConfig> adapter = NOTIFY_ADAPTER;
        protected @NotNull UnaryOperator<String> paramFormatter = s -> "%(" + s + ")";

        protected NotifyBuilder() {
            super(ConfigurationHolder.class, new ValueType<List<NotifyConfig>>() {
            });
        }

        @Override
        protected @NotNull ConfiguredNotify.NotifyBuilder self() {
            return this;
        }

        @Override
        public @NotNull ConfiguredNotify build() {
            return new ConfiguredNotify(buildManifest(), this.adapter, this.paramFormatter, this.params);
        }

        public NotifyBuilder adapter(@NotNull ValueAdapter<NotifyConfig> adapter) {
            this.adapter = adapter;
            return this;
        }

        public NotifyBuilder defaultMessages(@NotNull String... messages) {
            return defaultMessages(Arrays.asList(messages));
        }

        public NotifyBuilder defaultMessages(@NotNull List<String> messages) {
            for (String message : messages) {
                notifications.add(NotifyConfig.of(NotifyType.Standard.MESSAGE, message));
            }
            return defaults(this.notifications);
        }

        public NotifyBuilder defaultActionBar(@NotNull String message) {
            notifications.add(NotifyConfig.of(NotifyType.Standard.ACTIONBAR, message));
            return defaults(this.notifications);
        }

        public NotifyBuilder defaultSound(@NotNull Sound sound, float volume, float pitch) {
            return defaultSound(sound.name(), volume, pitch);
        }

        public NotifyBuilder defaultSound(@NotNull Sound sound, float volume) {
            return defaultSound(sound, volume, 1.0f);
        }

        public NotifyBuilder defaultSound(@NotNull Sound sound) {
            return defaultSound(sound, 1.0f);
        }

        public NotifyBuilder defaultSound(@NotNull String soundName, float volume, float pitch) {
            notifications.add(NotifyConfig.of(NotifyType.Standard.SOUND, new SoundConfig(soundName, volume, pitch)));
            return defaults(this.notifications);
        }

        public NotifyBuilder defaultSound(@NotNull String soundName, float volume) {
            return defaultSound(soundName, volume, 1.0f);
        }

        public NotifyBuilder defaultSound(@NotNull String soundName) {
            return defaultSound(soundName, 1.0f);
        }

        public NotifyBuilder defaultTitle(@Nullable String line1, @Nullable String line2,
                                          @Range(from = 0L, to = Integer.MAX_VALUE) int fadeIn,
                                          @Range(from = 0L, to = Integer.MAX_VALUE) int stay,
                                          @Range(from = 0L, to = Integer.MAX_VALUE) int fadeOut) {
            notifications.add(NotifyConfig.of(NotifyType.Standard.TITLE, TitleConfig.of(line1, line2, fadeIn, stay, fadeOut)));
            return defaults(this.notifications);
        }

        public NotifyBuilder defaultTitle(@Nullable String line1, @Nullable String line2) {
            return defaultTitle(line1, line2, 10, 60, 10);
        }

        public NotifyBuilder params(String... params) {
            this.params = params;
            return this;
        }

        public NotifyBuilder params(@NotNull List<String> params) {
            return params(params.toArray(new String[0]));
        }

        public NotifyBuilder formatParam(UnaryOperator<String> paramFormatter) {
            this.paramFormatter = paramFormatter;
            return this;
        }
    }

}
