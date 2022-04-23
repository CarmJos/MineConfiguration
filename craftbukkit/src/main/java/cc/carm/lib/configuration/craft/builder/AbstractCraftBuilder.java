package cc.carm.lib.configuration.craft.builder;

import cc.carm.lib.configuration.craft.source.CraftConfigProvider;
import cc.carm.lib.configuration.core.builder.AbstractConfigBuilder;

public abstract class AbstractCraftBuilder<T, B extends AbstractCraftBuilder<T, B>>
        extends AbstractConfigBuilder<T, B, CraftConfigProvider> {

    public AbstractCraftBuilder() {
        super(CraftConfigProvider.class);
    }

}
