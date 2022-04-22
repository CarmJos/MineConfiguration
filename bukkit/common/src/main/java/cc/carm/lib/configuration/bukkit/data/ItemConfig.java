package cc.carm.lib.configuration.bukkit.data;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class ItemConfig {

    @NotNull Material type;
    @Nullable String name;
    @Nullable List<String> lore;
    @NotNull Map<String, List<String>> additional;

    public ItemConfig(@NotNull Material type, @Nullable String name,
                      @Nullable List<String> lore, @NotNull Map<String, List<String>> additional) {
        this.type = type;
        this.name = name;
        this.lore = lore;
        this.additional = additional;
    }

    public @NotNull Material getType() {
        return type;
    }

    public @Nullable String getName() {
        return name;
    }

    public @Nullable List<String> getLore() {
        return lore;
    }

    public @NotNull Map<String, List<String>> getAdditionalLore() {
        return additional;
    }




}
