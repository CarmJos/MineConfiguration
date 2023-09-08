package cc.carm.lib.mineconfiguration.bukkit.value;

import cc.carm.lib.configuration.core.value.ValueManifest;
import cc.carm.lib.configuration.core.value.type.ConfiguredList;
import cc.carm.lib.configuration.core.value.type.ConfiguredSection;
import cc.carm.lib.mineconfiguration.bukkit.builder.item.ItemConfigBuilder;
import cc.carm.lib.mineconfiguration.bukkit.utils.TextParser;
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

    public static final @NotNull Pattern LORE_INSERT_PATTERN = Pattern.compile("^#(.*)#(\\{\\w+})?$");
    public static final @NotNull Pattern LORE_OFFSET_PATTERN = Pattern.compile("\\{(-?\\d+)(?:,(-?\\d+))?}");

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
    public @Nullable ItemStack get() {
        return Optional.ofNullable(super.get()).map(ItemStack::clone).orElse(null);
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

    public @NotNull PreparedItem prepare(@NotNull Object... values) {
        return new PreparedItem(this, values);
    }

    public @Nullable ItemStack get(@Nullable Player player) {
        return get(player, new HashMap<>());
    }

    public @Nullable ItemStack get(@Nullable Player player, @NotNull Object... values) {
        return get(player, ParamsUtils.buildParams(params, values));
    }

    public @Nullable ItemStack get(@Nullable Player player,
                                   @NotNull Object[] values,
                                   @NotNull Map<String, List<String>> inserted) {
        return get(player, ParamsUtils.buildParams(params, values), inserted);
    }


    public @Nullable ItemStack get(@Nullable Player player, @NotNull String[] params, @NotNull Object[] values) {
        return get(player, ParamsUtils.buildParams(params, values));
    }

    public @Nullable ItemStack get(@Nullable Player player,
                                   @NotNull Map<String, Object> placeholders) {
        return get(player, placeholders, new HashMap<>());
    }

    public @Nullable ItemStack get(@Nullable Player player,
                                   @NotNull Map<String, Object> placeholders,
                                   @NotNull Map<String, List<String>> inserted) {
        return get(item -> {
            ItemMeta meta = item.getItemMeta();
            if (meta == null) return;

            List<String> lore = insertLore(meta.getLore(), inserted);
            if (!lore.isEmpty()) {
                meta.setLore(TextParser.parseList(player, lore, placeholders));
            }

            String name = meta.getDisplayName();
            if (!name.isEmpty()) {
                meta.setDisplayName(TextParser.parseText(player, name, placeholders));
            }

            item.setItemMeta(meta);
        });
    }

    public @Nullable ItemStack get(Consumer<ItemStack> modifier) {
        return getOptional().map(item -> {
            modifier.accept(item);
            return item;
        }).orElse(null);
    }

    public static List<String> insertLore(List<String> original, Map<String, List<String>> inserted) {
        if (original == null) return Collections.emptyList();

        List<String> finalLore = new ArrayList<>();
        for (String line : original) {
            if (line == null) continue;

            Matcher matcher = LORE_INSERT_PATTERN.matcher(line);
            if (!matcher.matches()) {
                finalLore.add(line);
            } else {
                String path = matcher.group(1);
                String offset = matcher.group(2);
                finalLore.addAll(addLoreOffset(inserted.get(path), offset));
            }
        }

        return finalLore;
    }

    public static List<String> addLoreOffset(List<String> lore, String offsetSettings) {
        if (lore == null || lore.isEmpty()) return Collections.emptyList();
        if (offsetSettings == null) return lore;

        Matcher offsetMatcher = LORE_OFFSET_PATTERN.matcher(offsetSettings);
        if (!offsetMatcher.matches()) return lore;

        int upOffset = Optional.ofNullable(offsetMatcher.group(1)).map(Integer::parseInt).orElse(0);
        int downOffset = Optional.ofNullable(offsetMatcher.group(2)).map(Integer::parseInt).orElse(0);

        return addLoreOffset(lore, upOffset, downOffset);
    }

    public static List<String> addLoreOffset(List<String> lore, int upOffset, int downOffset) {
        if (lore == null || lore.isEmpty()) return Collections.emptyList();
        upOffset = Math.max(0, upOffset);
        downOffset = Math.max(0, downOffset);

        ArrayList<String> finalLore = new ArrayList<>(lore);
        for (int i = 0; i < upOffset; i++) finalLore.add(0, " ");
        for (int i = 0; i < downOffset; i++) finalLore.add(finalLore.size(), " ");

        return finalLore;
    }

    public static class PreparedItem {

        protected final @NotNull ConfiguredItem itemConfig;
        protected final @NotNull Map<String, List<String>> insertLore = new HashMap<>();

        protected @NotNull Object[] values;

        protected @NotNull BiConsumer<ItemStack, Player> itemModifier;
        protected @NotNull BiConsumer<ItemMeta, Player> metaModifier;

        protected PreparedItem(@NotNull ConfiguredItem itemConfig, @NotNull Object[] values) {
            this.itemConfig = itemConfig;
            this.values = values;
            itemModifier = (item, player) -> {
            };
            metaModifier = (meta, player) -> {
            };
        }

        public PreparedItem modifyMeta(@NotNull BiConsumer<ItemMeta, Player> modifier) {
            this.metaModifier = this.metaModifier.andThen(modifier);
            return this;
        }

        public PreparedItem modifyItem(@NotNull BiConsumer<ItemStack, Player> modifier) {
            this.itemModifier = this.itemModifier.andThen(modifier);
            return this;
        }

        public PreparedItem insertLore(String path, List<String> content) {
            insertLore.put(path, content);
            return this;
        }

        public PreparedItem insertLore(String path, String... content) {
            return insertLore(path, Arrays.asList(content));
        }

        public PreparedItem insertLore(String path, ConfiguredList<String> content) {
            return insertLore(path, content.copy());
        }

        public PreparedItem insertLore(String path, ConfiguredMessage<String> content, Object... params) {
            return insertLore(path, content.parse(null, params));
        }

        public PreparedItem insertLore(String path, ConfiguredMessageList<String> content, Object... params) {
            return insertLore(path, content.parse(null, params));
        }

        public PreparedItem values(Object... values) {
            this.values = values;
            return this;
        }

        public PreparedItem amount(int amount) {
            return modifyItem((item, player) -> item.setAmount(amount));
        }

        public PreparedItem addEnchantment(Enchantment e) {
            return addEnchantment(e, 1);
        }

        public PreparedItem addEnchantment(Enchantment e, int level) {
            return addEnchantment(e, level, true);
        }

        public PreparedItem addEnchantment(Enchantment e, int level, boolean ignoreLevelRestriction) {
            return modifyMeta((meta, player) -> meta.addEnchant(e, level, ignoreLevelRestriction));
        }

        public PreparedItem addItemFlags(ItemFlag... flags) {
            return modifyMeta((meta, player) -> meta.addItemFlags(flags));
        }

        public PreparedItem glow() {
            return addItemFlags(ItemFlag.HIDE_ENCHANTS).addEnchantment(Enchantment.DURABILITY);
        }

        /**
         * @param owner 玩家名
         * @return this
         * @deprecated Use {@link #setSkullOwner(OfflinePlayer)} instead.
         */
        @Deprecated
        public PreparedItem setSkullOwner(String owner) {
            return modifyItem((item, player) -> {
                if (!(item.getItemMeta() instanceof SkullMeta)) return;
                SkullMeta meta = (SkullMeta) item.getItemMeta();
                meta.setOwner(owner);
            });
        }

        public PreparedItem setSkullOwner(UUID owner) {
            return setSkullOwner(Bukkit.getOfflinePlayer(owner));
        }

        public PreparedItem setSkullOwner(OfflinePlayer owner) {
            return modifyItem((item, player) -> {
                if (!(item.getItemMeta() instanceof SkullMeta)) return;
                SkullMeta meta = (SkullMeta) item.getItemMeta();
                meta.setOwningPlayer(owner);
            });
        }

        public @Nullable ItemStack get(Player player) {
            return Optional.ofNullable(itemConfig.get(player, values, insertLore)).map(item -> {
                itemModifier.accept(item, player);

                ItemMeta meta = item.getItemMeta();
                if (meta == null) return item;

                metaModifier.accept(meta, player);
                item.setItemMeta(meta);

                return item;
            }).orElse(null);
        }

    }
}
