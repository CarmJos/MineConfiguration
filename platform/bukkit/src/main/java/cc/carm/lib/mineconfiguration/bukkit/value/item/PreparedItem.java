package cc.carm.lib.mineconfiguration.bukkit.value.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class PreparedItem extends ItemModifier<PreparedItem, ItemStack> {

    public static PreparedItem of(@NotNull Function<@NotNull Player, @Nullable ItemStack> itemProvider) {
        return new PreparedItem(itemProvider);
    }

    public static PreparedItem of(@Nullable ItemStack item) {
        return of(player -> item);
    }

    public PreparedItem(@NotNull Function<@NotNull Player, @Nullable ItemStack> itemProvider) {
        super(itemProvider);
    }

    public @Nullable ItemStack get(Player player) {
        @Nullable ItemStack item = itemProvider.apply(player);
        if (item == null) return null;

        applyTo(item, player);
        return item;
    }

    @Override
    public @NotNull PreparedItem getThis() {
        return this;
    }

}