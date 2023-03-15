package cc.carm.lib.mineconfiguration.bukkit.builder.serializable;

import cc.carm.lib.mineconfiguration.bukkit.builder.AbstractCraftBuilder;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

public class SerializableBuilder<T extends ConfigurationSerializable>
        extends AbstractCraftBuilder<T, SerializableBuilder<T>> {

    protected final @NotNull Class<T> valueClass;

    public SerializableBuilder(@NotNull Class<T> valueClass) {
        this.valueClass = valueClass;
    }

    @Override
    protected @NotNull SerializableBuilder<T> getThis() {
        return this;
    }

    @Override
    public @NotNull ConfiguredSerializable<T> build() {
        return new ConfiguredSerializable<>(buildManifest(), valueClass);
    }

}

