package cc.carm.lib.configuration.craft.data;

import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class ItemConfig {

    @NotNull Material type;
    short data;
    @Nullable String name;
    @NotNull List<String> lore;
    @NotNull Map<String, List<String>> additional;

    public ItemConfig(@NotNull Material type, short damage,
                      @Nullable String name, @NotNull List<String> lore,
                      @NotNull Map<String, List<String>> additional) {
        this.type = type;
        this.data = damage;
        this.name = name;
        this.lore = lore;
        this.additional = additional;
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

    public @NotNull List<String> getLore() {
        return lore;
    }

    public @NotNull Map<String, List<String>> getAdditionalLore() {
        return additional;
    }

    public @NotNull ItemStack getItemStack() {
        return getItemStack(1);
    }

    public @NotNull ItemStack getItemStack(int amount, @NotNull String... withAdditional) {
        ItemStack item = new ItemStack(type, amount, data);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        if (getName() != null) meta.setDisplayName(getName());

        List<String> finalLore = new ArrayList<>();
        if (!this.lore.isEmpty()) finalLore.addAll(this.lore);

        for (String s : withAdditional) {
            List<String> additional = this.additional.get(s);
            if (additional != null) finalLore.addAll(additional);
        }

        if (!finalLore.isEmpty()) meta.setLore(finalLore);

        item.setItemMeta(meta);
        return item;
    }


    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("type", type.name());
        if (this.data != 0) map.put("data", data);
        if (name != null) map.put("name", name);
        if (!lore.isEmpty()) map.put("lore", lore);
        if (!additional.isEmpty()) map.put("additional", additional);

        return map;
    }

    public static @NotNull ItemConfig deserialize(@NotNull ConfigurationWrapper section) throws Exception {
        String typeName = section.getString("name");
        if (typeName == null) throw new NullPointerException("Item type name is null");

        Material type = Material.matchMaterial(typeName);
        if (type == null) throw new Exception("Invalid material name: " + typeName);
        else return new ItemConfig(
                type, section.getShort("data", (short) 0), section.getString("name"),
                parseStringList(section.getList("lore")),
                readAdditionalLore(section.getConfigurationSection("additional"))
        );
    }

    private static List<String> parseStringList(@Nullable List<?> data) {
        if (data == null) return new ArrayList<>();
        else return data.stream()
                .map(o -> o instanceof String ? (String) o : o.toString())
                .collect(Collectors.toList());
    }

    private static Map<String, List<String>> readAdditionalLore(@Nullable ConfigurationWrapper loreSection) {
        Map<String, List<String>> additionalMap = new HashMap<>();
        if (loreSection == null) return additionalMap;

        for (String loreName : loreSection.getKeys(false)) {
            if (!loreSection.isList(loreName)) continue;

            List<String> additionalLore = parseStringList(loreSection.getList(loreName));
            if (additionalLore.isEmpty()) continue;

            additionalMap.put(loreName, additionalLore);
        }

        return additionalMap;
    }


}
