package cc.carm.lib.mineconfiguration.bukkit.builder;

import cc.carm.lib.mineconfiguration.bukkit.source.CraftConfigProvider;
import cc.carm.lib.configuration.core.builder.AbstractConfigBuilder;

public abstract class AbstractCraftBuilder<T, B extends AbstractCraftBuilder<T, B>>
        extends AbstractConfigBuilder<T, B, CraftConfigProvider> {

    public AbstractCraftBuilder() {
        super(CraftConfigProvider.class);
    }

}
