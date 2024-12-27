package cc.carm.lib.mineconfiguration.velocity.builder;

import cc.carm.lib.mineconfiguration.velocity.source.BungeeConfigProvider;
import cc.carm.lib.configuration.core.builder.AbstractConfigBuilder;

public abstract class AbstractVelocityBuilder<T, B extends AbstractVelocityBuilder<T, B>>
        extends AbstractConfigBuilder<T, B, BungeeConfigProvider> {

    public AbstractVelocityBuilder() {
        super(BungeeConfigProvider.class);
    }

}
