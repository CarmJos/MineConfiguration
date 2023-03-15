package cc.carm.lib.mineconfiguration.bukkit.value;

import cc.carm.lib.configuration.core.function.ConfigValueParser;
import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import cc.carm.lib.configuration.core.value.ValueManifest;
import cc.carm.lib.configuration.core.value.type.ConfiguredSection;
import cc.carm.lib.mineconfiguration.bukkit.CraftConfigValue;
import cc.carm.lib.mineconfiguration.bukkit.builder.item.ItemConfigBuilder;
import cc.carm.lib.mineconfiguration.bukkit.data.ItemConfig;
import cc.carm.lib.mineconfiguration.common.utils.ParamsUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
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

    public ConfiguredItem(@NotNull ValueManifest<ItemConfig> manifest, @NotNull String[] params) {
        super(
                manifest, ItemConfig.class,
                getItemParser(), ItemConfig::serialize
        );
        this.params = params;
    }

    public static ConfigValueParser<ConfigurationWrapper<?>, ItemConfig> getItemParser() {
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
