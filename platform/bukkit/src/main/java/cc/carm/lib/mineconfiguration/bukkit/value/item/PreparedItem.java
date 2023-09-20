package cc.carm.lib.mineconfiguration.bukkit.value.item;

import cc.carm.lib.configuration.core.value.type.ConfiguredList;
import cc.carm.lib.mineconfiguration.bukkit.utils.TextParser;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessage;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessageList;
import cc.carm.lib.mineconfiguration.common.utils.ParamsUtils;
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

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PreparedItem {

    public static final @NotNull Pattern LORE_INSERT_PATTERN = Pattern.compile("^(?:\\{(.*)})?#(.*)#(?:\\{(-?\\d+)(?:,(-?\\d+))?})?$");

    protected final @NotNull ConfiguredItem itemConfig;

    protected @NotNull Map<String, Object> placeholders = new HashMap<>();
    protected @NotNull String[] params;
    protected @NotNull Object[] values;

    protected final @NotNull Map<String, LoreContent> insertLore = new HashMap<>();

    protected @NotNull BiConsumer<ItemStack, Player> itemModifier;
    protected @NotNull BiConsumer<ItemMeta, Player> metaModifier;

    protected PreparedItem(@NotNull ConfiguredItem itemConfig, @NotNull Object[] values) {
        this.itemConfig = itemConfig;
        this.params = itemConfig.params;
        this.values = values;
        itemModifier = (item, player) -> {
        };
        metaModifier = (meta, player) -> {
        };
    }

    public @Nullable ItemStack get(Player player) {
        ItemStack item = itemConfig.get();
        if (item == null) return null;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;


        Map<String, Object> finalPlaceholders = buildPlaceholders();

        String name = meta.getDisplayName();
        if (!name.isEmpty()) {
            meta.setDisplayName(TextParser.parseText(player, name, finalPlaceholders));
        }

        List<String> parsedLore = parseLore(player, meta.getLore(), insertLore, finalPlaceholders);
        if (!parsedLore.isEmpty()) {
            meta.setLore(parsedLore);
        }

        metaModifier.accept(meta, player);
        item.setItemMeta(meta);
        itemModifier.accept(item, player);
        return item;
    }

    public PreparedItem handleMeta(@NotNull BiConsumer<ItemMeta, Player> modifier) {
        this.metaModifier = this.metaModifier.andThen(modifier);
        return this;
    }

    public PreparedItem handleItem(@NotNull BiConsumer<ItemStack, Player> modifier) {
        this.itemModifier = this.itemModifier.andThen(modifier);
        return this;
    }

    public PreparedItem params(String[] params) {
        this.params = params;
        return this;
    }

    public PreparedItem values(Object... values) {
        this.values = values;
        return this;
    }

    public PreparedItem placeholders(Map<String, Object> placeholders) {
        this.placeholders = placeholders;
        return this;
    }

    public PreparedItem placeholders(Consumer<Map<String, Object>> consumer) {
        Map<String, Object> placeholders = new HashMap<>();
        consumer.accept(placeholders);
        return placeholders(placeholders);
    }

    public PreparedItem insertLore(String path, LoreContent content) {
        insertLore.put(path, content);
        return this;
    }

    public PreparedItem insertLore(String path, List<String> content) {
        return insertLore(path, content, false);
    }

    public PreparedItem insertLore(String path, List<String> content, boolean original) {
        return insertLore(path, LoreContent.of(content, original));
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

    public PreparedItem amount(int amount) {
        return handleItem((item, player) -> item.setAmount(amount));
    }

    public PreparedItem addEnchantment(Enchantment e) {
        return addEnchantment(e, 1);
    }

    public PreparedItem addEnchantment(Enchantment e, int level) {
        return addEnchantment(e, level, true);
    }

    public PreparedItem addEnchantment(Enchantment e, int level, boolean ignoreLevelRestriction) {
        return handleMeta((meta, player) -> meta.addEnchant(e, level, ignoreLevelRestriction));
    }

    public PreparedItem addItemFlags(ItemFlag... flags) {
        return handleMeta((meta, player) -> meta.addItemFlags(flags));
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
        return handleItem((item, player) -> {
            if (!(item.getItemMeta() instanceof SkullMeta)) return;
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.setOwner(owner);
        });
    }

    public PreparedItem setSkullOwner(UUID owner) {
        return setSkullOwner(Bukkit.getOfflinePlayer(owner));
    }

    public PreparedItem setSkullOwner(OfflinePlayer owner) {
        return handleItem((item, player) -> {
            if (!(item.getItemMeta() instanceof SkullMeta)) return;
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.setOwningPlayer(owner);
        });
    }


    protected Map<String, Object> buildPlaceholders() {
        Map<String, Object> finalPlaceholders = new HashMap<>();
        finalPlaceholders.putAll(ParamsUtils.buildParams(params, values));
        finalPlaceholders.putAll(this.placeholders);
        return finalPlaceholders;
    }

    public static List<String> parseLore(@Nullable Player player, @Nullable List<String> lore,
                                         @NotNull Map<String, LoreContent> insertedLore,
                                         @NotNull Map<String, Object> placeholders) {
        List<String> parsedLore = new ArrayList<>();
        if (lore == null || lore.isEmpty()) return parsedLore;

        for (String line : lore) {
            Matcher matcher = PreparedItem.LORE_INSERT_PATTERN.matcher(line);
            if (!matcher.matches()) {
                parsedLore.add(TextParser.parseText(player, line, placeholders));
                continue;
            }

            String path = matcher.group(2);
            LoreContent content = insertedLore.get(path);
            if (content == null) continue;

            String prefix = matcher.group(1);
            int offset1 = Optional.ofNullable(matcher.group(3))
                    .map(Integer::parseInt).orElse(0);
            Integer offset2 = Optional.ofNullable(matcher.group(4))
                    .map(Integer::parseInt).orElse(null);

            List<String> inserted = generateLore(
                    content.getContent(), prefix,
                    offset2 == null ? 0 : offset1, offset2 == null ? offset1 : offset2
            );

            if (content.isOriginal()) {
                parsedLore.addAll(inserted);
            } else {
                parsedLore.addAll(TextParser.parseList(player, inserted, placeholders));
            }
        }
        return parsedLore;
    }

    public static List<String> generateLore(List<String> lore, String prefix, int upOffset, int downOffset) {
        if (lore == null || lore.isEmpty()) return Collections.emptyList();
        upOffset = Math.max(0, upOffset);
        downOffset = Math.max(0, downOffset);

        String finalPrefix = prefix == null ? "" : prefix;

        ArrayList<String> finalLore = lore.stream().map(s -> finalPrefix + s)
                .collect(Collectors.toCollection(ArrayList::new));
        for (int i = 0; i < upOffset; i++) finalLore.add(0, " ");
        for (int i = 0; i < downOffset; i++) finalLore.add(finalLore.size(), " ");

        return finalLore;
    }

}