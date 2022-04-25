package cc.carm.lib.configuration.craft.data;

import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public @NotNull List<String> getLore() {
        return lore;
    }

    public final @NotNull ItemStack getItemStack() {
        return getItemStack(1);
    }

    public @NotNull ItemStack getItemStack(int amount) {
        ItemStack item = new ItemStack(type, amount, data);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;
        if (getName() != null) meta.setDisplayName(getName());
        if (!getLore().isEmpty()) meta.setLore(getLore());
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
                parseStringList(section.getList("lore"))
        );
    }

    private static List<String> parseStringList(@Nullable List<?> data) {
        if (data == null) return new ArrayList<>();
        else return data.stream()
                .map(o -> o instanceof String ? (String) o : o.toString())
                .collect(Collectors.toList());
    }

}
