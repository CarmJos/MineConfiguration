package cc.carm.lib.mineconfiguration.bukkit;

import cc.carm.lib.mineconfiguration.bukkit.source.BukkitConfigProvider;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class MineConfiguration {

    public static BukkitConfigProvider from(File file, String source) {
        BukkitConfigProvider provider = new BukkitConfigProvider(file);
        try {
            provider.initializeFile(source);
            provider.initializeConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return provider;
    }

    public static BukkitConfigProvider from(File file) {
        return from(file, file.getName());
    }

    public static BukkitConfigProvider from(String fileName) {
        return from(fileName, fileName);
    }

    public static BukkitConfigProvider from(String fileName, String source) {
        return from(new File(fileName), source);
    }

    public static BukkitConfigProvider from(Plugin plugin, String fileName) {
        return from(plugin, fileName, fileName);
    }

    public static BukkitConfigProvider from(Plugin plugin, String fileName, String source) {
        return from(new File(plugin.getDataFolder(), fileName), source);
    }
}
