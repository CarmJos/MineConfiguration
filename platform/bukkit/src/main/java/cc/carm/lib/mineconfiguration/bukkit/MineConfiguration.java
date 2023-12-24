package cc.carm.lib.mineconfiguration.bukkit;

import cc.carm.lib.configuration.core.Configuration;
import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.mineconfiguration.bukkit.source.BukkitConfigProvider;
import cc.carm.lib.mineconfiguration.common.AbstractConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class MineConfiguration extends AbstractConfiguration<BukkitConfigProvider> {

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

    public MineConfiguration(@NotNull JavaPlugin plugin) {
        super(from(plugin, "config.yml"), from(plugin, "messages.yml"));
    }

    public MineConfiguration(@NotNull JavaPlugin plugin,
                             @NotNull Configuration configRoot,
                             @NotNull Configuration messageRoot) {
        this(plugin);
        initializeConfig(configRoot);
        initializeMessage(messageRoot);
    }

    public MineConfiguration(@NotNull JavaPlugin plugin,
                             @NotNull Class<? extends Configuration> configRoot,
                             @NotNull Class<? extends Configuration> messageRoot) {
        this(plugin);
        initializeConfig(configRoot);
        initializeMessage(messageRoot);
    }

}
