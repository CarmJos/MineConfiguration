package cc.carm.lib.mineconfiguration.bukkit.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.List;

public class PlaceholderAPIHelper {

    public static String parseMessages(Player player, String message) {
        try {
            return PlaceholderAPI.setPlaceholders(player, message);
        } catch (Exception ignored) {
            return message;
        }
    }

    public static List<String> parseMessages(Player player, List<String> messages) {
        try {
            return PlaceholderAPI.setPlaceholders(player, messages);
        } catch (Exception ignored) {
            return messages;
        }
    }

}

