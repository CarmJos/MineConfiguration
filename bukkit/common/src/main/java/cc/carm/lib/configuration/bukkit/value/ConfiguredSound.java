package cc.carm.lib.configuration.bukkit.value;

import cc.carm.lib.configuration.bukkit.BukkitConfigValue;
import cc.carm.lib.configuration.bukkit.data.SoundConfig;
import cc.carm.lib.configuration.core.function.ConfigValueParser;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ConfiguredSound extends ConfiguredValue<SoundConfig> {

    public static @NotNull ConfiguredSound of(Sound sound) {
        return BukkitConfigValue.builder().createSound().defaults(sound).build();
    }

    public static @NotNull ConfiguredSound of(Sound sound, float volume) {
        return BukkitConfigValue.builder().createSound().defaults(sound, volume).build();
    }

    public static @NotNull ConfiguredSound of(Sound sound, float volume, float pitch) {
        return BukkitConfigValue.builder().createSound().defaults(sound, volume, pitch).build();
    }


    public ConfiguredSound(@Nullable ConfigurationProvider<?> provider,
                           @Nullable String sectionPath, @NotNull String[] comments,
                           @Nullable SoundConfig defaultValue) {
        super(provider, sectionPath, comments, SoundConfig.class, defaultValue, getSoundParser(), SoundConfig::serialize);
    }

    public void playTo(@NotNull Player player) {
        Optional.ofNullable(get()).ifPresent(c -> c.playTo(player));
    }

    public void playToAll() {
        Optional.ofNullable(get()).ifPresent(SoundConfig::playToAll);
    }

    public static ConfigValueParser<Object, SoundConfig> getSoundParser() {
        return ConfigValueParser.castToString().andThen((s, d) -> SoundConfig.deserialize(s));
    }

}
