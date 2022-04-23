package cc.carm.lib.configuration.craft.builder.sound;

import cc.carm.lib.configuration.craft.builder.AbstractCraftBuilder;
import cc.carm.lib.configuration.craft.data.SoundConfig;
import cc.carm.lib.configuration.craft.value.ConfiguredSound;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;

public class SoundConfigBuilder extends AbstractCraftBuilder<SoundConfig, SoundConfigBuilder> {


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
        return new ConfiguredSound(this.provider, this.path, buildComments(), this.defaultValue);
    }

}
