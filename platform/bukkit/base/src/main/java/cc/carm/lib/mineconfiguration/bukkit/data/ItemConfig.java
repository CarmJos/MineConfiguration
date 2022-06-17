package cc.carm.lib.mineconfiguration.bukkit.data;

import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import cc.carm.lib.mineconfiguration.bukkit.utils.TextParser;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class ItemConfig {

    protected @NotNull Material type;
    protected short data;
    protected @Nullable String name;
    protected @NotNull List<String> lore;

    protected @NotNull Map<Enchantment, Integer> enchants;
    protected @NotNull Set<ItemFlag> flags;

    public ItemConfig(@NotNull Material type, @Nullable String name) {
        this(type, name, Collections.emptyList());
    }

    public ItemConfig(@NotNull Material type, @Nullable String name, @NotNull List<String> lore) {
        this(type, (short) 0, name, lore);
    }

    public ItemConfig(@NotNull Material type, short damage,
                      @Nullable String name, @NotNull List<String> lore) {
        this(type, damage, name, lore, Collections.emptyMap(), Collections.emptySet());
    }

    public ItemConfig(@NotNull Material type, short damage,
                      @Nullable String name, @NotNull List<String> lore,
                      @NotNull Map<Enchantment, Integer> enchants,
                      @NotNull Set<ItemFlag> flags) {
        this.type = type;
        this.data = damage;
        this.name = name;
        this.lore = lore;
        this.enchants = enchants;
        this.flags = flags;
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
        enchants.forEach((enchant, level) -> meta.addEnchant(enchant, level, true));
        flags.forEach(meta::addItemFlags);
        item.setItemMeta(meta);
        return item;
    }

    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("type", type.name());
        if (this.data != 0) map.put("data", data);
        if (name != null) map.put("name", name);
        if (!lore.isEmpty()) map.put("lore", lore);

        Map<String, Integer> enchantments = new LinkedHashMap<>();
        enchants.forEach((enchant, level) -> {
            if (level > 0) enchantments.put(enchant.getName(), level);
        });

        if (!enchantments.isEmpty()) {
            map.put("enchants", enchantments);
        }

        if (!flags.isEmpty()) {
            map.put("flags", flags.stream().map(ItemFlag::name).collect(Collectors.toList()));
        }
        return map;
    }

    public static @NotNull ItemConfig deserialize(@NotNull ConfigurationWrapper section) throws Exception {
        String typeName = section.getString("type");
        if (typeName == null) throw new NullPointerException("Item type name is null");

        Material type = Material.matchMaterial(typeName);
        if (type == null) throw new Exception("Invalid material name: " + typeName);

        short data = section.getShort("data", (short) 0);
        String name = section.getString("name");
        List<String> lore = section.getStringList("lore");

        Map<Enchantment, Integer> enchantments = readEnchantments(section.getConfigurationSection("enchants"));
        Set<ItemFlag> flags = readFlags(section.getStringList("flags"));

        return new ItemConfig(type, data, name, lore, enchantments, flags);
    }

    private static ItemFlag parseFlag(String flagName) {
        return Arrays.stream(ItemFlag.values()).filter(flag -> flag.name().equalsIgnoreCase(flagName)).findFirst().orElse(null);
    }

    private static Set<ItemFlag> readFlags(List<String> flagConfig) {
        Set<ItemFlag> flags = new LinkedHashSet<>();
        for (String flagName : flagConfig) {
            ItemFlag flag = parseFlag(flagName);
            if (flag != null) flags.add(flag);
        }
        return flags;
    }

    private static Map<Enchantment, Integer> readEnchantments(ConfigurationWrapper section) {
        Map<Enchantment, Integer> enchantments = new LinkedHashMap<>();
        if (section == null) return enchantments;
        section.getKeys(false).forEach(key -> {
            Enchantment enchantment = Enchantment.getByName(key);
            int level = section.getInt(key, 0);
            if (enchantment != null && level > 0) {
                enchantments.put(enchantment, level);
            }
        });
        return enchantments;
    }

}
