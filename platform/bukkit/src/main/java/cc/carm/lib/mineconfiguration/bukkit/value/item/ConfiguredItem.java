package cc.carm.lib.mineconfiguration.bukkit.value.item;

import cc.carm.lib.configuration.adapter.ValueAdapter;
import cc.carm.lib.configuration.adapter.ValueType;
import cc.carm.lib.configuration.builder.AbstractConfigBuilder;
import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.configuration.source.section.ConfigureSection;
import cc.carm.lib.configuration.value.ValueManifest;
import cc.carm.lib.configuration.value.standard.ConfiguredValue;
import cc.carm.lib.easyplugin.utils.ColorParser;
import com.cryptomorin.xseries.XItemStack;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ConfiguredItem extends ConfiguredValue<ItemStack> {

    public static Builder create() {
        return new Builder();
    }

    public static final @NotNull ValueType<ItemStack> ITEM_TYPE = ValueType.of(ItemStack.class);
    public static final ValueAdapter<ItemStack> ITEM_ADAPTER = new ValueAdapter<>(ITEM_TYPE,
            (holder, type, value) -> XItemStack.serialize(value),
            (holder, type, value) -> {
                ConfigureSection section = (ConfigureSection) value;
                return XItemStack.deserialize(section.asMap());
            }
    );

    protected final @NotNull BiFunction<Player, String, String> parser;
    protected final @NotNull String[] params;

    public ConfiguredItem(@NotNull ValueManifest<ItemStack, ItemStack> manifest, ValueAdapter<ItemStack> adapter,
                          @NotNull BiFunction<Player, String, String> parser, @NotNull String[] params) {
        super(manifest, adapter);
        this.parser = parser;
        this.params = params;
    }

    @Override
    public @NotNull Optional<@Nullable ItemStack> optional() {
        return Optional.ofNullable(super.get());
    }

    @Override
    public @Nullable ItemStack get() {
        return optional().map(ItemStack::clone).orElse(null);
    }

    public @Nullable ItemStack get(Consumer<ItemStack> modifier) {
        return optional().map(item -> {
            modifier.accept(item);
            return item;
        }).orElse(null);
    }

    public @Nullable ItemStack get(@Nullable Player player) {
        return get(player, new HashMap<>());
    }

    public @Nullable ItemStack get(@Nullable Player player, @NotNull Object... values) {
        return prepare(values).get(player);
    }

    public @Nullable ItemStack get(@Nullable Player player,
                                   @NotNull Map<String, Object> placeholders) {
        return prepare().placeholders(placeholders).get(player);
    }

    public @Nullable Map<Integer, ItemStack> give(@NotNull Player player, @NotNull Object... values) {
        return prepare(values).give(player);
    }

    public @NotNull PreparedItem prepare(@NotNull Object... values) {
        return PreparedItem.of(player -> get()).parser(parser).params(params).placeholders(values);
    }

    public void modifyItem(Consumer<ItemStack> modifier) {
        ItemStack item = get();
        if (item == null) return;
        modifier.accept(item);
        set(item);
    }

    public void modifyMeta(Consumer<ItemMeta> modifier) {
        modifyItem(item -> {
            ItemMeta meta = item.getItemMeta();
            modifier.accept(meta);
            item.setItemMeta(meta);
        });
    }

    public void setName(@Nullable String name) {
        modifyMeta(meta -> meta.setDisplayName(name));
    }

    public void setLore(@Nullable List<String> lore) {
        modifyMeta(meta -> meta.setLore(lore));
    }

    public void setLore(String... lore) {
        if (lore.length == 0) setLore((List<String>) null);
        else setLore(Arrays.asList(lore));
    }


    public static class Builder extends AbstractConfigBuilder<ItemStack, ItemStack, ConfiguredItem, ConfigurationHolder<?>, Builder> {

        protected @Nullable ItemStack item = null;
        protected @NotNull String[] params = new String[0];
        protected @NotNull BiFunction<Player, String, String> parser = (player, message) -> ColorParser.parse(message);

        public Builder() {
            super(ConfigurationHolder.class, ITEM_TYPE);
            defaults(() -> item);
        }

        @Override
        public @NotNull Builder defaults(@Nullable ItemStack item) {
            this.item = item;
            return this;
        }

        public Builder defaults(@NotNull Material type) {
            return defaults(new ItemStack(type));
        }

        public Builder defaults(Consumer<ItemStack> consumer) {
            if (this.item == null) return self();
            consumer.accept(this.item);
            return self();
        }

        public Builder defaultMeta(Consumer<ItemMeta> consumer) {
            return defaults(stack -> {
                ItemMeta meta = stack.getItemMeta();
                consumer.accept(meta);
                stack.setItemMeta(meta);
            });
        }

        public Builder defaultType(@NotNull Material type) {
            return defaults(new ItemStack(type));
        }

        public Builder defaultName(@Nullable String name) {
            return defaultMeta(meta -> meta.setDisplayName(name));
        }

        @SuppressWarnings("deprecation")
        public Builder defaultDataID(short dataID) {
            return defaults(stack -> stack.setDurability(dataID));
        }

        public Builder defaultLore(@NotNull String... lore) {
            return defaultLore(Arrays.asList(lore));
        }

        public Builder defaultLore(@NotNull List<String> lore) {
            return defaultMeta(meta -> meta.setLore(lore));
        }

        public Builder defaultEnchants(@NotNull Map<Enchantment, Integer> enchants) {
            return defaultMeta(meta -> enchants.forEach((enchant, level) -> meta.addEnchant(enchant, level, true)));
        }

        public Builder defaultEnchant(@NotNull Enchantment enchant, int level) {
            return defaultEnchants(Collections.singletonMap(enchant, level));
        }

        public Builder defaultFlags(@NotNull Set<ItemFlag> flags) {
            return defaultMeta(meta -> flags.forEach(meta::addItemFlags));
        }

        public Builder defaultFlags(@NotNull ItemFlag... flags) {
            return defaultFlags(new LinkedHashSet<>(Arrays.asList(flags)));
        }

        public Builder parser(@NotNull BiFunction<Player, String, String> parser) {
            this.parser = parser;
            return self();
        }

        public Builder params(@NotNull String... params) {
            this.params = params;
            return self();
        }

        public Builder params(@NotNull List<String> params) {
            this.params = params.toArray(new String[0]);
            return self();
        }

        @Override
        protected @NotNull Builder self() {
            return this;
        }

        @Override
        public @NotNull ConfiguredItem build() {
            return new ConfiguredItem(buildManifest(), ITEM_ADAPTER, parser, params);
        }
    }


}
