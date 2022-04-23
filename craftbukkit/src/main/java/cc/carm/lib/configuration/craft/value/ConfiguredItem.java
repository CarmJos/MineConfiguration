package cc.carm.lib.configuration.craft.value;

import cc.carm.lib.configuration.core.function.ConfigValueParser;
import cc.carm.lib.configuration.core.source.ConfigCommentInfo;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import cc.carm.lib.configuration.core.value.type.ConfiguredSection;
import cc.carm.lib.configuration.craft.data.ItemConfig;
import org.jetbrains.annotations.Nullable;

public class ConfiguredItem extends ConfiguredSection<ItemConfig> {

    public ConfiguredItem(@Nullable ConfigurationProvider<?> provider,
                          @Nullable String sectionPath, @Nullable ConfigCommentInfo comments,
                          @Nullable ItemConfig defaultValue) {
        super(provider, sectionPath, comments, ItemConfig.class, defaultValue, getItemParser(), ItemConfig::serialize);
    }

    public static ConfigValueParser<ConfigurationWrapper, ItemConfig> getItemParser() {
        return (s, d) -> ItemConfig.deserialize(s);
    }

}
