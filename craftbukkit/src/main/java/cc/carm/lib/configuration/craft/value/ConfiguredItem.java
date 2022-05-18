package cc.carm.lib.configuration.craft.value;

import cc.carm.lib.configuration.common.utils.ParamsUtils;
import cc.carm.lib.configuration.core.function.ConfigValueParser;
import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import cc.carm.lib.configuration.core.value.type.ConfiguredSection;
import cc.carm.lib.configuration.craft.CraftConfigValue;
import cc.carm.lib.configuration.craft.builder.item.ItemConfigBuilder;
import cc.carm.lib.configuration.craft.data.ItemConfig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    protected final @NotNull String[] params;

    public ConfiguredItem(@Nullable ConfigurationProvider<?> provider, @Nullable String sectionPath,
                          @Nullable List<String> headerComments, @Nullable String inlineComments,
                          @Nullable ItemConfig defaultValue, @NotNull String[] params) {
        super(provider, sectionPath, headerComments, inlineComments, ItemConfig.class, defaultValue, getItemParser(), ItemConfig::serialize);
        this.params = params;
    }

    public static ConfigValueParser<ConfigurationWrapper, ItemConfig> getItemParser() {
        return (s, d) -> ItemConfig.deserialize(s);
    }

    public @NotNull String[] getParams() {
        return params;
    }

    public @Nullable ItemStack getItem(@Nullable Player player) {
        return getItem(player, 1);
    }

    public @Nullable ItemStack getItem(@Nullable Player player, int amount) {
        return getItem(player, amount, new HashMap<>());
    }

    public @Nullable ItemStack getItem(@Nullable Player player, @NotNull Object... values) {
        return getItem(player, 1, values);
    }

    public @Nullable ItemStack getItem(@Nullable Player player, int amount, @NotNull Object... values) {
        return getItem(player, amount, ParamsUtils.buildParams(params, values));
    }

    public @Nullable ItemStack getItem(@Nullable Player player, int amount, @NotNull String[] params, @NotNull Object[] values) {
        return getItem(player, amount, ParamsUtils.buildParams(params, values));
    }

    public @Nullable ItemStack getItem(@Nullable Player player, int amount, @NotNull Map<String, Object> placeholders) {
        return getOptional().map(item -> item.getItemStack(player, amount, placeholders)).orElse(null);
    }

}
