package cc.carm.lib.configuration;

import cc.carm.lib.configuration.bukkit.source.BukkitConfigProvider;

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

}
