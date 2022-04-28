package cc.carm.lib.configuration.craft.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.List;

public class PAPIHelper {

    public static String parseMessages(Player player, String message) {
        return PlaceholderAPI.setPlaceholders(player, message);
    }

    public static List<String> parseMessages(Player player, List<String> messages) {
        return PlaceholderAPI.setPlaceholders(player, messages);
    }

}

