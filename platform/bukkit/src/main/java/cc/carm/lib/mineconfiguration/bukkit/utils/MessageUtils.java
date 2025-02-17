package cc.carm.lib.mineconfiguration.bukkit.utils;

import cc.carm.lib.easyplugin.utils.ColorParser;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MessageUtils {

    private MessageUtils() {
    }

    @Contract("_,!null->!null")
    public static @Nullable String parseMessage(@Nullable CommandSender sender, @Nullable String message) {
        if (message == null) return null;
        if (sender instanceof Player && hasPlaceholderAPI()) {
            message = PlaceholderAPIHelper.parseMessages((Player) sender, message);
        }
        return ColorParser.parse(message);
    }

    public static @NotNull List<String> parseMessage(@Nullable CommandSender sender, @NotNull List<String> messages) {
        if (sender instanceof Player && hasPlaceholderAPI()) {
            messages = PlaceholderAPIHelper.parseMessages((Player) sender, messages);
        }
        return ColorParser.parse(messages);
    }

    public static boolean hasPlaceholderAPI() {
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

}
