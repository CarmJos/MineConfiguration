package cc.carm.lib.configuration.minecraft.bukkit.builder.sound;

import cc.carm.lib.configuration.minecraft.bukkit.builder.AbstractCraftBuilder;
import cc.carm.lib.configuration.minecraft.bukkit.data.SoundConfig;
import cc.carm.lib.configuration.minecraft.bukkit.value.ConfiguredSound;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;

public class SoundConfigBuilder extends AbstractCraftBuilder<SoundConfig, SoundConfigBuilder> {


    public @NotNull SoundConfigBuilder defaults(@NotNull Sound sound, float volume, float pitch) {
        return defaults(new SoundConfig(sound.name(), sound, volume, pitch));
    }

    public @NotNull SoundConfigBuilder defaults(@NotNull Sound sound, float volume) {
        return defaults(sound, volume, 1.0f);
    }

    public @NotNull SoundConfigBuilder defaults(@NotNull Sound sound) {
        return defaults(sound, 1.0f);
    }

    public @NotNull SoundConfigBuilder defaults(@NotNull String soundName, float volume, float pitch) {
        return defaults(new SoundConfig(soundName, volume, pitch));
    }

    public @NotNull SoundConfigBuilder defaults(@NotNull String soundName, float volume) {
        return defaults(soundName, volume, 1.0f);
    }

    public @NotNull SoundConfigBuilder defaults(@NotNull String soundName) {
        return defaults(soundName, 1.0f);
    }

    @Override
    protected @NotNull SoundConfigBuilder getThis() {
        return this;
    }

    @Override
    public @NotNull ConfiguredSound build() {
        return new ConfiguredSound(this.provider, this.path, this.headerComments, this.inlineComment, this.defaultValue);
    }

}
