package cc.carm.lib.mineconfiguration.bukkit;

import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.mineconfiguration.bukkit.source.BukkitConfigFactory;
import cc.carm.lib.mineconfiguration.bukkit.source.BukkitSource;
import cc.carm.lib.mineconfiguration.common.AbstractConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class MineConfiguration extends AbstractConfiguration<ConfigurationHolder<BukkitSource>> {

    public static ConfigurationHolder<BukkitSource> from(@NotNull File file,
                                                         @Nullable String source) {
        return BukkitConfigFactory.from(file).resourcePath(source).build();
    }

    public static ConfigurationHolder<BukkitSource> from(@NotNull Plugin plugin,
                                                         @NotNull String fileName) {
        return from(plugin, fileName, null);
    }

    public static ConfigurationHolder<BukkitSource> from(@NotNull Plugin plugin,
                                                         @NotNull String fileName, @Nullable String resource) {
        return from(new File(plugin.getDataFolder(), fileName), resource);
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
