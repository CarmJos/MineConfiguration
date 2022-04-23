package cc.carm.lib.configuration.craft.builder.item;

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

public class ItemConfigBuilder extends AbstractCraftBuilder<ItemConfig, ItemConfigBuilder> {

    protected Material type;
    protected short data;
    protected String name;
    protected List<String> lore = new ArrayList<>();

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
        return defaults(new ItemConfig(type, data, name, lore));
    }

    public ItemConfigBuilder defaultType(@NotNull Material type) {
        this.type = type;
        return this;
    }

    public ItemConfigBuilder defaultName(@NotNull String name) {
        this.name = name;
        return this;
    }

    public ItemConfigBuilder defaultDataID(short dataID) {
        this.data = dataID;
        return this;
    }

    public ItemConfigBuilder defaultLore(@NotNull String... lore) {
        this.lore = new ArrayList<>(Arrays.asList(lore));
        return this;
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
        return new ConfiguredItem(this.provider, this.path, buildComments(), defaultItem);
    }

}
