package cc.carm.lib.mineconfiguration.bukkit.data;

import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import cc.carm.lib.mineconfiguration.bukkit.utils.TextParser;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ItemConfig {

    protected @NotNull Material type;
    protected short data;
    protected @Nullable String name;
    protected @NotNull List<String> lore;

    public ItemConfig(@NotNull Material type, short damage,
                      @Nullable String name, @NotNull List<String> lore) {
        this.type = type;
        this.data = damage;
        this.name = name;
        this.lore = lore;
    }

    public @NotNull Material getType() {
        return type;
    }

    public short getData() {
        return data;
    }

    public @Nullable String getName() {
        return name;
    }

    public @Nullable String getName(@Nullable Player player, @NotNull Map<String, Object> placeholders) {
        return Optional.ofNullable(getName())
                .map(name -> TextParser.parseText(player, name, placeholders))
                .orElse(null);
    }

    public @NotNull List<String> getLore() {
        return lore;
    }

    public @Nullable List<String> getLore(@Nullable Player player, @NotNull Map<String, Object> placeholders) {
        if (getLore().isEmpty()) return null;
        else return TextParser.parseList(player, getLore(), placeholders);
    }

    public final @NotNull ItemStack getItemStack() {
        return getItemStack(1);
    }

    public @NotNull ItemStack getItemStack(int amount) {
        return getItemStack(null, amount, new HashMap<>());
    }

    public @NotNull ItemStack getItemStack(@Nullable Player player) {
        return getItemStack(player, new HashMap<>());
    }

    public @NotNull ItemStack getItemStack(@Nullable Player player, @NotNull Map<String, Object> placeholders) {
        return getItemStack(player, 1, placeholders);
    }

    public @NotNull ItemStack getItemStack(@Nullable Player player, int amount, @NotNull Map<String, Object> placeholders) {
        ItemStack item = new ItemStack(type, amount, data);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;
        Optional.ofNullable(getName(player, placeholders)).ifPresent(meta::setDisplayName);
        Optional.ofNullable(getLore(player, placeholders)).ifPresent(meta::setLore);
        item.setItemMeta(meta);
        return item;
    }

    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("type", type.name());
        if (this.data != 0) map.put("data", data);
        if (name != null) map.put("name", name);
        if (!lore.isEmpty()) map.put("lore", lore);

        return map;
    }

    public static @NotNull ItemConfig deserialize(@NotNull ConfigurationWrapper section) throws Exception {
        String typeName = section.getString("type");
        if (typeName == null) throw new NullPointerException("Item type name is null");

        Material type = Material.matchMaterial(typeName);
        if (type == null) throw new Exception("Invalid material name: " + typeName);
        else return new ItemConfig(
                type, section.getShort("data", (short) 0),
                section.getString("name"),
                section.getStringList("lore")
        );
    }

}
