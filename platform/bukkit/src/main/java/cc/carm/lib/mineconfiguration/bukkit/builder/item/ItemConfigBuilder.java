package cc.carm.lib.mineconfiguration.bukkit.builder.item;

import cc.carm.lib.configuration.core.value.ValueManifest;
import cc.carm.lib.mineconfiguration.bukkit.builder.AbstractCraftBuilder;
import cc.carm.lib.mineconfiguration.bukkit.data.ItemConfig;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredItem;
import cc.carm.lib.mineconfiguration.common.utils.ParamsUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public class ItemConfigBuilder extends AbstractCraftBuilder<ItemConfig, ItemConfigBuilder> {

    protected Material type;
    protected short data = 0;
    protected String name = null;
    protected List<String> lore = new ArrayList<>();

    protected Map<Enchantment, Integer> enchants = new LinkedHashMap<>();
    protected Set<ItemFlag> flags = new LinkedHashSet<>();

    protected @NotNull String[] params = new String[0];
    protected @NotNull Function<@NotNull String, @NotNull String> paramFormatter = ParamsUtils.DEFAULT_PARAM_FORMATTER;

    public ItemConfigBuilder defaults(@NotNull Material type,
                                      @Nullable String name, @NotNull String... lore) {
        return defaults(type, (short) 0, name, Arrays.asList(lore));
    }

    public ItemConfigBuilder defaults(@NotNull Material type, short data,
                                      @Nullable String name, @NotNull String... lore) {
        return defaults(type, data, name, Arrays.asList(lore));
    }

    public ItemConfigBuilder defaults(@NotNull Material type, short data,
                                      @Nullable String name, @NotNull List<String> lore) {
        return defaultType(type).defaultDataID(data).defaultName(name).defaultLore(lore);
    }

    public ItemConfigBuilder defaultType(@NotNull Material type) {
        this.type = type;
        return this;
    }

    public ItemConfigBuilder defaultName(@Nullable String name) {
        this.name = name;
        return this;
    }

    public ItemConfigBuilder defaultDataID(short dataID) {
        this.data = dataID;
        return this;
    }

    public ItemConfigBuilder defaultLore(@NotNull String... lore) {
        return defaultLore(Arrays.asList(lore));
    }

    public ItemConfigBuilder defaultLore(@NotNull List<String> lore) {
        this.lore = new ArrayList<>(lore);
        return this;
    }

    public ItemConfigBuilder defaultEnchants(@NotNull Map<Enchantment, Integer> enchants) {
        this.enchants = new LinkedHashMap<>(enchants);
        return this;
    }

    public ItemConfigBuilder defaultEnchant(@NotNull Enchantment enchant, int level) {
        return defaultEnchants(Collections.singletonMap(enchant, level));
    }

    public ItemConfigBuilder defaultFlags(@NotNull Set<ItemFlag> flags) {
        this.flags = new LinkedHashSet<>(flags);
        return this;
    }

    public ItemConfigBuilder defaultFlags(@NotNull ItemFlag... flags) {
        return defaultFlags(new LinkedHashSet<>(Arrays.asList(flags)));
    }

    public ItemConfigBuilder formatParam(@NotNull Function<@NotNull String, @NotNull String> paramFormatter) {
        this.paramFormatter = paramFormatter;
        return getThis();
    }

    public ItemConfigBuilder params(@NotNull String... params) {
        this.params = params;
        return getThis();
    }

    public ItemConfigBuilder params(@NotNull List<String> params) {
        this.params = params.toArray(new String[0]);
        return getThis();
    }

    @Override
    protected @NotNull ItemConfigBuilder getThis() {
        return this;
    }

    protected @Nullable ItemConfig buildDefault() {
        if (this.type == null) return null;
        else return new ItemConfig(type, data, name, lore, enchants, flags);
    }

    @Override
    public @NotNull ConfiguredItem build() {
        return new ConfiguredItem(
                new ValueManifest<>(
                        this.provider, this.path, this.headerComments, this.inlineComment,
                        Optional.ofNullable(this.defaultValue).orElse(buildDefault())
                ), buildParams()
        );
    }

    protected final String[] buildParams() {
        return Arrays.stream(params).map(param -> paramFormatter.apply(param)).toArray(String[]::new);
    }

}
