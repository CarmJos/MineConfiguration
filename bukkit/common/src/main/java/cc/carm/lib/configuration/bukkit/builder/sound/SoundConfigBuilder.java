package cc.carm.lib.configuration.bukkit.builder.sound;

import cc.carm.lib.configuration.bukkit.builder.AbstractBukkitBuilder;
import cc.carm.lib.configuration.bukkit.data.SoundConfig;
import cc.carm.lib.configuration.bukkit.value.ConfiguredSound;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;

public class SoundConfigBuilder extends AbstractBukkitBuilder<SoundConfig, SoundConfigBuilder> {


    public @NotNull SoundConfigBuilder defaults(@NotNull Sound sound, float volume, float pitch) {
        return defaults(new SoundConfig(sound, volume, pitch));
    }

    public @NotNull SoundConfigBuilder defaults(@NotNull Sound sound, float volume) {
        return defaults(sound, volume, 1.0f);
    }

    public @NotNull SoundConfigBuilder defaults(@NotNull Sound sound) {
        return defaults(sound, 1.0f);
    }

    @Override
    protected @NotNull SoundConfigBuilder getThis() {
        return this;
    }

    @Override
    public @NotNull ConfiguredSound build() {
        return new ConfiguredSound(this.provider, this.path, this.comments, this.defaultValue);
    }

}
