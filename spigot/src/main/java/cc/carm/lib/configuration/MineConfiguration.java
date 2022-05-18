package cc.carm.lib.configuration;

import cc.carm.lib.configuration.spigot.source.SpigotConfigProvider;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class MineConfiguration {

    public static SpigotConfigProvider from(File file, String source) {
        SpigotConfigProvider provider = new SpigotConfigProvider(file);
        try {
            provider.initializeFile(source);
            provider.initializeConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return provider;
    }

    public static SpigotConfigProvider from(File file) {
        return from(file, file.getName());
    }

    public static SpigotConfigProvider from(String fileName) {
        return from(fileName, fileName);
    }

    public static SpigotConfigProvider from(String fileName, String source) {
        return from(new File(fileName), source);
    }

    public static SpigotConfigProvider from(Plugin plugin, String fileName) {
        return from(plugin, fileName, fileName);
    }

    public static SpigotConfigProvider from(Plugin plugin, String fileName, String source) {
        return from(new File(plugin.getDataFolder(), fileName), source);
    }

}
