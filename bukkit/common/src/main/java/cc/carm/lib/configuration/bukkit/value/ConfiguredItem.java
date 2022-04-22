package cc.carm.lib.configuration.bukkit.value;

import cc.carm.lib.configuration.bukkit.data.ItemConfig;
import cc.carm.lib.configuration.core.function.ConfigDataFunction;
import cc.carm.lib.configuration.core.function.ConfigValueParser;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import cc.carm.lib.configuration.core.value.type.ConfiguredSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ConfiguredItem extends ConfiguredSection<ItemConfig> {

    public ConfiguredItem(@Nullable ConfigurationProvider<?> provider,
                          @Nullable String sectionPath, @NotNull String[] comments,
                          @NotNull Class<ItemConfig> valueClass, @Nullable ItemConfig defaultValue,
                          @NotNull ConfigValueParser<ConfigurationWrapper, ItemConfig> parser,
                          @NotNull ConfigDataFunction<ItemConfig, ? extends Map<String, Object>> serializer) {
        super(provider, sectionPath, comments, valueClass, defaultValue, parser, serializer);
    }

}
