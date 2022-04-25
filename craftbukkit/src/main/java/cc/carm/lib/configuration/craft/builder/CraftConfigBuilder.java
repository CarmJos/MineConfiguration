package cc.carm.lib.configuration.craft.builder;

import cc.carm.lib.configuration.core.builder.ConfigBuilder;
import cc.carm.lib.configuration.craft.builder.item.ItemConfigBuilder;
import cc.carm.lib.configuration.craft.builder.message.MessageConfigBuilder;
import cc.carm.lib.configuration.craft.builder.serializable.SerializableBuilder;
import cc.carm.lib.configuration.craft.builder.sound.SoundConfigBuilder;
import cc.carm.lib.configuration.craft.data.ItemConfig;
import cc.carm.lib.configuration.craft.value.ConfiguredItem;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CraftConfigBuilder extends ConfigBuilder {

    public @NotNull SoundConfigBuilder createSound() {
        return new SoundConfigBuilder();
    }

    public @NotNull ItemConfigBuilder createItem() {
        return new ItemConfigBuilder();
    }

    public @NotNull MessageConfigBuilder createMessage() {
        return new MessageConfigBuilder();
    }

    public <V extends ConfigurationSerializable> @NotNull SerializableBuilder<V> ofSerializable(@NotNull Class<V> valueClass) {
        return new SerializableBuilder<>(valueClass);
    }

    public @NotNull ConfiguredItem ofItem() {
        return createItem().build();
    }

    public @NotNull ConfiguredItem ofItem(@Nullable ItemConfig defaultItem) {
        return createItem().defaults(defaultItem).build();
    }

}
