package cc.carm.lib.mineconfiguration.bungee.builder;

import cc.carm.lib.mineconfiguration.bungee.source.BungeeConfigProvider;
import cc.carm.lib.configuration.core.builder.AbstractConfigBuilder;

public abstract class AbstractBungeeBuilder<T, B extends AbstractBungeeBuilder<T, B>>
        extends AbstractConfigBuilder<T, B, BungeeConfigProvider> {

    public AbstractBungeeBuilder() {
        super(BungeeConfigProvider.class);
    }

}
