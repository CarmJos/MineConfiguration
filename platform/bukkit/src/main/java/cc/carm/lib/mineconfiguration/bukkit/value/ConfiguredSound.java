package cc.carm.lib.mineconfiguration.bukkit.value;


import cc.carm.lib.configuration.adapter.ValueAdapter;
import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.builder.AbstractConfigBuilder;
import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.configuration.value.ValueManifest;
import cc.carm.lib.configuration.value.standard.ConfiguredValue;
import cc.carm.lib.mineconfiguration.bukkit.data.SoundConfig;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ConfiguredSound extends ConfiguredValue<SoundConfig> {

    public static @NotNull Builder create() {
        return new Builder();
    }

    public static @NotNull ConfiguredSound of(Sound sound) {
        return create().defaults(sound).build();
    }

    public static @NotNull ConfiguredSound of(Sound sound, float volume) {
        return create().defaults(sound, volume).build();
    }

    public static @NotNull ConfiguredSound of(Sound sound, float volume, float pitch) {
        return create().defaults(sound, volume, pitch).build();
    }

    public static @NotNull ConfiguredSound of(String soundName) {
        return create().defaults(soundName).build();
    }

    public static @NotNull ConfiguredSound of(String soundName, float volume) {
        return create().defaults(soundName, volume).build();
    }

    public static @NotNull ConfiguredSound of(String soundName, float volume, float pitch) {
        return create().defaults(soundName, volume, pitch).build();
    }


    public static final ValueType<SoundConfig> SOUND_TYPE = ValueType.of(SoundConfig.class);
    public static final ValueAdapter<SoundConfig> SOUND_ADAPTER = new ValueAdapter<>(SOUND_TYPE,
            (holder, type, value) -> value.serialize(),
            (holder, type, value) -> {
                String conf = holder.deserialize(String.class, value);
                return SoundConfig.deserialize(conf);
            }
    );

    public ConfiguredSound(@NotNull ValueManifest<SoundConfig> manifest, @NotNull ValueAdapter<SoundConfig> adapter) {
        super(manifest, adapter);
    }

    public void set(@NotNull Sound sound) {
        set(sound, 1.0f);
    }

    public void set(@NotNull Sound sound, float volume) {
        set(sound, volume, 1.0f);
    }

    public void set(@NotNull Sound sound, float volume, float pitch) {
        set(new SoundConfig(sound.name(), sound, volume, pitch));
    }

    public void playTo(@NotNull Player player) {
        Optional.ofNullable(get()).ifPresent(c -> c.playTo(player));
    }

    public void playToAll() {
        Optional.ofNullable(get()).ifPresent(SoundConfig::playToAll);
    }

    public void playAt(Location location) {
        Optional.ofNullable(get()).ifPresent(s -> s.playAt(location));
    }

    public static class Builder extends AbstractConfigBuilder<SoundConfig, ConfiguredSound, ConfigurationHolder<?>, Builder> {

        protected @NotNull ValueAdapter<SoundConfig> adapter = SOUND_ADAPTER;

        protected Builder() {
            super(ConfigurationHolder.class, SOUND_TYPE);
        }

        public @NotNull Builder adapter(@NotNull ValueAdapter<SoundConfig> adapter) {
            this.adapter = adapter;
            return this;
        }

        public @NotNull Builder defaults(@NotNull Sound sound, float volume, float pitch) {
            return defaults(new SoundConfig(sound.name(), sound, volume, pitch));
        }

        public @NotNull Builder defaults(@NotNull Sound sound, float volume) {
            return defaults(sound, volume, 1.0f);
        }

        public @NotNull Builder defaults(@NotNull Sound sound) {
            return defaults(sound, 1.0f);
        }

        public @NotNull Builder defaults(@NotNull String soundName, float volume, float pitch) {
            return defaults(new SoundConfig(soundName, volume, pitch));
        }

        public @NotNull Builder defaults(@NotNull String soundName, float volume) {
            return defaults(soundName, volume, 1.0f);
        }

        public @NotNull Builder defaults(@NotNull String soundName) {
            return defaults(soundName, 1.0f);
        }

        @Override
        protected @NotNull Builder self() {
            return this;
        }

        @Override
        public @NotNull ConfiguredSound build() {
            return new ConfiguredSound(buildManifest(), this.adapter);
        }
    }


}
