package cc.carm.lib.mineconfiguration.bukkit.value.item;

import cc.carm.lib.configuration.core.value.ValueManifest;
import cc.carm.lib.configuration.core.value.type.ConfiguredList;
import cc.carm.lib.configuration.core.value.type.ConfiguredSection;
import cc.carm.lib.mineconfiguration.bukkit.builder.item.ItemConfigBuilder;
import cc.carm.lib.mineconfiguration.bukkit.utils.TextParser;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessage;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessageList;
import cc.carm.lib.mineconfiguration.common.utils.ParamsUtils;
import com.cryptomorin.xseries.XItemStack;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfiguredItem extends ConfiguredSection<ItemStack> {


    public static ItemConfigBuilder create() {
        return new ItemConfigBuilder();
    }

    protected final @NotNull String[] params;

    public ConfiguredItem(@NotNull ValueManifest<ItemStack> manifest, @NotNull String[] params) {
        super(
                manifest, ItemStack.class,
                (data, v) -> XItemStack.deserialize((ConfigurationSection) data.getSource()),
                XItemStack::serialize
        );
        this.params = params;
    }

    public @NotNull String[] getParams() {
        return params;
    }

    @Override
    public @NotNull Optional<@Nullable ItemStack> getOptional() {
        return Optional.ofNullable(super.get());
    }

    @Override
    public @Nullable ItemStack get() {
        return getOptional().map(ItemStack::clone).orElse(null);
    }

    public @Nullable ItemStack get(Consumer<ItemStack> modifier) {
        return getOptional().map(item -> {
            modifier.accept(item);
            return item;
        }).orElse(null);
    }

    public @NotNull PreparedItem prepare(@NotNull Object... values) {
        return new PreparedItem(this, values);
    }

    public @Nullable ItemStack get(@Nullable Player player) {
        return get(player, new HashMap<>());
    }

    public @Nullable ItemStack get(@Nullable Player player, @NotNull Object... values) {
        return prepare(values).get(player);
    }

    public @Nullable ItemStack get(@Nullable Player player, @NotNull String[] params, @NotNull Object[] values) {
        return prepare().params(params).values(values).get(player);
    }

    public @Nullable ItemStack get(@Nullable Player player,
                                   @NotNull Map<String, Object> placeholders) {
        return prepare().placeholders(placeholders).get(player);
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

}
