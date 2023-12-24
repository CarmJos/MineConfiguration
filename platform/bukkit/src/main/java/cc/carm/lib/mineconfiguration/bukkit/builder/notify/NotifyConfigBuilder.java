package cc.carm.lib.mineconfiguration.bukkit.builder.notify;

import cc.carm.lib.configuration.core.builder.CommonConfigBuilder;
import cc.carm.lib.mineconfiguration.bukkit.data.NotifyConfig;
import cc.carm.lib.mineconfiguration.bukkit.data.SoundConfig;
import cc.carm.lib.mineconfiguration.bukkit.data.TitleConfig;
import cc.carm.lib.mineconfiguration.bukkit.value.notify.ConfiguredNotify;
import cc.carm.lib.mineconfiguration.bukkit.value.notify.DefaultNotifyTypes;
import cc.carm.lib.mineconfiguration.common.utils.ParamsUtils;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class NotifyConfigBuilder extends CommonConfigBuilder<List<NotifyConfig>, NotifyConfigBuilder> {

    protected final @NotNull List<NotifyConfig> notifications = new ArrayList<>();

    protected @NotNull String[] params = new String[0];
    protected @NotNull Function<@NotNull String, @NotNull String> paramFormatter;

    public NotifyConfigBuilder() {
        this.paramFormatter = ParamsUtils.DEFAULT_PARAM_FORMATTER;
    }

    public NotifyConfigBuilder defaultMessages(@NotNull String... messages) {
        return defaultMessages(Arrays.asList(messages));
    }

    public NotifyConfigBuilder defaultMessages(@NotNull List<String> messages) {
        for (String message : messages) {
            notifications.add(NotifyConfig.of(DefaultNotifyTypes.MESSAGE, message));
        }
        return defaults(this.notifications);
    }

    public NotifyConfigBuilder defaultActionBar(@NotNull String message) {
        notifications.add(NotifyConfig.of(DefaultNotifyTypes.ACTIONBAR, message));
        return defaults(this.notifications);
    }

    public NotifyConfigBuilder defaultSound(@NotNull Sound sound, float volume, float pitch) {
        return defaultSound(sound.name(), volume, pitch);
    }

    public NotifyConfigBuilder defaultSound(@NotNull Sound sound, float volume) {
        return defaultSound(sound, volume, 1.0f);
    }

    public NotifyConfigBuilder defaultSound(@NotNull Sound sound) {
        return defaultSound(sound, 1.0f);
    }

    public NotifyConfigBuilder defaultSound(@NotNull String soundName, float volume, float pitch) {
        notifications.add(NotifyConfig.of(DefaultNotifyTypes.SOUND, new SoundConfig(soundName, volume, pitch)));
        return defaults(this.notifications);
    }

    public NotifyConfigBuilder defaultSound(@NotNull String soundName, float volume) {
        return defaultSound(soundName, volume, 1.0f);
    }

    public NotifyConfigBuilder defaultSound(@NotNull String soundName) {
        return defaultSound(soundName, 1.0f);
    }

    public NotifyConfigBuilder defaultTitle(@Nullable String line1, @Nullable String line2,
                                            @Range(from = 0L, to = Integer.MAX_VALUE) int fadeIn,
                                            @Range(from = 0L, to = Integer.MAX_VALUE) int stay,
                                            @Range(from = 0L, to = Integer.MAX_VALUE) int fadeOut) {
        notifications.add(NotifyConfig.of(DefaultNotifyTypes.TITLE, TitleConfig.of(line1, line2, fadeIn, stay, fadeOut)));
        return defaults(this.notifications);
    }

    public NotifyConfigBuilder defaultTitle(@Nullable String line1, @Nullable String line2) {
        return defaultTitle(line1, line2, 10, 60, 10);
    }

    public NotifyConfigBuilder params(String... params) {
        this.params = params;
        return this;
    }

    public NotifyConfigBuilder params(@NotNull List<String> params) {
        return params(params.toArray(new String[0]));
    }


    public NotifyConfigBuilder formatParam(UnaryOperator<String> paramFormatter) {
        this.paramFormatter = paramFormatter;
        return this;
    }

    @Override
    protected @NotNull NotifyConfigBuilder getThis() {
        return this;
    }

    @Override
    public @NotNull ConfiguredNotify build() {
        return new ConfiguredNotify(buildManifest(), ParamsUtils.formatParams(this.paramFormatter, this.params));
    }

}
