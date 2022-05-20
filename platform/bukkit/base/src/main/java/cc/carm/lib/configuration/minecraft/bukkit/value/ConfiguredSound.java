package cc.carm.lib.configuration.minecraft.bukkit.value;

import cc.carm.lib.configuration.core.function.ConfigValueParser;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;
import cc.carm.lib.configuration.minecraft.bukkit.CraftConfigValue;
import cc.carm.lib.configuration.minecraft.bukkit.builder.sound.SoundConfigBuilder;
import cc.carm.lib.configuration.minecraft.bukkit.data.SoundConfig;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class ConfiguredSound extends ConfiguredValue<SoundConfig> {

    public static @NotNull SoundConfigBuilder create() {
        return CraftConfigValue.builder().createSound();
    }

    public static @NotNull ConfiguredSound of(Sound sound) {
        return CraftConfigValue.builder().createSound().defaults(sound).build();
    }

    public static @NotNull ConfiguredSound of(Sound sound, float volume) {
        return CraftConfigValue.builder().createSound().defaults(sound, volume).build();
    }

    public static @NotNull ConfiguredSound of(Sound sound, float volume, float pitch) {
        return CraftConfigValue.builder().createSound().defaults(sound, volume, pitch).build();
    }

    public static @NotNull ConfiguredSound of(String soundName) {
        return CraftConfigValue.builder().createSound().defaults(soundName).build();
    }

    public static @NotNull ConfiguredSound of(String soundName, float volume) {
        return CraftConfigValue.builder().createSound().defaults(soundName, volume).build();
    }

    public static @NotNull ConfiguredSound of(String soundName, float volume, float pitch) {
        return CraftConfigValue.builder().createSound().defaults(soundName, volume, pitch).build();
    }

    public ConfiguredSound(@Nullable ConfigurationProvider<?> provider, @Nullable String sectionPath,
                           @Nullable List<String> headerComments, @Nullable String inlineComments,
                           @Nullable SoundConfig defaultValue) {
        super(provider, sectionPath, headerComments, inlineComments, SoundConfig.class, defaultValue, getSoundParser(), SoundConfig::serialize);
    }

    public void setSound(@NotNull Sound sound) {
        setSound(sound, 1.0f);
    }

    public void setSound(@NotNull Sound sound, float volume) {
        setSound(sound, volume, 1.0f);
    }

    public void setSound(@NotNull Sound sound, float volume, float pitch) {
        set(new SoundConfig(sound.name(), sound, volume, pitch));
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
