package cc.carm.lib.mineconfiguration.bukkit;

import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.mineconfiguration.bukkit.source.BukkitConfigFactory;
import cc.carm.lib.mineconfiguration.bukkit.source.BukkitSource;
import cc.carm.lib.mineconfiguration.common.AbstractConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class MineConfiguration extends AbstractConfiguration<ConfigurationHolder<BukkitSource>> {

    public static ConfigurationHolder<BukkitSource> from(File file, String source) {
        return BukkitConfigFactory.from(file).resourcePath(source).build();
    }

    public static ConfigurationHolder<BukkitSource> from(Plugin plugin, String fileName) {
        return from(new File(plugin.getDataFolder(), fileName), fileName);
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
