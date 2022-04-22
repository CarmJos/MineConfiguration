package cc.carm.lib.configuration.bukkit.builder;

import cc.carm.lib.configuration.bukkit.builder.serializable.SerializableBuilder;
import cc.carm.lib.configuration.bukkit.builder.sound.SoundConfigBuilder;
import cc.carm.lib.configuration.core.builder.ConfigBuilder;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

public class BukkitConfigBuilder extends ConfigBuilder {

    public @NotNull SoundConfigBuilder createSound() {
        return new SoundConfigBuilder();
    }

    public <V extends ConfigurationSerializable> @NotNull SerializableBuilder<V> ofSerializable(@NotNull Class<V> valueClass) {
        return new SerializableBuilder<>(valueClass);
    }

}
