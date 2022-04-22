package cc.carm.lib.configuration.bukkit.builder;

import cc.carm.lib.configuration.bukkit.source.BukkitConfigProvider;
import cc.carm.lib.configuration.core.builder.AbstractConfigBuilder;

public abstract class AbstractBukkitBuilder<T, B extends AbstractBukkitBuilder<T, B>>
        extends AbstractConfigBuilder<T, B, BukkitConfigProvider<?, ?>> {

    public AbstractBukkitBuilder() {
        super(BukkitConfigProvider.class);
    }

}
