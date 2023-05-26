package cc.carm.lib.mineconfiguration.bukkit.builder;

import cc.carm.lib.configuration.core.builder.ConfigBuilder;
import cc.carm.lib.mineconfiguration.bukkit.builder.item.ItemConfigBuilder;
import cc.carm.lib.mineconfiguration.bukkit.builder.message.CraftMessageBuilder;
import cc.carm.lib.mineconfiguration.bukkit.builder.serializable.SerializableBuilder;
import cc.carm.lib.mineconfiguration.bukkit.builder.sound.SoundConfigBuilder;
import cc.carm.lib.mineconfiguration.bukkit.builder.title.TitleConfigBuilder;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

public class CraftConfigBuilder extends ConfigBuilder {

    public @NotNull SoundConfigBuilder createSound() {
        return new SoundConfigBuilder();
    }

    public @NotNull ItemConfigBuilder createItem() {
        return new ItemConfigBuilder();
    }

    public @NotNull CraftMessageBuilder createMessage() {
        return new CraftMessageBuilder();
    }

    public @NotNull TitleConfigBuilder createTitle() {
        return new TitleConfigBuilder();
    }

    public <V extends ConfigurationSerializable> @NotNull SerializableBuilder<V> ofSerializable(@NotNull Class<V> valueClass) {
        return new SerializableBuilder<>(valueClass);
    }

}
