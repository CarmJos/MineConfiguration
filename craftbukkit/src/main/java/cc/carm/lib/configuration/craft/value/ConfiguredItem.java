package cc.carm.lib.configuration.craft.value;

import cc.carm.lib.configuration.core.function.ConfigValueParser;
import cc.carm.lib.configuration.core.source.ConfigCommentInfo;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import cc.carm.lib.configuration.core.value.type.ConfiguredSection;
import cc.carm.lib.configuration.craft.CraftConfigValue;
import cc.carm.lib.configuration.craft.builder.item.ItemConfigBuilder;
import cc.carm.lib.configuration.craft.data.ItemConfig;
import org.jetbrains.annotations.Nullable;

public class ConfiguredItem extends ConfiguredSection<ItemConfig> {

    public static ItemConfigBuilder create() {
        return CraftConfigValue.builder().createItem();
    }

    public static ConfiguredItem of() {
        return CraftConfigValue.builder().ofItem();
    }

    public static ConfiguredItem of(@Nullable ItemConfig defaultItem) {
        return CraftConfigValue.builder().ofItem(defaultItem);
    }

    public ConfiguredItem(@Nullable ConfigurationProvider<?> provider,
                          @Nullable String sectionPath, @Nullable ConfigCommentInfo comments,
                          @Nullable ItemConfig defaultValue) {
        super(provider, sectionPath, comments, ItemConfig.class, defaultValue, getItemParser(), ItemConfig::serialize);
    }

    public static ConfigValueParser<ConfigurationWrapper, ItemConfig> getItemParser() {
        return (s, d) -> ItemConfig.deserialize(s);
    }

}
