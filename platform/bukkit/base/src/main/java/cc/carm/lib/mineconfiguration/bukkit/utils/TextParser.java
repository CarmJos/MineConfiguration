package cc.carm.lib.mineconfiguration.bukkit.utils;

import cc.carm.lib.mineconfiguration.common.utils.ColorParser;
import cc.carm.lib.mineconfiguration.common.utils.ParamsUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TextParser {

    @Contract("_,!null,_->!null")
    public static @Nullable String parseText(@Nullable CommandSender sender, @Nullable String message, @NotNull Map<String, Object> placeholders) {
        if (message == null) return null;
        if (sender instanceof Player && hasPlaceholderAPI()) {
            message = PlaceholderAPIHelper.parseMessages((Player) sender, message);
        }
        return ColorParser.parse(ParamsUtils.setPlaceholders(message, placeholders));
    }

    public static @NotNull List<String> parseList(@Nullable CommandSender sender, List<String> messages, @NotNull Map<String, Object> placeholders) {
        if (sender instanceof Player && hasPlaceholderAPI()) {
            messages = PlaceholderAPIHelper.parseMessages((Player) sender, messages);
        }
        return ColorParser.parse(messages.stream().map(s -> ParamsUtils.setPlaceholders(s, placeholders)).collect(Collectors.toList()));
    }

    public static boolean hasPlaceholderAPI() {
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

}
