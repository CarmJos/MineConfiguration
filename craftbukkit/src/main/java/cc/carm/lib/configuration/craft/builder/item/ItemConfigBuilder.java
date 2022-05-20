package cc.carm.lib.configuration.craft.builder.item;

import cc.carm.lib.configuration.minecraft.utils.ParamsUtils;
import cc.carm.lib.configuration.craft.builder.AbstractCraftBuilder;
import cc.carm.lib.configuration.craft.data.ItemConfig;
import cc.carm.lib.configuration.craft.value.ConfiguredItem;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ItemConfigBuilder extends AbstractCraftBuilder<ItemConfig, ItemConfigBuilder> {

    protected Material type;
    protected short data;
    protected String name;
    protected List<String> lore = new ArrayList<>();

    protected @NotNull String[] params;
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
        else return new ItemConfig(type, data, name, lore);
    }

    @Override
    public @NotNull ConfiguredItem build() {
        ItemConfig defaultItem = Optional.ofNullable(this.defaultValue).orElse(buildDefault());
        return new ConfiguredItem(this.provider, this.path, this.headerComments, this.inlineComment, defaultItem, buildParams());
    }

    protected final String[] buildParams() {
        return Arrays.stream(params).map(param -> paramFormatter.apply(param)).toArray(String[]::new);
    }

}
