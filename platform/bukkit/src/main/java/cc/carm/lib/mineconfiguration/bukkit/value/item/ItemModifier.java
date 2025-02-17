package cc.carm.lib.mineconfiguration.bukkit.value.item;

import cc.carm.lib.configuration.value.text.data.TextContents;
import cc.carm.lib.configuration.value.text.function.ContentHandler;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Pattern;

public abstract class ItemModifier<S extends ItemModifier<S, R>, R>
        extends ContentHandler<Player, S> {

    public static final @NotNull Pattern LORE_INSERT_PATTERN = Pattern.compile("^(?:\\{(.*)})?#(.*)#(?:\\{(-?\\d+)(?:,(-?\\d+))?})?$");

    protected final @NotNull Function<@NotNull Player, @Nullable ItemStack> itemProvider;

    protected @NotNull BiConsumer<ItemStack, Player> itemConsumer;
    protected @NotNull BiConsumer<ItemMeta, Player> metaConsumer;

    protected ItemModifier(@NotNull Function<@NotNull Player, @Nullable ItemStack> itemProvider) {
        super();
        this.itemProvider = itemProvider;
        this.itemConsumer = (item, player) -> {
        };
        this.metaConsumer = (meta, player) -> {
        };
        this.lineSeparator = " ";
    }

    public abstract @Nullable R get(Player player);

    public void applyTo(@Nullable ItemStack item, @Nullable Player player) {
        if (item == null) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        String name = meta.getDisplayName();
        if (!name.isEmpty()) {
            meta.setDisplayName(parse(player, name));
        }

        List<String> parsedLore = parseLore(player, meta.getLore());
        if (!parsedLore.isEmpty()) {
            meta.setLore(parsedLore);
        }

        metaConsumer.accept(meta, player);
        item.setItemMeta(meta);
        itemConsumer.accept(item, player);
    }

    public S handleMeta(@NotNull BiConsumer<ItemMeta, Player> modifier) {
        this.metaConsumer = this.metaConsumer.andThen(modifier);
        return self();
    }

    public S handleItem(@NotNull BiConsumer<ItemStack, Player> modifier) {
        this.itemConsumer = this.itemConsumer.andThen(modifier);
        return self();
    }

    public S insert(@NotNull String key, @NotNull ConfiguredMessage<?> message,
                    @NotNull Object... values) {
        return insert(key, receiver -> message.parse(receiver, values));
    }

    public S amount(int amount) {
        return handleItem((item, player) -> item.setAmount(amount));
    }

    public S addEnchantment(Enchantment e) {
        return addEnchantment(e, 1);
    }

    public S addEnchantment(Enchantment e, int level) {
        return addEnchantment(e, level, true);
    }

    public S addEnchantment(Enchantment e, int level, boolean ignoreLevelRestriction) {
        return handleMeta((meta, player) -> meta.addEnchant(e, level, ignoreLevelRestriction));
    }

    public S addItemFlags(ItemFlag... flags) {
        return handleMeta((meta, player) -> meta.addItemFlags(flags));
    }

    public S glow() {
        return addItemFlags(ItemFlag.HIDE_ENCHANTS).addEnchantment(Enchantment.DURABILITY);
    }

    /**
     * @param owner 玩家名
     * @return this
     * @deprecated Use {@link #setSkullOwner(OfflinePlayer)} instead.
     */
    @Deprecated
    public S setSkullOwner(String owner) {
        return handleMeta((meta, player) -> {
            if (!(meta instanceof SkullMeta)) return;
            SkullMeta skullMeta = (SkullMeta) meta;
            skullMeta.setOwner(owner);
        });
    }

    public S setSkullOwner(UUID owner) {
        return setSkullOwner(Bukkit.getOfflinePlayer(owner));
    }

    public S setSkullOwner(OfflinePlayer owner) {
        return handleMeta((meta, player) -> {
            if (!(meta instanceof SkullMeta)) return;
            SkullMeta skullMeta = (SkullMeta) meta;
            skullMeta.setOwningPlayer(owner);
        });
    }

    public List<String> parseLore(@Nullable Player player, @Nullable List<String> current) {
        if (current == null || current.isEmpty()) return new ArrayList<>();
        List<String> parsed = new ArrayList<>();
        handle(TextContents.of(current, new HashMap<>()), player, parsed::add);
        return parsed;
    }


}
